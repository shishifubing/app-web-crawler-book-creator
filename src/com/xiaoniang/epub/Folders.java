package com.xiaoniang.epub;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

final class Folders {
    static String[] paths = { "", "OEBPS", "META-INF", "OEBPS/Styles", "OEBPS/Text"};

    protected static void create(EpubFile epubFile) {
	File file;
	for (String string : paths) {
	    file = new File(epubFile.outputPath() + string);
	    file.mkdir();
	}
	System.out.println("[Created] folders");
    }

    protected static void delete(EpubFile epubFile) throws IOException {
	for (int i = paths.length - 1; i >= 1; i--) {
	    Path path = Paths.get(epubFile.outputPath() + paths[i]);
	    Files.deleteIfExists(path);
	}
    }
}