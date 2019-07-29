package com.kobylynskyi.graphql.generator;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.Collections;

import org.junit.jupiter.api.Test;

class GraphQLSourcesGeneratorTest {

    private final GraphQLSourcesGenerator generator = new GraphQLSourcesGenerator();

    GraphQLSourcesGeneratorTest() throws IOException {
    }

    @Test
    void addition() {
        File outputDir = new File("build/generated");
        generator.setOutputDir(outputDir);
        generator.setModelPackage("com.kobylynskyi.graphql.test1");
        generator.setGraphqlSchemas(Collections.singletonList(new File("test.graphqls")));

        generator.generate();

        File[] generatedFiles = outputDir.listFiles();
        assertEquals(4, generatedFiles.length);
        assertEquals("CreateEventMutation.java", generatedFiles[0].getName());
        assertEquals("EventByIdQuery.java", generatedFiles[1].getName());
        assertEquals("EventsByCategoryAndStatusQuery.java", generatedFiles[2].getName());
        assertEquals("VersionQuery.java", generatedFiles[3].getName());
    }

}