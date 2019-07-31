package com.kobylynskyi.graphql.codegen;

import com.kobylynskyi.graphql.codegen.mapper.EnumDefinitionToDataModelMapper;
import com.kobylynskyi.graphql.codegen.mapper.FieldDefinitionToDataModelMapper;
import com.kobylynskyi.graphql.codegen.mapper.TypeDefinitionToDataModelMapper;
import com.kobylynskyi.graphql.codegen.model.DataModelFields;
import com.kobylynskyi.graphql.codegen.model.MappingConfig;
import com.kobylynskyi.graphql.codegen.utils.Utils;
import freemarker.template.Template;
import graphql.language.*;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.io.FileWriter;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

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

    private List<File> schemas;
    private File outputDir;
    private MappingConfig mappingConfig;

    private File classesOutputDir;

    GraphqlCodegen(List<File> schemas, File outputDir, MappingConfig mappingConfig) {
        this.schemas = schemas;
        this.outputDir = outputDir;
        this.mappingConfig = mappingConfig;
    }

    void generate() {
        classesOutputDir = prepareOutputDir(outputDir, mappingConfig);
        List<Document> graphqlDocuments = schemas.stream().map(GraphqlDocumentParser::getDocument).collect(toList());
        graphqlDocuments.forEach(this::addScalarsToCustomMappingConfig);
        graphqlDocuments.forEach(this::processDocument);
    }

    private void processDocument(Document document) {
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
            }
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

    private void generateFile(Template template, Map<String, Object> dataModel) {
        File javaSourceFile = new File(classesOutputDir, dataModel.get(DataModelFields.CLASS_NAME) + ".java");
        try {
            boolean fileCreated = javaSourceFile.createNewFile();
            if (!fileCreated) {
                throw new CodeGenerationException("Failed to create a file: " + javaSourceFile.getName());
            }
            template.process(dataModel, new FileWriter(javaSourceFile));
        } catch (Exception e) {
            throw new CodeGenerationException(e);
        }
    }

    private static File prepareOutputDir(File outputDir, MappingConfig mappingConfig) {
        Utils.deleteFolder(outputDir);

        File targetDir;
        String javaPackage = mappingConfig.getJavaPackage();
        if (javaPackage == null || javaPackage.trim().isEmpty()) {
            targetDir = outputDir;
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
