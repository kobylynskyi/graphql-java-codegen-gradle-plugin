package com.kobylynskyi.graphql.codegen.utils;

import graphql.language.OperationDefinition;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Various utilities
 *
 * @author kobylynskyi
 */
public final class Utils {

    private Utils() {
    }

    public static boolean isGraphqlOperation(String typeDef) {
        String typeDefNormalized = typeDef.toUpperCase();
        return typeDefNormalized.equals(OperationDefinition.Operation.QUERY.name()) ||
                typeDefNormalized.equals(OperationDefinition.Operation.MUTATION.name()) ||
                typeDefNormalized.equals(OperationDefinition.Operation.SUBSCRIPTION.name());
    }

    public static String capitalize(String name) {
        if (name != null && name.length() != 0) {
            char[] chars = name.toCharArray();
            chars[0] = Character.toUpperCase(chars[0]);
            return new String(chars);
        } else {
            return name;
        }
    }

    public static String getFileContent(String filePath) throws IOException {
        return new String(Files.readAllBytes(Paths.get(filePath)));
    }

    public static void deleteFolder(File folder) throws IOException {
        if (!folder.exists()) {
            return;
        }
        File[] files = folder.listFiles();
        if (files != null) { //some JVMs return null for empty dirs
            for (File subFile : files) {
                if (subFile.isDirectory()) {
                    deleteFolder(subFile);
                } else {
                    Files.delete(subFile.toPath());
                }
            }
        }
        Files.delete(folder.toPath());
    }

}
