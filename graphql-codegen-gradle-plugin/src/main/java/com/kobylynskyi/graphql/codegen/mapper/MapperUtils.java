package com.kobylynskyi.graphql.codegen.mapper;

import com.kobylynskyi.graphql.codegen.utils.Utils;
import graphql.language.Document;
import graphql.language.NamedNode;
import graphql.language.UnionTypeDefinition;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class MapperUtils {

    private static final Set<String> JAVA_RESTRICTED_KEYWORDS = new HashSet<>(Arrays.asList(
            "abstract", "assert", "boolean", "break", "byte", "case", "catch", "char", "class", "const", "continue",
            "default", "do", "double", "else", "extends", "false", "final", "finally", "float", "for", "goto", "if",
            "implements", "import", "instanceof", "int", "interface", "long", "native", "new", "null", "package",
            "private", "protected", "public", "return", "short", "static", "strictfp", "super", "switch",
            "synchronized", "this", "throw", "throws", "transient", "true", "try", "void", "volatile", "while"));

    /**
     * Capitalize field name if it is Java-restricted.
     * Examples:
     * * class -> Class
     * * int -> Int
     *
     * @param fieldName any string
     * @return capitalized value if it is restricted in Java, same value as parameter otherwise
     */
    static String capitalizeIfRestricted(String fieldName) {
        if (JAVA_RESTRICTED_KEYWORDS.contains(fieldName)) {
            return Utils.capitalize(fieldName);
        }
        return fieldName;
    }

    /**
     * Iterate through all unions across the document and find all that given <code>definition</code> is part of.
     *
     * @param definition GraphQL NamedNode definition
     * @param document   Parent GraphQL document
     * @return Names of all unions that requested <code>definition</code> is part of.
     */
    static List<String> getUnionsHavingType(NamedNode definition,
                                            Document document) {
        return document.getDefinitions().stream()
                .filter(def -> def instanceof UnionTypeDefinition)
                .map(def -> (UnionTypeDefinition) def)
                .filter(union -> isDefinitionPartOfUnion(definition, union))
                .map(UnionTypeDefinition::getName)
                .collect(Collectors.toList());
    }

    /**
     * Find out if a definition is a part of a union.
     *
     * @param definition GraphQL definition (type / interface / object / union / etc.)
     * @param union      GraphQL Union definition
     * @return <b>true</b> if <code>definition</code> is a part of <code>union</code>. <b>false</b>if <code>definition</code> is a part of <code>union</code>.
     */
    private static boolean isDefinitionPartOfUnion(NamedNode definition,
                                                   UnionTypeDefinition union) {
        return union.getMemberTypes().stream()
                .filter(member -> member instanceof NamedNode)
                .map(member -> (NamedNode) member)
                .anyMatch(member -> member.getName().equals(definition.getName()));
    }
}
