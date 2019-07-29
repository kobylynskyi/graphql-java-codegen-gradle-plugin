package com.kobylynskyi.graphql.generator.model;

import graphql.language.ListType;
import graphql.language.NonNullType;
import graphql.language.Type;
import graphql.language.TypeName;

/**
 * Map GraphQL type to Java type
 *
 * @author kobylynskyi
 */
public final class TypeMapper {

    private TypeMapper() {
    }

    public static String getType(Type type) {
        if (type instanceof TypeName) {
            return mapToJavaType(((TypeName) type).getName());
        } else if (type instanceof ListType) {
            return "java.util.Collection<" + getType(((ListType) type).getType()) + ">";
        } else if (type instanceof NonNullType) {
            return getType(((NonNullType) type).getType());
        }
        return null;
    }

    private static String mapToJavaType(String graphlType) {
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
