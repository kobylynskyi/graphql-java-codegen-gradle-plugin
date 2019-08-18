package com.kobylynskyi.graphql.codegen.mapper;

import com.kobylynskyi.graphql.codegen.model.MappingConfig;
import com.kobylynskyi.graphql.codegen.utils.Utils;
import graphql.language.UnionTypeDefinition;

import java.util.HashMap;
import java.util.Map;

import static com.kobylynskyi.graphql.codegen.model.DataModelFields.CLASS_NAME;
import static com.kobylynskyi.graphql.codegen.model.DataModelFields.PACKAGE;

/**
 * Map union definition to a Freemarker data model
 *
 * @author kobylynskyi
 */
public class UnionDefinitionToDataModelMapper {

    /**
     * Map union definition to a Freemarker data model
     *
     * @param mappingConfig  Global mapping configuration
     * @param typeDefinition GraphQL union definition
     * @return Freemarker data model of the GraphQL union
     */
    public static Map<String, Object> map(MappingConfig mappingConfig, UnionTypeDefinition typeDefinition) {
        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put(PACKAGE, mappingConfig.getPackageName());
        dataModel.put(CLASS_NAME, Utils.capitalize(typeDefinition.getName()));
        return dataModel;
    }

}
