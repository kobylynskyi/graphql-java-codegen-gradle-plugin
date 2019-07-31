package com.kobylynskyi.graphql.codegen.mapper;

public enum DefinitionType {

    SCHEMA,
    OPERATION, // Query/Mutation/Subscription
    TYPE,
    INTERFACE,
    INPUT,
    UNION,
    ENUM,
    SCALAR;

}
