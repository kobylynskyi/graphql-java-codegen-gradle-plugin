package com.kobylynskyi.graphql.codegen.mapper;

public enum DefinitionType {

    SCHEMA,
    OPERATION, // Query/Mutation/Subscription
    TYPE, // type/interface
    INPUT,
    UNION,
    ENUM,
    SCALAR;

}
