package com.kobylynskyi.graphql.generator;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

public final class Utils {

    private Utils() {
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

    public static String getFileContent(File file) throws IOException, URISyntaxException {
        URL resource = Utils.class.getClassLoader().getResource(file.getPath());
        return new String(Files.readAllBytes(Paths.get(resource.toURI())));
    }

}
