package com.kobylynskyi.graphql.codegen;

import com.kobylynskyi.graphql.codegen.mapper.EnumDefinitionToDataModelMapper;
import com.kobylynskyi.graphql.codegen.mapper.FieldDefinitionToDataModelMapper;
import com.kobylynskyi.graphql.codegen.mapper.InputTypeDefinitionToDataModelMapper;
import com.kobylynskyi.graphql.codegen.mapper.TypeDefinitionToDataModelMapper;
import com.kobylynskyi.graphql.codegen.model.DataModelFields;
import com.kobylynskyi.graphql.codegen.model.MappingConfig;
import com.kobylynskyi.graphql.codegen.utils.Utils;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import graphql.language.*;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.util.List;
import java.util.Map;

/**
 * Generator of:
 * - Interface for each GraphQL query
 * - Interface for each GraphQL mutation
 * - Interface for each GraphQL subscription
 * - Class for each GraphQL data type
 * - Class for each GraphQL enum type
 * - Class for each GraphQL scalar type
 *
 * @author kobylynskyi
 */
@Getter
@Setter
class GraphqlCodegen {

    private List<String> schemas;
    private String outputDir;
    private MappingConfig mappingConfig;

    private File classesOutputDir;

    GraphqlCodegen(List<String> schemas, String outputDir, MappingConfig mappingConfig) {
        this.schemas = schemas;
        this.outputDir = outputDir;
        this.mappingConfig = mappingConfig;
    }

    void generate() throws IOException, TemplateException {
        classesOutputDir = prepareOutputDir(outputDir, mappingConfig);
        for (String schema : schemas) {
            Document document = GraphqlDocumentParser.getDocument(schema);
            addScalarsToCustomMappingConfig(document);
            processDocument(document);
        }
    }

    private void processDocument(Document document) throws IOException, TemplateException {
        for (Definition definition : document.getDefinitions()) {
            if (definition instanceof ObjectTypeDefinition) {
                ObjectTypeDefinition typeDef = (ObjectTypeDefinition) definition;
                if (Utils.isGraphqlOperation(typeDef.getName())) {
                    for (FieldDefinition fieldDef : typeDef.getFieldDefinitions()) {
                        Map<String, Object> dataModel = FieldDefinitionToDataModelMapper.map(fieldDef, typeDef.getName(), mappingConfig);
                        generateFile(FreeMarkerTemplatesRegistry.operationTemplate, dataModel);
                    }
                } else {
                    Map<String, Object> dataModel = TypeDefinitionToDataModelMapper.map(typeDef, mappingConfig);
                    generateFile(FreeMarkerTemplatesRegistry.typeTemplate, dataModel);
                }
            } else if (definition instanceof EnumTypeDefinition) {
                EnumTypeDefinition typeDef = (EnumTypeDefinition) definition;
                Map<String, Object> dataModel = EnumDefinitionToDataModelMapper.map(typeDef, mappingConfig);
                generateFile(FreeMarkerTemplatesRegistry.enumTemplate, dataModel);
            } else if (definition instanceof InputObjectTypeDefinition) {
                InputObjectTypeDefinition typeDef = (InputObjectTypeDefinition) definition;
                Map<String, Object> dataModel = InputTypeDefinitionToDataModelMapper.map(typeDef, mappingConfig);
                generateFile(FreeMarkerTemplatesRegistry.typeTemplate, dataModel);
            }
            // TODO: Add support of union
            // TODO: Add support of interface
        }
    }

    private void addScalarsToCustomMappingConfig(Document document) {
        for (Definition definition : document.getDefinitions()) {
            if (definition instanceof ScalarTypeDefinition) {
                String scalarName = ((ScalarTypeDefinition) definition).getName();
                if (!mappingConfig.getCustomTypesMapping().containsKey(scalarName)) {
                    mappingConfig.getCustomTypesMapping().put(scalarName, "String");
                }
            }
        }
    }

    private void generateFile(Template template, Map<String, Object> dataModel) throws IOException, TemplateException {
        File javaSourceFile = new File(classesOutputDir, dataModel.get(DataModelFields.CLASS_NAME) + ".java");
        boolean fileCreated = javaSourceFile.createNewFile();
        if (!fileCreated) {
            throw new FileAlreadyExistsException("File already exists: " + javaSourceFile.getPath());
        }
        template.process(dataModel, new FileWriter(javaSourceFile));

    }

    private static File prepareOutputDir(String outputDir, MappingConfig mappingConfig) {
        File outputDirFolder = new File(outputDir);
        Utils.deleteFolder(outputDirFolder);

        File targetDir;
        String javaPackage = mappingConfig.getJavaPackage();
        if (javaPackage == null || javaPackage.trim().isEmpty()) {
            targetDir = outputDirFolder;
        } else {
            targetDir = new File(outputDir, javaPackage.replace(".", File.separator));
        }
        boolean outputDirCreated = targetDir.mkdirs();
        if (!outputDirCreated) {
            throw new CodeGenerationException("Unable to create output directory");
        }
        return targetDir;
    }

}
