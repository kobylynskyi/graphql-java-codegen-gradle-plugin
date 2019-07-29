package com.kobylynskyi.graphql.generator;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GraphQLSourcesGeneratorTest {

    private final GraphQLSourcesGenerator generator = new GraphQLSourcesGenerator();

    GraphQLSourcesGeneratorTest() throws IOException {
    }

    @Test
    void addition() throws Exception {
        File outputDir = new File("build/generated");
        generator.setOutputDir(outputDir);
        generator.setModelPackage("com.kobylynskyi.graphql.test1");
        generator.setGraphqlSchemas(Collections.singletonList(new File("test.graphqls")));

        generator.generate();

        File[] generatedFiles = outputDir.listFiles();
        assertEquals(5, generatedFiles.length);
    }

}