package com.makeepub;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CreateEpub {

    protected static String tempDir;
    protected static String outputPath = "";
    protected static List<String> supportFiles = new ArrayList<String>();
    protected static List<String> allFiles = new ArrayList<String>();
    protected static String encoding = StandardCharsets.UTF_8.name();
    protected static Charset encodingCharset = StandardCharsets.UTF_8;
    protected static String title = "UNDEFINED_TITLE";
    protected static String author = "UNDEFINED_AUTHOR";
    protected static List<String> summary = new ArrayList<String>();
    protected static String url;
    protected static File epubFile;
    protected static String timeNow;
    protected static String bookID;

    public static void main(String[] args) {
	try {
	    url = "https://www.wuxiaworld.com/novel/sage-monarch";
	    timeNow = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
	    bookID = "XiaoNiang-" + timeNow + "-" + title + "-" + "-WuxiaWorld.com";
	    String currentTime = timeNow + '-' + LocalTime.now().format(DateTimeFormatter.ofPattern("HH-mm-ss"));
	    tempDir = "XiaoNiang-" + currentTime + "-WuxiaWorld.com"+File.separatorChar;
	    File epubDir = new File(tempDir);
	    Folders.create();
	    Mimetype.create();
	    Container_xml.create();
	    Stylesheet_css.create();
	    listFiles(epubDir, supportFiles);
	    Chapters.create();
	    listFiles(new File(CreateEpub.tempDir + "OEBPS/Text"), allFiles);
	    for (String filePath : supportFiles) {
		Files.deleteIfExists(Paths.get(filePath));
	    }
	    Folders.delete();
	} catch (IOException e) {
	    System.out.println("[!]FAILURE: Couldn't create required files");
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