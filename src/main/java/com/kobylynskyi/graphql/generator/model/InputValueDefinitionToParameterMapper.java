package com.kobylynskyi.graphql.generator.model;

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

    public static List<Parameter> map(List<InputValueDefinition> valueDefinitions) {
        if (valueDefinitions == null) {
            return Collections.emptyList();
        }
        return valueDefinitions.stream().map(InputValueDefinitionToParameterMapper::map).collect(Collectors.toList());
    }

    public static Parameter map(InputValueDefinition inputValueDefinition) {
        Parameter parameter = new Parameter();
        parameter.setName(inputValueDefinition.getName());
        parameter.setType(TypeMapper.getType(inputValueDefinition.getType()));
        return parameter;
    }

}
