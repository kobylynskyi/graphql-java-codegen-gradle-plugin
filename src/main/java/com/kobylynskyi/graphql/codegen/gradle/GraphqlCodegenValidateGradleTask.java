package com.kobylynskyi.graphql.codegen.gradle;

import com.kobylynskyi.graphql.codegen.GraphqlCodegenValidate;
import lombok.Getter;
import lombok.Setter;
import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;

import java.io.IOException;
import java.util.List;

/**
 * Gradle task for GraphQL code generation
 *
 * @author kobylynskyi
 */
@Getter
@Setter
public class GraphqlCodegenValidateGradleTask extends DefaultTask {

    private List<String> graphqlSchemaPaths;

    @TaskAction
    public void validate() throws IOException {
        new GraphqlCodegenValidate(graphqlSchemaPaths).validate();
    }

}
