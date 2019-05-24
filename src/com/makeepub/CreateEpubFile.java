package com.makeepub;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.adobe.epubcheck.api.EpubCheck;

public class CreateEpubFile {

    @SuppressWarnings("unused")
    public static void main(String[] args) {
	String epubDir = "output/";
	File epubDirFile = new File(epubDir);
	Folders folders = new Folders(epubDir);
	Mimetype mimetype = new Mimetype(epubDir);
	Container_xml container = new Container_xml(epubDir);
	Cover_xhtml cover = new Cover_xhtml(epubDir);
	Stylesheet_css stylesheet = new Stylesheet_css(epubDir);
	String url = "https://www.wuxiaworld.com/novel/martial-god-asura/mga-chapter-1";
	int lastChapterIndex = 5;
	Chapters chapters = new Chapters(url, lastChapterIndex);
	Toc_ncx toc = new Toc_ncx(epubDir, lastChapterIndex);
	Content_opf content = new Content_opf(epubDir, lastChapterIndex);
	List<String> epubFiles = new ArrayList<String>();
	try {
	    listFiles(epubFiles, epubDirFile);
	} catch (IOException e1) {
	    System.out.println("[!] Issues with the creation of the list of files");
	}
	EpubFile epubFile = new EpubFile(epubDir, epubFiles);
	EpubCheck epubcheck = new EpubCheck(epubFile.getFile());
	if (epubcheck.validate()) {
	    System.out.println("[EPUB FILE IS VALID]");
	} else {
	    System.out.println("[EPUB FILE HAS ERRORS]");
	}
    }

    static List<String> listFiles(List<String> epubFiles, File dir) throws IOException {
	File[] files = dir.listFiles();
	for (File file : files) {
	    if (file.getName().contentEquals("mimetype"))
		epubFiles.add(0, file.getAbsolutePath());
	    else if (file.isDirectory()) {
		listFiles(epubFiles, file);
	    } else {
		epubFiles.add(file.getAbsolutePath());
	    }
	}
	return epubFiles;
    }
}