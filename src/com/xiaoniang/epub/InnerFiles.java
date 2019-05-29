package com.xiaoniang.epub;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

abstract class InnerFiles {
    protected final List<String> content = new ArrayList<String>();;
    protected String innerPath;
    protected void fill(EpubBook epubFile) {
	try (PrintWriter writer = new PrintWriter(epubFile.getPath()+innerPath, epubFile.getEncoding())) {
	    writer.print(content);
	    } catch (IOException e) {
		System.out.println("[!] Couldn't create "+ innerPath);
	    }
	    epubFile.addToSupportFilesPaths(epubFile.getTempPath()+innerPath);
	    System.out.println("[Created] mimetype");
    }
    protected void addContent(String ...line) {
	for (String string : line) {
	    content.add(string);
	}
    }
    protected void addContent(int index, String line) {
	content.add(index, line);
    }
    protected List<String> getContent() {
	return content;
    }
    protected String getContent(int index) {
	return content.get(index);
    }
}
