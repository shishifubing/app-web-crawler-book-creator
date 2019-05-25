package com.makeepub;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.adobe.epubcheck.api.EpubCheck;

public class CreateEpub  {
    
    protected static String path;
    protected static List<String> epubFiles = new ArrayList<String>();
    protected static String encoding = StandardCharsets.UTF_8.name();
    protected static Charset encodingCharset = StandardCharsets.UTF_8;
    protected static String title;
    protected static String author = "Toy Car";
    protected static List<String> summary = new ArrayList<String>();
    protected static String url;
    protected static File epubFile;
    protected static String timeNow;
    protected static int lastChapterIndex = -1;
    protected static String bookID = "XiaoNiang-"+timeNow+"-"+title+"-"+"-WuxiaWorld.com";
    protected static long timeTakenMills;
    public static void main(String[] args) {
	try {
	    path = "output/";
	    url = "https://www.wuxiaworld.com/novel/trash-of-the-counts-family";
	    LocalDate date = LocalDate.now();
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	    timeNow = ""+date.format(formatter);
	    File epubDirFile = new File(path);
	    Folders.create();
	    Mimetype.create();
	    Container_xml.create();
	    Stylesheet_css.create();
	    Chapters.create();
	    Cover_xhtml.create();
	    Toc_ncx.create(lastChapterIndex);
	    Content_opf.create(lastChapterIndex);
	    try {
	        listFiles(epubFiles, epubDirFile);
	    } catch (IOException e1) {
	        System.out.println("[!] Issues with the creation of the list of files");
	    }
	    EpubFile.create();
	EpubCheck epubcheck = new EpubCheck(CreateEpub.epubFile);
	if (epubcheck.validate()) {
	    System.out.println("[EPUB FILE IS VALID]");
	} else {
	    System.out.println("[EPUB FILE HAS ERRORS]");
	}
	} catch (IOException e) {
	    System.out.println("[!]FAILURE: Couldn't create required files");
	}
	System.out.println("[Time to get the chapters] "+ timeTakenMills +" milliseconds");
	System.out.println("[Average time to get a chapter] "+ timeTakenMills/lastChapterIndex +" milliseconds");
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