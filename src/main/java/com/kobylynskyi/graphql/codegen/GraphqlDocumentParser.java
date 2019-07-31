package com.kobylynskyi.graphql.codegen;

import com.kobylynskyi.graphql.codegen.utils.Utils;
import graphql.language.Document;
import graphql.parser.Parser;

import java.io.File;

public class GraphqlDocumentParser {

    private static final Parser GRAPHQL_PARSER = new Parser();

    public static Document getDocument(String schemaFilePath) {
        String fileContent = Utils.getFileContent(schemaFilePath);
        return GRAPHQL_PARSER.parseDocument(fileContent);
    }


}
