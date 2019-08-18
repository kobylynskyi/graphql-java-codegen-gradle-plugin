package com.kobylynskyi.graphql.codegen;

import com.kobylynskyi.graphql.codegen.model.MappingConfig;
import com.kobylynskyi.graphql.codegen.utils.Utils;
import freemarker.template.TemplateException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GraphqlCodegenGitHubTest {

    private GraphqlCodegen generator;

    private final File outputJavaClassesDir = new File("build/generated/com/github/graphql");
    private final MappingConfig mappingConfig = new MappingConfig();

    @BeforeEach
    void init() {
        mappingConfig.setPackageName("com.github.graphql");
        generator = new GraphqlCodegen(Collections.singletonList("src/test/resources/schemas/github.graphqls"),
                "build/generated", mappingConfig);
    }

    //@AfterEach
    void cleanup() throws IOException {
        Utils.deleteFolder(new File("build/generated"));
    }

    @Test
    void generate_MultipleInterfacesPerType() throws IOException, TemplateException {
        generator.generate();

        File[] files = Objects.requireNonNull(outputJavaClassesDir.listFiles());
        File commitFile = Arrays.stream(files).filter(file -> file.getName().equalsIgnoreCase("Commit.java"))
                .findFirst().orElseThrow(FileNotFoundException::new);
        assertEquals(Utils.getFileContent(new File("src/test/resources/expected-classes/Commit.java.txt").getPath()),
                Utils.getFileContent(commitFile.getPath()));


        File profileOwner = Arrays.stream(files).filter(file -> file.getName().equalsIgnoreCase("ProfileOwner.java"))
                .findFirst().orElseThrow(FileNotFoundException::new);
        assertEquals(Utils.getFileContent(new File("src/test/resources/expected-classes/ProfileOwner.java.txt").getPath()),
                Utils.getFileContent(profileOwner.getPath()));
    }

}