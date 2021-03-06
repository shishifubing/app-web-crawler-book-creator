package com.borinskikh.book_creator.main;

import com.borinskikh.book_creator.resources.Links;

import java.io.File;

public class CreateBook {

    public static void main(String[] args) {
        String path = "output" + File.separator;
        new File(path).mkdir();
        Links.fill();
        /*
         * for (String link : Links.links().keySet()) { Log.println("\n[Start]"); try {
         * new EpubBook(path, link).create(); } catch (Exception e) {
         * Log.println("[!] Couldn't create the Epub book");
         * e.printStackTrace(Log.stream()); } Log.println("[End]\n"); }
         */
    }
}
