package com.kobylynskyi.graphql.codegen.mapper;

import com.kobylynskyi.graphql.codegen.model.MappingConfig;
import com.kobylynskyi.graphql.codegen.model.Parameter;
import graphql.language.FieldDefinition;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper from GraphQL's FieldDefinition to a Freemarker-understandable format
 *
 * @author kobylynskyi
 */
public class FieldDefinitionToParameterMapper {

    public static List<Parameter> map(List<FieldDefinition> fieldDefinitions, MappingConfig mappingConfig) {
        if (fieldDefinitions == null) {
            return Collections.emptyList();
        }
        return fieldDefinitions.stream()
                .map(fieldDefinition -> map(fieldDefinition, mappingConfig))
                .collect(Collectors.toList());
    }

    private static Parameter map(FieldDefinition fieldDefinition,
                                 MappingConfig mappingConfig) {
        Parameter parameter = new Parameter();
        parameter.setName(fieldDefinition.getName());
        parameter.setType(TypeMapper.mapToJavaType(fieldDefinition.getType(), mappingConfig));
        return parameter;
    }

}
