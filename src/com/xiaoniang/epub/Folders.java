package com.xiaoniang.epub;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

final class Folders {

    protected static void createIn(EpubBook epubFile) {
	File file;
	for (String innerPath : epubFile.getInnerFoldersPaths()) {
	    file = new File(epubFile.getPath() + innerPath);
	    file.mkdir();
	    System.out.println("[Created] " + epubFile.getPath() + innerPath);
	}
    }

    protected static void deleteIn(EpubBook epubFile) throws IOException {
	for (int i = epubFile.getInnerFoldersPaths().length - 1; i >= 1; i--) {
	    Files.deleteIfExists(Paths.get(epubFile.getPath() + epubFile.getInnerFoldersPath(i)));
	}
    }
}