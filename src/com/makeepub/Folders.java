package com.makeepub;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

final class Folders extends CreateEpub {
    static String[] path = { "", "OEBPS", "META-INF", "OEBPS/Styles", "OEBPS/Text", "OEBPS/Images" };

    protected static void create() {
	File file;
	for (String string : path) {
	    file = new File(CreateEpub.path + string);
	    file.mkdir();
	}
	System.out.println("[Created] Folders");
    }

    protected static void delete() throws IOException {
	for (int i = path.length - 1; i >= 1; i--) {
	    Path pathPath = Paths.get(CreateEpub.path + path[i]);
	    Files.deleteIfExists(pathPath);
	}
    }
}