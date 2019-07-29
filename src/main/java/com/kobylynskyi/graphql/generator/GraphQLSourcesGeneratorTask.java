package com.kobylynskyi.graphql.generator;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;

import java.io.File;
import java.util.List;

public class GraphQLSourcesGeneratorTask extends DefaultTask {

    private List<File> graphqlSchemas;
    private File outputDir;
    private String modelPackage;

    @TaskAction
    public void generate() throws Exception {
        GraphQLSourcesGenerator graphQLSourcesGenerator = new GraphQLSourcesGenerator();
        graphQLSourcesGenerator.setGraphqlSchemas(graphqlSchemas);
        graphQLSourcesGenerator.setOutputDir(outputDir);
        graphQLSourcesGenerator.setModelPackage(modelPackage);
        graphQLSourcesGenerator.generate();
    }

    public List<File> getGraphqlSchemas() {
        return graphqlSchemas;
    }

    public void setGraphqlSchemas(List<File> graphqlSchemas) {
        this.graphqlSchemas = graphqlSchemas;
    }

    public File getOutputDir() {
        return outputDir;
    }

    public void setOutputDir(File outputDir) {
        this.outputDir = outputDir;
    }
}
