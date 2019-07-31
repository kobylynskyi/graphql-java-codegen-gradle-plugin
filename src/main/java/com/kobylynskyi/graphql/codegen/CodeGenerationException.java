package com.kobylynskyi.graphql.codegen;

/**
 * Generic exception that indicates some code generation error
 *
 * @author kobylynskyi
 */
public class CodeGenerationException extends RuntimeException {

    public CodeGenerationException(String message) {
        super(message);
    }

    public CodeGenerationException(Exception e) {
        super(e);
    }
}
