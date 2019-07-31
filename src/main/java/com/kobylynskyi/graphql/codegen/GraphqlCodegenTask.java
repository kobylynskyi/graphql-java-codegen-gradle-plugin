package com.kobylynskyi.graphql.codegen;

import com.kobylynskyi.graphql.codegen.model.MappingConfig;
import freemarker.template.TemplateException;
import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.OutputDirectory;
import org.gradle.api.tasks.TaskAction;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Gradle task for GraphQL code generation
 *
 * @author kobylynskyi
 */
public class GraphqlCodegenTask extends DefaultTask {

    private List<String> graphqlSchemaPaths;
    private String outputDir;
    private Map<String, String> customTypesMapping;
    private String javaPackage;

    @TaskAction
    public void generate() throws IOException, TemplateException {
        MappingConfig mappingConfig = new MappingConfig();
        mappingConfig.setJavaPackage(javaPackage);
        mappingConfig.setCustomTypesMapping(customTypesMapping != null ? customTypesMapping : new HashMap<>());
        new GraphqlCodegen(graphqlSchemaPaths, outputDir, mappingConfig).generate();
    }

    public List<String> getGraphqlSchemaPaths() {
        return graphqlSchemaPaths;
    }

    public void setGraphqlSchemaPaths(List<String> graphqlSchemaPaths) {
        this.graphqlSchemaPaths = graphqlSchemaPaths;
    }

    @OutputDirectory
    public String getOutputDir() {
        return outputDir;
    }

    public void setOutputDir(String outputDir) {
        this.outputDir = outputDir;
    }

    public Map<String, String> getCustomTypesMapping() {
        return customTypesMapping;
    }

    public void setCustomTypesMapping(Map<String, String> customTypesMapping) {
        this.customTypesMapping = customTypesMapping;
    }

    public String getJavaPackage() {
        return javaPackage;
    }

    public void setJavaPackage(String javaPackage) {
        this.javaPackage = javaPackage;
    }

}
