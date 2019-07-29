package com.kobylynskyi.graphql.generator;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;
import graphql.language.Document;
import graphql.language.FieldDefinition;
import graphql.language.ObjectTypeDefinition;
import graphql.parser.Parser;
import lombok.Getter;
import lombok.Setter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.kobylynskyi.graphql.generator.model.DataModelFields;
import com.kobylynskyi.graphql.generator.model.FieldDefinitionToDataModelMapper;
import com.kobylynskyi.graphql.generator.utils.Utils;

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
class GraphQLSourcesGenerator {

    private static final Parser GRAPHQL_PARSER = new Parser();

    private static Template queryTemplate;

    GraphQLSourcesGenerator() throws IOException {
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_28);
        configuration.setClassLoaderForTemplateLoading(GraphQLSourcesGenerator.class.getClassLoader(), "");
        configuration.setDefaultEncoding("UTF-8");
        configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        configuration.setLogTemplateExceptions(false);
        configuration.setWrapUncheckedExceptions(true);

        queryTemplate = configuration.getTemplate("javaClassGraphqlQueryInterface.ftl");
    }

    private List<File> graphqlSchemas;
    private File outputDir;
    private String modelPackage;

    void generate() {
        prepareOutputDir();
        graphqlSchemas.stream().map(schemaFile -> GRAPHQL_PARSER.parseDocument(Utils.getFileContent(schemaFile)))
                .forEach(this::processDocument);
    }

    private void prepareOutputDir() {
        Utils.deleteFolder(outputDir);
        boolean outputDirCreated = outputDir.mkdirs();
        if (!outputDirCreated) {
            throw new CodeGenerationException("Unable to create output directory");
        }
    }

    private void processDocument(Document document) {
        document.getDefinitions().stream().filter(def -> def instanceof ObjectTypeDefinition)
                .map(typeDef -> (ObjectTypeDefinition) typeDef)
                .filter(typeDef -> Utils.isGraphqlOperation(typeDef.getName())).forEach(
                typeDef -> typeDef.getFieldDefinitions()
                        .forEach(fieldDef -> generateJavaSourceFile(fieldDef, typeDef.getName())));
    }

    private void generateJavaSourceFile(FieldDefinition fieldDefinition, String objectType) {
        Map<String, Object> dataModel = FieldDefinitionToDataModelMapper.map(fieldDefinition, modelPackage, objectType);

        File javaSourceFile = new File(outputDir, dataModel.get(DataModelFields.CLASS_NAME) + ".java");
        try {
            boolean fileCreated = javaSourceFile.createNewFile();
            if (!fileCreated) {
                throw new CodeGenerationException("Failed to create a file: " + javaSourceFile.getName());
            }
            queryTemplate.process(dataModel, new FileWriter(javaSourceFile));
        } catch (Exception e) {
            throw new CodeGenerationException(e);
        }
    }

}
