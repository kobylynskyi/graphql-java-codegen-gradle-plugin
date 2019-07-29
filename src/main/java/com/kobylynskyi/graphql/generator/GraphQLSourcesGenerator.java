package com.kobylynskyi.graphql.generator;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import graphql.language.Document;
import graphql.language.FieldDefinition;
import graphql.language.ObjectTypeDefinition;
import graphql.language.OperationDefinition;
import graphql.parser.Parser;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

class GraphQLSourcesGenerator {

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

    void generate() throws Exception {
        List<Document> documents = parseDocuments(graphqlSchemas);

        if (!documents.isEmpty()) {
            outputDir.mkdirs();
        }

        for (Document document : documents) {
            document.getDefinitions().stream()
                    .filter(def -> def instanceof ObjectTypeDefinition)
                    .map(typeDef -> (ObjectTypeDefinition) typeDef)
                    .filter(typeDef -> typeDef.getName().equalsIgnoreCase(OperationDefinition.Operation.QUERY.name()))
                    .map(ObjectTypeDefinition::getFieldDefinitions)
                    .flatMap(List::stream)
                    .forEach(typeDef -> {
                        try {
                            generateJavaSourceFile(typeDef, modelPackage, outputDir);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    });
        }
    }

    private void generateJavaSourceFile(FieldDefinition fieldDefinition, String modelPackage, File outputDir) throws IOException, TemplateException {
        Map<String, Object> dataModel = FieldDefinitionToDataModelMapper.map(fieldDefinition, modelPackage);

        File javaSourceFile = new File(outputDir, dataModel.get(DataModelFields.CLASS_NAME) + ".java");
        javaSourceFile.createNewFile();
        Writer javaSourceFileWriter = new FileWriter(javaSourceFile);

        queryTemplate.process(dataModel, javaSourceFileWriter);
    }

    private static List<Document> parseDocuments(List<File> graphqlSchemaFile) throws IOException, URISyntaxException {
        Parser parser = new Parser();
        List<Document> documents = new ArrayList<>();
        for (File file : graphqlSchemaFile) {
            documents.add(parser.parseDocument(Utils.getFileContent(file)));
        }
        return documents;
    }

    public void setGraphqlSchemas(List<File> graphqlSchemas) {
        this.graphqlSchemas = graphqlSchemas;
    }

    public void setOutputDir(File outputDir) {
        this.outputDir = outputDir;
    }

    public void setModelPackage(String modelPackage) {
        this.modelPackage = modelPackage;
    }
}
