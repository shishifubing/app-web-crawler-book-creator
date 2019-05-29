package com.xiaoniang.epub;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

final class Mimetype extends InnerFiles {

	Mimetype(EpubBook epubFile) {
	    innerPath = "mimetype";
	    addContent("application/epub+zip");
	}
	void create(EpubBook epubFile) {
	    try (PrintWriter writer = new PrintWriter(epubFile.getPath()+innerPath, epubFile.getEncoding())) {
	    writer.print(content);
	    } catch (FileNotFoundException | UnsupportedEncodingException e) {
		System.out.println("[!] Couldn't create mimetype");
	    }
	    epubFile.addToSupportFilesPaths(0, epubFile.getTempPath()+innerPath);
	    System.out.println("[Created] mimetype");
	}
	
}
