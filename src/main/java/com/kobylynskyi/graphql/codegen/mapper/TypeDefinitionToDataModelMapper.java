package com.kobylynskyi.graphql.codegen.mapper;

import com.kobylynskyi.graphql.codegen.model.MappingConfig;
import com.kobylynskyi.graphql.codegen.model.Parameter;
import com.kobylynskyi.graphql.codegen.utils.Utils;
import graphql.language.*;

import java.util.*;
import java.util.stream.Collectors;

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
     * @param document       Parent GraphQL document
     * @return Freemarker data model of the GraphQL type
     */
    public static Map<String, Object> map(MappingConfig mappingConfig, ObjectTypeDefinition typeDefinition,
                                          Document document) {
        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put(PACKAGE, MapperUtils.getJavaPackageLine(mappingConfig));
        dataModel.put(CLASS_NAME, Utils.capitalize(typeDefinition.getName()));
        dataModel.put(IMPLEMENTS, getUnionsHavingType(typeDefinition, document));

        List<Parameter> typeParameters = FieldDefinitionToParameterMapper.map(mappingConfig, typeDefinition.getFieldDefinitions());
        Set<Parameter> allParameters = new LinkedHashSet<>(typeParameters);
        List<InterfaceTypeDefinition> interfaces = getInterfacesOfType(mappingConfig, typeDefinition, document);
        interfaces.stream()
                .map(i -> FieldDefinitionToParameterMapper.map(mappingConfig, i.getFieldDefinitions()))
                .forEach(allParameters::addAll);
        dataModel.put(FIELDS, allParameters);

        // FIXME: consider:
        //   blame(
        //    path: String!
        //  ): Blame!

        return dataModel;
    }

    /**
     * Iterate through all unions and find all unions that given definition is part of
     *
     * @param definition GraphQL type definition
     * @param document   Parent GraphQL document
     * @return Unions names that given definition is part of
     */
    private static List<String> getUnionsHavingType(ObjectTypeDefinition definition,
                                                    Document document) {
        return document.getDefinitions().stream()
                .filter(def -> def instanceof UnionTypeDefinition)
                .map(def -> (UnionTypeDefinition) def)
                .filter(union -> isDefinitionPartOfUnion(definition, union))
                .map(UnionTypeDefinition::getName)
                .collect(Collectors.toList());
    }

    private static boolean isDefinitionPartOfUnion(ObjectTypeDefinition definition, UnionTypeDefinition union) {
        return union.getMemberTypes().stream()
                .filter(member -> member instanceof NamedNode)
                .map(member -> (NamedNode) member)
                .anyMatch(member -> member.getName().equals(definition.getName()));
    }

    /**
     * Scan document and return all interfaces that given type implements.
     *
     * @param mappingConfig Global mapping configuration
     * @param definition    GraphQL type definition
     * @param document      Parent GraphQL document
     * @return all interfaces that given type implements.
     */
    private static List<InterfaceTypeDefinition> getInterfacesOfType(MappingConfig mappingConfig,
                                                                     ObjectTypeDefinition definition,
                                                                     Document document) {
        if (definition.getImplements().isEmpty()) {
            return Collections.emptyList();
        }
        Set<String> typeImplements = definition.getImplements().stream()
                .map(type -> GraphqlTypeToJavaTypeMapper.mapToJavaType(mappingConfig, type))
                .collect(Collectors.toSet());
        return document.getDefinitions().stream()
                .filter(def -> def instanceof InterfaceTypeDefinition)
                .map(def -> (InterfaceTypeDefinition) def)
                .filter(def -> typeImplements.contains(def.getName()))
                .collect(Collectors.toList());
    }

}
