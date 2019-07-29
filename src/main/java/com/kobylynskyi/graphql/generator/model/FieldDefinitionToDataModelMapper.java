package com.kobylynskyi.graphql.generator.model;

import static com.kobylynskyi.graphql.generator.model.DataModelFields.CLASS_NAME;
import static com.kobylynskyi.graphql.generator.model.DataModelFields.NAME;
import static com.kobylynskyi.graphql.generator.model.DataModelFields.PACKAGE;
import static com.kobylynskyi.graphql.generator.model.DataModelFields.PARAMS;
import static com.kobylynskyi.graphql.generator.model.DataModelFields.RETURN_TYPE;
import static com.kobylynskyi.graphql.generator.model.TypeMapper.getType;

import graphql.language.FieldDefinition;
import java.util.HashMap;
import java.util.Map;

import com.kobylynskyi.graphql.generator.utils.Utils;

/**
 * Map field definition to a Freemarker data model
 *
 * @author kobylynskyi
 */
public class FieldDefinitionToDataModelMapper {

    /**
     * Map field definition to a Freemarker data model
     *
     * @param fieldDefinition GraphQL field definition
     * @param javaPackage     Java package (e.g.: "com.kobylynskyi.example")
     * @param objectType      Object type (e.g.: "Query", "Mutation" or "Subscription")
     * @return Freemarker data model of the GraphQL field
     */
    public static Map<String, Object> map(FieldDefinition fieldDefinition, String javaPackage, String objectType) {
        Map<String, Object> dataModel = new HashMap<>();

        String className = getClassName(fieldDefinition.getName(), objectType);
        if (javaPackage != null && !javaPackage.trim().isEmpty()) {
            dataModel.put(PACKAGE, "package " + javaPackage + ";");
        }
        dataModel.put(CLASS_NAME, className);
        dataModel.put(RETURN_TYPE, getType(fieldDefinition.getType()));
        dataModel.put(NAME, fieldDefinition.getName());
        dataModel.put(PARAMS, InputValueDefinitionToParameterMapper.map(fieldDefinition.getInputValueDefinitions()));

        return dataModel;
    }

    /**
     * Examples:
     * - VersionQuery
     * - EventsByCategoryQuery
     */
    private static String getClassName(String queryDefinitionName, String objectType) {
        return Utils.capitalize(queryDefinitionName) + objectType;
    }
}
