package com.xiaoniang.epub;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

final class Mimetype {

	static String innerPath = "mimetype";
	static String content = "application/epub+zip";
	
	static void create(EpubFile epubFile) {
	    try (PrintWriter writer = new PrintWriter(epubFile.getPath()+innerPath, epubFile.getEncoding())) {
	    writer.print(content);
	    } catch (FileNotFoundException | UnsupportedEncodingException e) {
		System.out.println("[!] Couldn't create mimetype");
	    }
	    epubFile.addToSupportFilesPaths(0, epubFile.getTempPath()+innerPath);
	    System.out.println("[Created] mimetype");
	}
	
}
