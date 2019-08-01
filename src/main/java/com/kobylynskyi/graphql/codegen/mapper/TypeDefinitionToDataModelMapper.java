package com.kobylynskyi.graphql.codegen.mapper;

import com.kobylynskyi.graphql.codegen.model.MappingConfig;
import com.kobylynskyi.graphql.codegen.model.Parameter;
import com.kobylynskyi.graphql.codegen.utils.Utils;
import graphql.language.InterfaceTypeDefinition;
import graphql.language.ObjectTypeDefinition;

import java.util.*;

import static com.kobylynskyi.graphql.codegen.model.DataModelFields.*;

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
     * @param interfaces     Interfaces which given type implements
     * @return Freemarker data model of the GraphQL type
     */
    public static Map<String, Object> map(MappingConfig mappingConfig, ObjectTypeDefinition typeDefinition,
                                          List<InterfaceTypeDefinition> interfaces) {
        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put(PACKAGE, MapperUtils.getJavaPackageLine(mappingConfig));
        dataModel.put(CLASS_NAME, Utils.capitalize(typeDefinition.getName()));
        Set<Parameter> parameters = new LinkedHashSet<>(FieldDefinitionToParameterMapper.map(mappingConfig, typeDefinition.getFieldDefinitions()));
        interfaces.stream()
                .map(i -> FieldDefinitionToParameterMapper.map(mappingConfig, i.getFieldDefinitions()))
                .forEach(parameters::addAll);
        dataModel.put(FIELDS, parameters);
        // FIXME: consider:
        //   blame(
        //    path: String!
        //  ): Blame!
        return dataModel;
    }

}
