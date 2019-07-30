package com.kobylynskyi.graphql.generator.mapper;

import com.kobylynskyi.graphql.generator.model.MappingConfig;
import graphql.language.ListType;
import graphql.language.NonNullType;
import graphql.language.Type;
import graphql.language.TypeName;

import java.util.Map;

/**
 * Map GraphQL type to Java type
 *
 * @author kobylynskyi
 */
public class TypeMapper {

    public static String mapToJavaType(Type type, MappingConfig mappingConfig) {
        if (type instanceof TypeName) {
            return mapToJavaType(((TypeName) type).getName(), mappingConfig);
        } else if (type instanceof ListType) {
            return wrapIntoJavaCollection(mapToJavaType(((ListType) type).getType(), mappingConfig));
        } else if (type instanceof NonNullType) {
            return mapToJavaType(((NonNullType) type).getType(), mappingConfig);
        }
        return null;
    }

    private static String wrapIntoJavaCollection(String type) {
        return String.format("java.util.Collection<%s>", type);
    }

    private static String mapToJavaType(String graphlType, MappingConfig mappingConfig) {
        Map<String, String> graphqlScalarsMapping = mappingConfig.getCustomTypesMapping();
        if (graphqlScalarsMapping.containsKey(graphlType)) {
            return graphqlScalarsMapping.get(graphlType);
        }
        switch (graphlType) {
            case "ID":
                return "String";
            case "Int":
                return "Integer";
            default: // String, Float, Boolean
                return graphlType;
        }
    }

}
