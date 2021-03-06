package com.borinskikh.book_creator.resources;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class Links {
    private static final Map<String, String> linkPairs = new HashMap<>();

    public static String link(String key) {
        return linkPairs.get(key);
    }

    public static Map<String, String> links() {
        return linkPairs;
    }

    public static void fill() {
        try {
            Files.lines(Paths.get("src/com/xiaoniang/epub/resources/linkPairs.txt")).forEach((String line) -> {
                String[] string = line.split(",");
                linkPairs.put(string[0], string[1]);
            });
        } catch (IOException e) {
            e.printStackTrace(Log.stream());
        }
    }
}
