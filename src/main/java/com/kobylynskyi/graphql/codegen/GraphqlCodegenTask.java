package com.kobylynskyi.graphql.codegen;

import com.kobylynskyi.graphql.codegen.model.MappingConfig;
import lombok.Getter;
import lombok.Setter;
import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Gradle task for GraphQL code generation
 *
 * @author kobylynskyi
 */
@Getter
@Setter
public class GraphqlCodegenTask extends DefaultTask {

    private List<File> graphqlSchemas;
    private File outputDir;
    private Map<String, String> customTypesMapping = new HashMap<>();
    private String javaPackage;

    @TaskAction
    public void generate() {
        MappingConfig mappingConfig = new MappingConfig();
        mappingConfig.setJavaPackage(javaPackage);
        mappingConfig.setCustomTypesMapping(customTypesMapping);
        new GraphqlCodegen(graphqlSchemas, outputDir, mappingConfig).generate();
    }

}
