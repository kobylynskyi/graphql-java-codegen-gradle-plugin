package com.kobylynskyi.graphql.generator.mapper;

import com.kobylynskyi.graphql.generator.model.MappingConfig;
import com.kobylynskyi.graphql.generator.utils.Utils;
import graphql.language.ObjectTypeDefinition;

import java.util.HashMap;
import java.util.Map;

import static com.kobylynskyi.graphql.generator.model.DataModelFields.*;

/**
 * Map type definition to a Freemarker data model
 *
 * @author kobylynskyi
 */
public class TypeDefinitionToDataModelMapper {

    /**
     * Map type definition to a Freemarker data model
     *
     * @param typeDefinition GraphQL type definition
     * @param mappingConfig  Global mapping configuration
     * @return Freemarker data model of the GraphQL type
     */
    public static Map<String, Object> map(ObjectTypeDefinition typeDefinition, MappingConfig mappingConfig) {
        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put(PACKAGE, MapperUtils.getJavaPackageLine(mappingConfig));
        dataModel.put(CLASS_NAME, Utils.capitalize(typeDefinition.getName()));
        dataModel.put(NAME, typeDefinition.getName());
        dataModel.put(FIELDS, FieldDefinitionToParameterMapper.map(typeDefinition.getFieldDefinitions(), mappingConfig));
        return dataModel;
    }

}
