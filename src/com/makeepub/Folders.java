package com.makeepub;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

final class Folders extends CreateEpub {
    static String[] path = { "", "OEBPS", "META-INF", "OEBPS/Styles", "OEBPS/Text"};

    protected static void create() {
	File file;
	for (String string : path) {
	    file = new File(CreateEpub.tempDir + string);
	    file.mkdir();
	}
	System.out.println("[Created] folders");
    }

    protected static void delete() throws IOException {
	for (int i = path.length - 1; i >= 1; i--) {
	    Path pathPath = Paths.get(CreateEpub.tempDir + path[i]);
	    Files.deleteIfExists(pathPath);
	}
    }
}