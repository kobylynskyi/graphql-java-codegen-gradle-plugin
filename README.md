# GraphQL Codegen Gradle plugin #

[![CircleCI](https://circleci.com/gh/kobylynskyi/graphql-java-codegen-gradle-plugin/tree/master.svg?style=svg)](https://circleci.com/gh/kobylynskyi/graphql-java-codegen-gradle-plugin/tree/master)
[![Gradle Plugins](https://img.shields.io/maven-metadata/v/https/plugins.gradle.org/m2/io/github/kobylynskyi/graphql/codegen/graphql-codegen-gradle-plugin/maven-metadata.xml.svg?label=gradle)](https://plugins.gradle.org/plugin/io.github.kobylynskyi.graphql.codegen)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

This document describes the gradle plugin for GraphQL Generator.

### Description

This Gradle plugin is able to generate the following classes based on your GraphQL schema:
* Interfaces for GraphQL queries, mutations and subscriptions
* Interfaces for GraphQL unions
* POJO classes for GraphQL types
* Enum classes for each GraphQL enum

### Plugin Setup

    plugins {
      id "io.github.kobylynskyi.graphql.codegen" version "1.2.1"
    }

Using [legacy plugin application](https://docs.gradle.org/current/userguide/plugins.html#sec:old_plugin_application):

    buildscript {
      repositories {
        maven {
          url "https://plugins.gradle.org/m2/"
        }
      }
      dependencies {
        classpath "io.github.kobylynskyi.graphql.codegen:graphql-codegen-gradle-plugin:1.2.1"
      }
    }
    
    apply plugin: "io.github.kobylynskyi.graphql.codegen"

### Plugin Configuration

#### build.gradle:

    graphqlCodegen {
        graphqlSchemaPaths = [
            "$projectDir/src/main/resources/schema.graphqls".toString()
        ]
        outputDir = "$buildDir/generated/graphql"
        packageName = "com.example.graphql.model"
        customTypesMapping = [
            DateTime: "org.joda.time.DateTime"
            Price.amount: "java.math.BigDecimal"
        ]
        customAnnotationsMapping = [
            DateTime: "com.fasterxml.jackson.databind.annotation.JsonDeserialize(using = com.example.json.EpochMillisScalarDeserializer.class"
        ]
        modelNameSuffix = "TO"
    }
    
    // Automatically generate GraphQL code on project build:
    compileJava.dependsOn 'graphqlCodegen'
    
    // Add generated sources to your project source sets:
    sourceSets.main.java.srcDir "$buildDir/generated"

#### build.gradle.kts:

    tasks.named<GraphqlCodegenGradleTask>("graphqlCodegen") {
        graphqlSchemaPaths = listOf("$projectDir/src/main/resources/graphql/schema.graphqls".toString())
        outputDir = File("$buildDir/generated/graphql")
        packageName = "com.example.graphql.model"
        customTypesMapping = mutableMapOf(Pair("EpochMillis", "java.time.LocalDateTime"))
        customAnnotationsMapping = mutableMapOf(Pair("EpochMillis", "com.fasterxml.jackson.databind.annotation.JsonDeserialize(using = com.example.json.EpochMillisScalarDeserializer.class"))
    }
    
    // Automatically generate GraphQL code on project build:
    sourceSets {
        getByName("main").java.srcDirs("$buildDir/generated/graphql")
    }
    
    // Add generated sources to your project source sets:
    val check: DefaultTask by tasks
    val graphqlCodegen: DefaultTask by tasks
    check.dependsOn(graphqlCodegen)    


#### Plugin Options

| Key                       | Data Type          | Default value                         | Description |
| ------------------------- | ------------------ | ------------------------------------- | ----------- |
| graphqlSchemaPaths        | List(String)       | None                                  | GraphQL schema locations. You can supply multiple paths to GraphQL schemas. |
| packageName               | String             | Empty                                 | Java package for generated classes. |
| outputDir                 | String             | None                                  | The output target directory into which code will be generated. |
| apiPackage                | String             | Empty                                 | Java package for generated api classes (Query, Mutation, Subscription). |
| modelPackage              | String             | Empty                                 | Java package for generated model classes (type, input, interface, enum, union). |
| generateApis              | Boolean            | True                                  | Java package for generated model classes (type, input, interface, enum, union). |
| customTypesMapping        | Map(String,String) | Empty                                 | Can be used to supply custom mappings for scalars. <br/> Supports:<br/> * Map of (GraphqlObjectName.fieldName) to (JavaType) <br/> * Map of (GraphqlType) to (JavaType) |
| customAnnotationsMapping  | Map(String,String) | Empty                                 | Can be used to supply custom annotations (serializers) for scalars. <br/> Supports:<br/> * Map of (GraphqlObjectName.fieldName) to (JavaType) <br/> * Map of (GraphqlType) to (JavaType) |
| modelValidationAnnotation | String             | @javax.validation.<br>constraints.NotNull | Annotation for mandatory (NonNull) fields. Can be null/empty. |
| modelNamePrefix           | String             | Empty                                 | Sets the prefix for GraphQL model classes (type, input, interface, enum, union). |
| modelNameSuffix           | String             | Empty                                 | Sets the suffix for GraphQL model classes (type, input, interface, enum, union). |


### Example

[graphql-codegen-gradle-plugin-example](graphql-codegen-gradle-plugin-example)


### Inspired by

[swagger-codegen](https://github.com/swagger-api/swagger-codegen)

### Convert Java classes to Kotlin classes

Navigate in IntelijIdea to the `./build/generated/graphql/` folder and press `Cmd+Alt+Shift+K`
Access to classes from your code as normal Kotlin classes.

