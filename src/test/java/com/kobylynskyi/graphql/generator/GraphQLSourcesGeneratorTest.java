package com.kobylynskyi.graphql.generator;

import com.kobylynskyi.graphql.generator.model.MappingConfig;
import com.kobylynskyi.graphql.generator.utils.Utils;
import org.gradle.internal.impldep.org.hamcrest.core.StringContains;
import org.gradle.internal.impldep.org.hamcrest.core.StringStartsWith;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.gradle.internal.impldep.org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class GraphQLSourcesGeneratorTest {

    private final GraphqlCodegen generator = new GraphqlCodegen();
    private final File outputDir = new File("build/generated");
    private final MappingConfig mappingConfig = new MappingConfig();

    @BeforeEach
    void init() {
        mappingConfig.setJavaPackage("com.kobylynskyi.graphql.test1");
        generator.setMappingConfig(mappingConfig);
        generator.setSchemas(Collections.singletonList(new File("test.graphqls")));
        generator.setOutputDir(outputDir);
    }

    @Test
    void generate_CheckFiles() {
        generator.generate();

        File[] files = outputDir.listFiles();
        List<String> generatedFileNames = Arrays.stream(files).map(File::getName).sorted().collect(toList());
        assertEquals(Arrays.asList(
                "CreateEventMutation.java",
                "Event.java",
                "EventByIdQuery.java",
                "EventProperty.java",
                "EventStatusEnum.java",
                "EventsByCategoryAndStatusQuery.java",
                "VersionQuery.java"),
                generatedFileNames);
    }

    @Test
    void generate_CustomMappings() {
        mappingConfig.setCustomTypesMapping(Collections.singletonMap("DateTime", "java.util.Date"));

        generator.generate();

        File eventFile = Arrays.stream(outputDir.listFiles())
                .filter(file -> file.getName().equalsIgnoreCase("Event.java"))
                .findFirst().get();

        assertThat(Utils.getFileContent(eventFile), StringContains.containsString("java.util.Date createdDateTime;"));
    }

    @Test
    void generate_NoCustomMappings() {
        generator.generate();

        File eventFile = Arrays.stream(outputDir.listFiles())
                .filter(file -> file.getName().equalsIgnoreCase("Event.java"))
                .findFirst().get();

        assertThat(Utils.getFileContent(eventFile), StringContains.containsString("String createdDateTime;"));
    }

    @Test
    void generate_NoPackage() {
        mappingConfig.setJavaPackage(null);
        generator.generate();

        File eventFile = Arrays.stream(outputDir.listFiles())
                .filter(file -> file.getName().equalsIgnoreCase("Event.java"))
                .findFirst().get();

        assertThat(Utils.getFileContent(eventFile), StringStartsWith.startsWith("\n" +
                "\n" +
                "public class Event"));
    }

}