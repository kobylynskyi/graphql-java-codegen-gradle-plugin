package com.kobylynskyi.graphql.codegen;

import com.kobylynskyi.graphql.codegen.mapper.*;
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
            DefinitionType definitionType = DefinitionTypeDeterminer.determine(definition);
            switch (definitionType) {
                case OPERATION:
                    generateOperation((ObjectTypeDefinition) definition);
                    break;
                case TYPE:
                    generateType((ObjectTypeDefinition) definition, document);
                    break;
                case INTERFACE:
                    generateInterface((InterfaceTypeDefinition) definition);
                    break;
                case ENUM:
                    generateEnum((EnumTypeDefinition) definition);
                    break;
                case INPUT:
                    generateInput((InputObjectTypeDefinition) definition);
                    break;
                case UNION:
                    generateUnion((UnionTypeDefinition) definition);
            }
        }
    }

    private void generateUnion(UnionTypeDefinition definition) throws IOException, TemplateException {
        Map<String, Object> dataModel = UnionDefinitionToDataModelMapper.map(mappingConfig, definition);
        generateFile(FreeMarkerTemplatesRegistry.unionTemplate, dataModel);
    }

    private void generateInterface(InterfaceTypeDefinition definition) throws IOException, TemplateException {
        Map<String, Object> dataModel = InterfaceDefinitionToDataModelMapper.map(mappingConfig, definition);
        generateFile(FreeMarkerTemplatesRegistry.typeTemplate, dataModel);
    }

    private void generateOperation(ObjectTypeDefinition definition) throws IOException, TemplateException {
        for (FieldDefinition fieldDef : definition.getFieldDefinitions()) {
            Map<String, Object> dataModel = FieldDefinitionToDataModelMapper.map(mappingConfig, fieldDef, definition.getName());
            generateFile(FreeMarkerTemplatesRegistry.operationsTemplate, dataModel);
        }
        // We need to generate a root object to workaround https://github.com/facebook/relay/issues/112
        Map<String, Object> dataModel = ObjectDefinitionToDataModelMapper.map(mappingConfig, definition);
        generateFile(FreeMarkerTemplatesRegistry.operationsTemplate, dataModel);
    }

    private void generateType(ObjectTypeDefinition definition, Document document) throws IOException, TemplateException {
        Map<String, Object> dataModel = TypeDefinitionToDataModelMapper.map(mappingConfig, definition, document);
        generateFile(FreeMarkerTemplatesRegistry.typeTemplate, dataModel);
    }

    private void generateInput(InputObjectTypeDefinition definition) throws IOException, TemplateException {
        Map<String, Object> dataModel = InputDefinitionToDataModelMapper.map(mappingConfig, definition);
        generateFile(FreeMarkerTemplatesRegistry.typeTemplate, dataModel);
    }

    private void generateEnum(EnumTypeDefinition definition) throws IOException, TemplateException {
        Map<String, Object> dataModel = EnumDefinitionToDataModelMapper.map(mappingConfig, definition);
        generateFile(FreeMarkerTemplatesRegistry.enumTemplate, dataModel);
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

    private static File prepareOutputDir(String outputDir, MappingConfig mappingConfig) throws IOException {
        File targetDir;
        String javaPackage = mappingConfig.getJavaPackage();
        if (javaPackage == null || javaPackage.trim().isEmpty()) {
            targetDir = new File(outputDir);
        } else {
            targetDir = new File(outputDir, javaPackage.replace(".", File.separator));
        }
        Utils.deleteFolder(targetDir);
        boolean outputDirCreated = targetDir.mkdirs();
        if (!outputDirCreated) {
            throw new IOException("Unable to create output directory");
        }
        return targetDir;
    }

}
