package com.kobylynskyi.graphql.codegen;

import static java.util.stream.Collectors.toList;
import static org.gradle.internal.impldep.org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import freemarker.template.TemplateException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.gradle.internal.impldep.org.hamcrest.core.StringContains;
import org.gradle.internal.impldep.org.hamcrest.core.StringStartsWith;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.kobylynskyi.graphql.codegen.model.MappingConfig;
import com.kobylynskyi.graphql.codegen.utils.Utils;

class GraphQLSourcesGeneratorGitHubTest {

    private GraphqlCodegen generator;

    private final File outputJavaClassesDir = new File("build/generated/com/github/graphql");
    private final MappingConfig mappingConfig = new MappingConfig();

    @BeforeEach
    void init() {
        mappingConfig.setJavaPackage("com.github.graphql");
        generator = new GraphqlCodegen(Collections.singletonList("src/test/resources/github.graphqls"),
                "build/generated", mappingConfig);
    }

    @Test
    void generate_MultipleInterfacesPerType() throws IOException, TemplateException {
        generator.generate();

        File[] files = Objects.requireNonNull(outputJavaClassesDir.listFiles());
        File eventFile = Arrays.stream(files)
                .filter(file -> file.getName().equalsIgnoreCase("Commit.java"))
                .findFirst().orElseThrow(FileNotFoundException::new);

        assertThat(Utils.getFileContent(eventFile.getPath()), StringContains.containsString("String createdDateTime;"));
    }

}