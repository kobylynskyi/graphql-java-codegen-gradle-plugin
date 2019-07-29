package com.kobylynskyi.graphql.generator;

import lombok.Getter;
import lombok.Setter;
import java.io.File;
import java.util.List;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;

/**
 * Gradle task for GraphQL code generation
 *
 * @author kobylynskyi
 */
@Getter
@Setter
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

}
