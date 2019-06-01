package com.xiaoniang.epub.api;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public abstract class InnerFiles {
    private final List<String> content = new ArrayList<String>();
    private String innerPath;
    private File file;
    private EpubBook epubBook;
    private int volume = -1;
    
    public boolean fill() {
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
	} else if (!epubBook.supportFiles().contains(file)){
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
    public List<String> content() {
	return content;
    }
    public String content(int index) {
	return content.get(index);
    }
    public void setInnerPath(String string) {
	innerPath = string;
    }
    public String innerPath() {
	return innerPath;
    }
    public void setFile(File file) {
	this.file = file;
    }
    public File file() {
	return file;
    }
    public void setEpubBook(EpubBook book) {
	epubBook = book;
    }
    public EpubBook epubBook() {
	return epubBook;
    }
    public void setVolume(int index) {
	volume = index;
    }
    public int volume() {
	return volume;
    }
}
