package com.xiaoniang.epub;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class CreateEpub {

    public static void main(String[] args) {
	try {
	    EpubFile epubFile = new EpubFile("", "https://www.wuxiaworld.com/novel/sage-monarch");
	    epubFile.createInnerFiles();
	    epubFile.packInnerFiles();
	    epubFile.validate();
	    System.out.println("Epub book is created");
	} catch (IOException e) {
	    System.out.println("[!] Couldn't create the Epub book");
	}
    }

    static List<String> listFiles(File dir, List<String> epubFiles) throws IOException {
	File[] files = dir.listFiles();
	for (File file : files) {
	    if (file.isDirectory()) {
		listFiles(file, epubFiles);
	    } else {
		addFileToList(file, dir, epubFiles);
	    }
	}
	return epubFiles;
    }

    static void addFileToList (File file, File dir, List<String> epubFiles) {
	String name = file.getName();
	String filePath = file.getAbsolutePath();
	String dirPath = dir.getAbsolutePath();
	String epubPath = filePath.substring(dirPath.length()+1);
	if (epubPath.isEmpty() && name.contentEquals("mimetype")) {
	    epubFiles.add(0, file.getAbsolutePath());
	} else if (epubPath.startsWith("META-INF") && name.contentEquals("container.xml")){
	    epubFiles.add(file.getAbsolutePath());
	} else if (epubPath.startsWith("OEBPS") && (name.contentEquals("container.xml") ||
		name.contentEquals("content.opf") || name.contentEquals("toc.ncx") ||
		name.contentEquals("stylesheet.css"))) {
	    epubFiles.add(file.getAbsolutePath());
	}
    }
}