package com.xiaoniang.epub;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

final class Mimetype {

	static String path = "mimetype";
	static String content = "application/epub+zip";
	
	static void create(EpubFile epubFile) {
	    try (PrintWriter writer = new PrintWriter(epubFile.outputPath()+path, epubFile.encoding())) {
	    writer.print(content);
	    } catch (FileNotFoundException | UnsupportedEncodingException e) {
		System.out.println("[!] Couldn't create mimetype");
	    }
	    epubFile.addToSupportFiles(0, epubFile.outputPath()+path);
	    System.out.println("[Created] mimetype");
	}
	
}
