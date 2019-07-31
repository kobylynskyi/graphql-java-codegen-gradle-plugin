package com.kobylynskyi.graphql.codegen.utils;

import graphql.language.OperationDefinition;

import java.io.File;
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

    public static String getFileContent(String filePath) {
        try {
            return new String(Files.readAllBytes(Paths.get(filePath)));
        } catch (Exception e) {
            throw new RuntimeException("Unable to read the file: " + filePath, e);
        }
    }

    public static void deleteFolder(File folder) {
        File[] files = folder.listFiles();
        if (files != null) { //some JVMs return null for empty dirs
            for (File f : files) {
                if (f.isDirectory()) {
                    deleteFolder(f);
                } else {
                    f.delete();
                }
            }
        }
        folder.delete();
    }

}
