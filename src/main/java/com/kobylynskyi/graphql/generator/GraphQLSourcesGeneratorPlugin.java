package com.kobylynskyi.graphql.generator;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class GraphQLSourcesGeneratorPlugin implements Plugin<Project> {

    @Override
    public void apply(Project project) {
        project.getTasks().create("generateGraphQLClassesTask", GraphQLSourcesGeneratorTask.class);
    }

}
