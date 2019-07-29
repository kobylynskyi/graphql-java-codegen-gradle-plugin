# GraphQL Code Generator #

[![Build Status](https://travis-ci.com/kobylynskyi/graphql-code-generator.svg?branch=master)](https://travis-ci.com/kobylynskyi/graphql-code-generator)

Make your GraphQL application schema-driven!

Inspired by [swagger-codegen](https://github.com/swagger-api/swagger-codegen).

This Gradle plugin is able to generate the following classes based on your GraphQL schema:
* Interfaces for each GraphQL query, mutation and subscription
* POJO classes for each GraphQL object type
* Enum classes for each GraphQL enum