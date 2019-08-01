package com.kobylynskyi.graphql.codegen;

import com.kobylynskyi.graphql.codegen.model.MappingConfig;
import com.kobylynskyi.graphql.codegen.utils.Utils;
import freemarker.template.TemplateException;
import org.gradle.internal.impldep.org.hamcrest.core.StringContains;
import org.gradle.internal.impldep.org.hamcrest.core.StringStartsWith;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toList;
import static org.gradle.internal.impldep.org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class GraphQLSourcesGeneratorTest {

    private GraphqlCodegen generator;

    private final String outputBuildDir = "build/generated";
    private final File outputJavaClassesDir = new File("build/generated/com/kobylynskyi/graphql/test1");
    private final MappingConfig mappingConfig = new MappingConfig();

    @BeforeEach
    void init() {
        mappingConfig.setJavaPackage("com.kobylynskyi.graphql.test1");
        generator = new GraphqlCodegen(Collections.singletonList("src/test/resources/schemas/test.graphqls"),
                outputBuildDir, mappingConfig);
    }

    @Test
    void generate_CheckFiles() throws IOException, TemplateException {
        generator.generate();

        File[] files = Objects.requireNonNull(outputJavaClassesDir.listFiles());
        List<String> generatedFileNames = Arrays.stream(files).map(File::getName).sorted().collect(toList());
        assertEquals(Arrays.asList("CreateEventMutation.java", "Event.java", "EventByIdQuery.java",
                "EventProperty.java", "EventStatus.java", "EventsByCategoryAndStatusQuery.java",
                "Mutation.java", "Query.java", "VersionQuery.java"),
                generatedFileNames);

        Arrays.stream(files).forEach(file -> {
            try {
                File expected = new File(String.format("src/test/resources/expected-classes/%s.txt", file.getName()));
                assertEquals(Utils.getFileContent(expected.getPath()), Utils.getFileContent(file.getPath()));
            } catch (IOException e) {
                fail(e);
            }
        });
    }

    @Test
    void generate_CustomMappings() throws IOException, TemplateException {
        mappingConfig.setCustomTypesMapping(Collections.singletonMap("DateTime", "java.util.Date"));

        generator.generate();

        File[] files = Objects.requireNonNull(outputJavaClassesDir.listFiles());
        File eventFile = Arrays.stream(files)
                .filter(file -> file.getName().equalsIgnoreCase("Event.java"))
                .findFirst().orElseThrow(FileNotFoundException::new);

        assertThat(Utils.getFileContent(eventFile.getPath()), StringContains.containsString("java.util.Date createdDateTime;"));
    }

    @Test
    void generate_NoCustomMappings() throws IOException, TemplateException {
        generator.generate();

        File[] files = Objects.requireNonNull(outputJavaClassesDir.listFiles());
        File eventFile = Arrays.stream(files)
                .filter(file -> file.getName().equalsIgnoreCase("Event.java"))
                .findFirst().orElseThrow(FileNotFoundException::new);

        assertThat(Utils.getFileContent(eventFile.getPath()), StringContains.containsString("String createdDateTime;"));
    }

    @Test
    void generate_NoPackage() throws IOException, TemplateException {
        mappingConfig.setJavaPackage(null);
        generator.generate();

        File[] files = Objects.requireNonNull(new File(outputBuildDir).listFiles());
        File eventFile = Arrays.stream(files)
                .filter(file -> file.getName().equalsIgnoreCase("Event.java"))
                .findFirst().orElseThrow(FileNotFoundException::new);

        assertThat(Utils.getFileContent(eventFile.getPath()), StringStartsWith.startsWith(
                System.lineSeparator() + System.lineSeparator() + "public class Event"));
    }

    @Test
    void generate_NoSchemas() throws IOException, TemplateException {
        generator.setSchemas(Collections.emptyList());
        generator.generate();

        File[] files = Objects.requireNonNull(outputJavaClassesDir.listFiles());
        assertEquals(0, files.length);
    }

    @Test
    void generate_WrongSchema() {
        generator.setSchemas(Collections.singletonList("wrong.graphqls"));

        Assertions.assertThrows(NoSuchFileException.class, () -> {
            generator.generate();
        });
    }

}