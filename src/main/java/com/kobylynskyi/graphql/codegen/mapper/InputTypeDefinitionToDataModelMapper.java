package com.kobylynskyi.graphql.codegen.mapper;

import com.kobylynskyi.graphql.codegen.model.MappingConfig;
import com.kobylynskyi.graphql.codegen.utils.Utils;
import graphql.language.InputObjectTypeDefinition;

import java.util.HashMap;
import java.util.Map;

import static com.kobylynskyi.graphql.codegen.model.DataModelFields.*;

/**
 * Map input type definition to a Freemarker data model
 *
 * @author kobylynskyi
 */
public class InputTypeDefinitionToDataModelMapper {

    /**
     * Map input type definition to a Freemarker data model
     *
     * @param typeDefinition GraphQL type definition
     * @param mappingConfig  Global mapping configuration
     * @return Freemarker data model of the GraphQL type
     */
    public static Map<String, Object> map(InputObjectTypeDefinition typeDefinition, MappingConfig mappingConfig) {
        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put(PACKAGE, MapperUtils.getJavaPackageLine(mappingConfig));
        dataModel.put(CLASS_NAME, Utils.capitalize(typeDefinition.getName()));
        dataModel.put(NAME, typeDefinition.getName());
        dataModel.put(FIELDS, InputValueDefinitionToParameterMapper.map(typeDefinition.getInputValueDefinitions(), mappingConfig));
        return dataModel;
    }

}
