package com.kobylynskyi.graphql.codegen.mapper;

import com.kobylynskyi.graphql.codegen.model.MappingConfig;
import com.kobylynskyi.graphql.codegen.model.Parameter;
import graphql.language.InputValueDefinition;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper from GraphQL's InputValueDefinition to a Freemarker-understandable format
 *
 * @author kobylynskyi
 */
public class InputValueDefinitionToParameterMapper {

    public static List<Parameter> map(List<InputValueDefinition> valueDefinitions,
                                      MappingConfig mappingConfig) {
        if (valueDefinitions == null) {
            return Collections.emptyList();
        }
        return valueDefinitions.stream()
                .map(inputValueDefinition -> map(inputValueDefinition, mappingConfig))
                .collect(Collectors.toList());
    }

    public static Parameter map(InputValueDefinition inputValueDefinition,
                                MappingConfig mappingConfig) {
        Parameter parameter = new Parameter();
        parameter.setName(inputValueDefinition.getName());
        parameter.setType(TypeMapper.mapToJavaType(inputValueDefinition.getType(), mappingConfig));
        return parameter;
    }

}
