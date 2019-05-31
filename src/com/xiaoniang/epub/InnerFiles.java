package com.xiaoniang.epub;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

abstract class InnerFiles {
    private final List<String> content = new ArrayList<String>();
    private String innerPath;
    private File file;
    private EpubBook epubBook;
    private int volume = -1;
    
    protected boolean fill() {
	try (PrintWriter writer = new PrintWriter(file, epubBook.encoding())) {
	    for (String string : content) {
		writer.print(string);
	    }
	} catch (IOException e) {
		System.out.println("[!] Couldn't create "+ innerPath);
		return false;
	}
	if (volume!=-1) {
	    epubBook.addToChapterFiles(volume, file);
	} else if (file.getName().contentEquals("mimetype")) {
	    epubBook.addToSupportFiles(0, file);
	} else {
	    epubBook.addToSupportFiles(file);
	}
	System.out.println("[Created] "+innerPath);
	return true;
    }
    
    protected static String escapeHtml(String input) {
	return input.replaceAll("&", "&amp;").replaceAll("'", "&apos;").replaceAll("<", "&lt;").replaceAll(">", "&gt;");
    }
    
    protected void addContent(String ...line) {
	for (String string : line) {
	    content.add(string);
	}
    }
    protected void addContent(int index, String line) {
	content.add(index, line);
    }
    protected List<String> content() {
	return content;
    }
    protected String content(int index) {
	return content.get(index);
    }
    protected void setInnerPath(String string) {
	innerPath = string;
    }
    protected String innerPath() {
	return innerPath;
    }
    protected void setFile(File file) {
	this.file = file;
    }
    protected File file() {
	return file;
    }
    protected void setEpubBook(EpubBook book) {
	epubBook = book;
    }
    protected EpubBook epubBook() {
	return epubBook;
    }
    protected void setVolume(int index) {
	volume = index;
    }
    protected int volume() {
	return volume;
    }
}
