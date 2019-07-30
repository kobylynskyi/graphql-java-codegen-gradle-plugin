package com.kobylynskyi.graphql.generator;

import com.kobylynskyi.graphql.generator.utils.Utils;
import graphql.language.Document;
import graphql.parser.Parser;

import java.io.File;

public class GraphqlDocumentParser {

    private static final Parser GRAPHQL_PARSER = new Parser();

    public static Document getDocument(File schemaFile) {
        String fileContent = Utils.getResourceContent(schemaFile);
        return GRAPHQL_PARSER.parseDocument(fileContent);
    }


}
