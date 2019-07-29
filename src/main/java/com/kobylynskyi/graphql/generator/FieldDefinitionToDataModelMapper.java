package com.kobylynskyi.graphql.generator;

import graphql.language.FieldDefinition;
import graphql.language.ListType;
import graphql.language.Type;
import graphql.language.TypeName;

import java.util.HashMap;
import java.util.Map;

import static com.kobylynskyi.graphql.generator.DataModelFields.*;

public class FieldDefinitionToDataModelMapper {

    public static Map<String, Object> map(FieldDefinition fieldDefinition, String modelPackage) {
        Map<String, Object> dataModel = new HashMap<>();

        String className = getClassName(fieldDefinition.getName());
        if (modelPackage != null && !modelPackage.trim().isEmpty()) {
            dataModel.put(PACKAGE, "package " + modelPackage + ";");
        }
        dataModel.put(CLASS_NAME, className);
        dataModel.put(RETURN_TYPE, getType(fieldDefinition.getType()));
        dataModel.put(NAME, fieldDefinition.getName());
        dataModel.put(PARAMS, fieldDefinition.getInputValueDefinitions());

        return dataModel;
    }

    private static String getType(Type fieldDefinitionType) {
        if (fieldDefinitionType instanceof TypeName) {
            return ((TypeName) fieldDefinitionType).getName();
        } else if (fieldDefinitionType instanceof ListType) {
            return "java.util.Collection<" + getType(((ListType) fieldDefinitionType).getType()) + ">";
        }
        return null;
    }

    private static String getClassName(String queryDefinitionName) {
        return Utils.capitalize(queryDefinitionName) + "Query";
    }
}
