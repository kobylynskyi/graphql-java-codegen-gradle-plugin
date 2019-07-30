package com.kobylynskyi.graphql.generator;

import com.kobylynskyi.graphql.generator.model.MappingConfig;
import lombok.Getter;
import lombok.Setter;
import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;

import java.io.File;
import java.util.List;

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
    private MappingConfig mappingConfig;

    @TaskAction
    public void generate() {
        new GraphqlCodegen(graphqlSchemas, outputDir, mappingConfig).generate();
    }

}
