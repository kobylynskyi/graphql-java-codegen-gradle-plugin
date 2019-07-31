package com.kobylynskyi.graphql.codegen.mapper;

import com.kobylynskyi.graphql.codegen.model.MappingConfig;

public class MapperUtils {

    public static String getJavaPackageLine(MappingConfig mappingConfig) {
        String javaPackage = mappingConfig.getJavaPackage();
        if (javaPackage == null || javaPackage.trim().isEmpty()) {
            return "";
        }
        return String.format("package %s;", javaPackage);
    }
}
