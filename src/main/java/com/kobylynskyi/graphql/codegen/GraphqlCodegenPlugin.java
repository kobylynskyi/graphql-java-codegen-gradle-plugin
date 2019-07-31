package com.kobylynskyi.graphql.codegen;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

/**
 * Gradle plugin for GraphQL code generation
 *
 * @author kobylynskyi
 */
public class GraphqlCodegenPlugin implements Plugin<Project> {

    @Override
    public void apply(Project project) {
        project.getTasks().create("graphqlCodegenTask", GraphqlCodegenTask.class);
    }

}
