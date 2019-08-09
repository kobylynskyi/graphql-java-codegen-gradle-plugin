package com.kobylynskyi.graphql.codegen.model;

import graphql.language.Definition;

/**
 * Generic exception that indicates some code generation error
 *
 * @author kobylynskyi
 */
public class UnsupportedGraphqlDefinitionException extends RuntimeException {

    public UnsupportedGraphqlDefinitionException(Definition unsupported) {
        super("Unsupported GraphQL definition type: " + unsupported.getClass());
    }

}
