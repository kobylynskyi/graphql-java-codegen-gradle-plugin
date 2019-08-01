package com.kobylynskyi.graphql.codegen;

import com.kobylynskyi.graphql.codegen.model.MappingConfig;
import freemarker.template.TemplateException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;

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

        //assertThat(Utils.getFileContent(eventFile.getPath()), StringContains.containsString("String createdDateTime;"));
    }

}