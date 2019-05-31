package com.xiaoniang.epub;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

final class Folders {

    protected static void createFor(EpubBook epubBook) {
	File file;
	for (String innerPath : epubBook.innerFoldersPaths()) {
	    file = new File(epubBook.tempPath() + innerPath);
	    if (file.mkdir()) {
		System.out.println("[Created Folder] " + innerPath);
	    }
	}
    }

    protected static void deleteIn(EpubBook epubBook) throws IOException {
	for (int i = epubBook.innerFoldersPaths().length - 1; i >= 1; i--) {
	    Files.deleteIfExists(Paths.get(epubBook.path() + epubBook.innerFolderPath(i)));
	}
    }
}