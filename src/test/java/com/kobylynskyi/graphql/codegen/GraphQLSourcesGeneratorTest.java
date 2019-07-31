package com.kobylynskyi.graphql.codegen;

import com.kobylynskyi.graphql.codegen.model.MappingConfig;
import com.kobylynskyi.graphql.codegen.utils.Utils;
import org.gradle.internal.impldep.org.hamcrest.core.StringContains;
import org.gradle.internal.impldep.org.hamcrest.core.StringStartsWith;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toList;
import static org.gradle.internal.impldep.org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class GraphQLSourcesGeneratorTest {

    private GraphqlCodegen generator;

    private final File outputBuildDir = new File("build/generated");
    private final File outputJavaClassesDir = new File("build/generated/com/kobylynskyi/graphql/test1");
    private final MappingConfig mappingConfig = new MappingConfig();

    @BeforeEach
    void init() {
        mappingConfig.setJavaPackage("com.kobylynskyi.graphql.test1");
        generator = new GraphqlCodegen(Collections.singletonList("src/test/resources/test.graphqls"),
                outputBuildDir, mappingConfig);
    }

    @Test
    void generate_CheckFiles() {
        generator.generate();

        File[] files = Objects.requireNonNull(outputJavaClassesDir.listFiles());
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
    void generate_CustomMappings() throws FileNotFoundException {
        mappingConfig.setCustomTypesMapping(Collections.singletonMap("DateTime", "java.util.Date"));

        generator.generate();

        File[] files = Objects.requireNonNull(outputJavaClassesDir.listFiles());
        File eventFile = Arrays.stream(files)
                .filter(file -> file.getName().equalsIgnoreCase("Event.java"))
                .findFirst().orElseThrow(FileNotFoundException::new);

        assertThat(Utils.getFileContent(eventFile.getPath()), StringContains.containsString("java.util.Date createdDateTime;"));
    }

    @Test
    void generate_NoCustomMappings() throws FileNotFoundException {
        generator.generate();

        File[] files = Objects.requireNonNull(outputJavaClassesDir.listFiles());
        File eventFile = Arrays.stream(files)
                .filter(file -> file.getName().equalsIgnoreCase("Event.java"))
                .findFirst().orElseThrow(FileNotFoundException::new);

        assertThat(Utils.getFileContent(eventFile.getPath()), StringContains.containsString("String createdDateTime;"));
    }

    @Test
    void generate_NoPackage() throws FileNotFoundException {
        mappingConfig.setJavaPackage(null);
        generator.generate();

        File[] files = Objects.requireNonNull(outputBuildDir.listFiles());
        File eventFile = Arrays.stream(files)
                .filter(file -> file.getName().equalsIgnoreCase("Event.java"))
                .findFirst().orElseThrow(FileNotFoundException::new);

        assertThat(Utils.getFileContent(eventFile.getPath()), StringStartsWith.startsWith("\n" +
                "\n" +
                "public class Event"));
    }

}