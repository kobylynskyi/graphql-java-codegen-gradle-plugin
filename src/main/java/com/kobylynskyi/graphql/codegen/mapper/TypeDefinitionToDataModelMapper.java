package com.kobylynskyi.graphql.codegen.mapper;

import static com.kobylynskyi.graphql.codegen.model.DataModelFields.CLASS_NAME;
import static com.kobylynskyi.graphql.codegen.model.DataModelFields.FIELDS;
import static com.kobylynskyi.graphql.codegen.model.DataModelFields.PACKAGE;

import graphql.language.InterfaceTypeDefinition;
import graphql.language.ObjectTypeDefinition;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kobylynskyi.graphql.codegen.model.MappingConfig;
import com.kobylynskyi.graphql.codegen.utils.Utils;

/**
 * Map type definition to a Freemarker data model
 *
 * @author kobylynskyi
 */
public class TypeDefinitionToDataModelMapper {

    /**
     * Map type definition to a Freemarker data model
     *
     * @param mappingConfig  Global mapping configuration
     * @param typeDefinition GraphQL type definition
     * @return Freemarker data model of the GraphQL type
     */
    public static Map<String, Object> map(MappingConfig mappingConfig, ObjectTypeDefinition typeDefinition) {
        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put(PACKAGE, MapperUtils.getJavaPackageLine(mappingConfig));
        dataModel.put(CLASS_NAME, Utils.capitalize(typeDefinition.getName()));
        dataModel.put(FIELDS, FieldDefinitionToParameterMapper.map(mappingConfig, typeDefinition.getFieldDefinitions()));
        return dataModel;
    }


    /**
     * Map type definition to a Freemarker data model
     *
     * @param mappingConfig   Global mapping configuration
     * @param typeDefinition  GraphQL type definition
     * @param nodesImplements Interfaces which given type implements
     * @return Freemarker data model of the GraphQL type
     */
    public static Map<String, Object> map(MappingConfig mappingConfig, ObjectTypeDefinition typeDefinition,
            List<InterfaceTypeDefinition> nodesImplements) {
        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put(PACKAGE, MapperUtils.getJavaPackageLine(mappingConfig));
        dataModel.put(CLASS_NAME, Utils.capitalize(typeDefinition.getName()));
        dataModel.put(FIELDS, FieldDefinitionToParameterMapper.map(mappingConfig, typeDefinition.getFieldDefinitions()));
        // FIXME POPULATE FIELDS FROM INTERFACES
        return dataModel;
    }

}
