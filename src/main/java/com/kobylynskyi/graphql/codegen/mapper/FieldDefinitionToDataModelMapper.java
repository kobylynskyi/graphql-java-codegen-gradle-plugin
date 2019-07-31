package com.kobylynskyi.graphql.codegen.mapper;

import com.kobylynskyi.graphql.codegen.model.MappingConfig;
import com.kobylynskyi.graphql.codegen.utils.Utils;
import graphql.language.FieldDefinition;

import java.util.HashMap;
import java.util.Map;

import static com.kobylynskyi.graphql.codegen.model.DataModelFields.*;

/**
 * Map field definition to a Freemarker data model
 *
 * @author kobylynskyi
 */
public class FieldDefinitionToDataModelMapper {

    /**
     * Map field definition to a Freemarker data model
     *
     * @param mappingConfig   Global mapping configuration
     * @param fieldDefinition GraphQL field definition
     * @param objectType      Object type (e.g.: "Query", "Mutation" or "Subscription")
     * @return Freemarker data model of the GraphQL field
     */
    public static Map<String, Object> map(MappingConfig mappingConfig, FieldDefinition fieldDefinition, String objectType) {
        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put(PACKAGE, MapperUtils.getJavaPackageLine(mappingConfig));
        dataModel.put(CLASS_NAME, getClassName(fieldDefinition.getName(), objectType));
        dataModel.put(TYPE, TypeMapper.mapToJavaType(mappingConfig, fieldDefinition.getType()));
        dataModel.put(NAME, fieldDefinition.getName());
        dataModel.put(FIELDS, InputValueDefinitionToParameterMapper.map(mappingConfig, fieldDefinition.getInputValueDefinitions()));
        return dataModel;
    }

    /**
     * Examples:
     * - VersionQuery
     * - EventsByCategoryQuery
     * - CreateEventMutation
     */
    private static String getClassName(String queryDefinitionName, String objectType) {
        return Utils.capitalize(queryDefinitionName) + objectType;
    }
}
