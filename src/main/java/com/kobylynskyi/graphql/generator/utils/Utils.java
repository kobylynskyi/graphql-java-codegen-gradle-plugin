package com.kobylynskyi.graphql.generator.utils;

import graphql.language.OperationDefinition;
import java.io.File;
import java.net.URL;
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

    public static String getResourceContent(File file) {
        try {
            URL resource = Utils.class.getClassLoader().getResource(file.getPath());
            return new String(Files.readAllBytes(Paths.get(resource.toURI())));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String getFileContent(File file) {
        try {
            return new String(Files.readAllBytes(Paths.get(file.toURI())));
        } catch (Exception e) {
            throw new RuntimeException(e);
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
