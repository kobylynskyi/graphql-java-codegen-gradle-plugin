# GraphQL Codegen Gradle plugin #

[![Build Status](https://img.shields.io/maven-metadata/v/https/plugins.gradle.org/m2/com/kobylynskyi/graphql/codegen/graphql-codegen-gradle-plugin/maven-metadata.xml.svg?label=gradle)](https://plugins.gradle.org/plugin/com.kobylynskyi.graphql.codegen)
[![Build Status](https://travis-ci.com/kobylynskyi/graphql-java-codegen-gradle-plugin.svg?branch=master)](https://travis-ci.com/kobylynskyi/graphql-java-codegen-gradle-plugin)
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
        id "com.kobylynskyi.graphql.codegen" version "0.2-SNAPSHOT"
    }

Using [legacy plugin application](https://docs.gradle.org/current/userguide/plugins.html#sec:old_plugin_application):

    buildscript {
      repositories {
        maven {
          url "https://plugins.gradle.org/m2/"
        }
      }
      dependencies {
        classpath "com.kobylynskyi.graphql.codegen:graphql-codegen-gradle-plugin:0.2-SNAPSHOT"
      }
    }

### Plugin Configuration

    graphqlCodegen {
        graphqlSchemaPaths = [
            "$projectDir/src/main/resources/schema.graphqls".toString()
        ]
        outputDir = "$buildDir/generated/graphql"
        packageName = "com.example.graphql.model"
        customTypesMapping = [
            DateTime: "org.joda.time.DateTime"
        ]
    }
### Additional Configurations

* Automatically generate GraphQL code on project build:
   ```
   compileJava.dependsOn 'graphqlCodegen'
   ```
* Add generated sources to your project source sets:
   ```
   sourceSets {
       main {
           java {
               srcDir "$buildDir/generated/graphql"
           }
       }
   }
   ```


#### Plugin Options

| Key                     | Data Type          | Default value | Description |
| ----------------------- | ------------------ | ------------- | ----------- |
| graphqlSchemaPaths      | List(String)       | None          | GraphQL schema locations. You can supply multiple paths to GraphQL schemas. |
| packageName             | String             | Empty         | Java package for generated classes. |
| outputDir               | String             | None          | The output target directory into which code will be generated. |
| customTypesMapping      | Map(String,String) | Empty         | Map of (GraphQL Schema Type) to (Java Type). Can be used to supply custom mappings for scalars. |
| modelNamePrefix         | String             | Empty         | Sets the prefix for GraphQL model classes (type, input, interface, enum, union). |
| modelNameSuffix         | String             | Empty         | Sets the suffix for GraphQL model classes (type, input, interface, enum, union). |
| -                       | -                  | -             | -           |
| **TBD** apiPackage      | String             | Empty         |             |
| **TBD** modelPackage    | String             | Empty         |             |


### Example

[graphql-codegen-gradle-plugin-example](graphql-codegen-gradle-plugin-example)


### Inspired by
[swagger-codegen](https://github.com/swagger-api/swagger-codegen)

