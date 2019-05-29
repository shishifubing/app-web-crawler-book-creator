package com.xiaoniang.epub;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
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
import java.util.zip.CRC32;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

class EpubFile {
    
    private File file; 
    public File file() {return file;}
    private Charset encodingCharset; 
    public Charset encodingCharset() {return encodingCharset;}
    private List<String> supportFiles = new ArrayList(); 
    public List<String> supportFiles() {return supportFiles;}
    public void addToSupportFiles(String absPath) {supportFiles.add(absPath);}
    public void addToSupportFiles(int index, String absPath) {supportFiles.add(index, absPath);}
    private List<String> chapterFiles = new ArrayList(); 
    public List<String> chapterFiles() {return chapterFiles;}
    public void addToChapterFiles(String absPath) {chapterFiles.add(absPath);}
    private List<String> summary; 
    public List<String> summary() {return summary;}
    private String tempDir; 
    public String tempDir() {return tempDir;}
    private String outputPath; 
    public String outputPath() {return outputPath;}
    private String encoding; 
    public String encoding() {return encoding;}
    private String title; 
    public String title() {return title;}
    private String author; 
    public String author() {return author;}
    private String url; 
    public String url() {return url;}
    private String dateOfCreation; 
    public String dateOfCreation() {return dateOfCreation;}
    private String timeOfCreation; 
    public String timeOfCreation() {return timeOfCreation;}
    private String bookID; 
    public String bookID() {return bookID;}
        
    EpubFile (String outputPath, String url) throws IOException {
	this.url = url;
	Document frontPage = null;
	while (frontPage == null) {
	    try {
		frontPage = Jsoup.connect(url).get();
	    } catch (IOException e) {
		System.out.println("[!] Cannot connect to the " + url);
	    }
	}
	Element title = frontPage.select("div.p-15 > *").first();
	this.title = title.text();
	file = new File(outputPath+title+".epub");
	encoding = StandardCharsets.UTF_8.name();
	encodingCharset = StandardCharsets.UTF_8;
	dateOfCreation = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
	timeOfCreation = LocalTime.now().format(DateTimeFormatter.ofPattern("HH-mm-ss"));
	bookID = "WuxiaWorld.com-" + "XiaoNiang-" + title+ "-" + dateOfCreation;
	tempDir = "WuxiaWorld.com-" + "XiaoNiang-" + dateOfCreation + '-' + timeOfCreation + File.separator;
	createInnerFiles(frontPage);
    }
    
    protected void createInnerFiles(Document frontPage) throws IOException {
	    Folders.create(this);
	    Mimetype.create(this);
	    Container_xml.create(this);
	    Stylesheet_css.create(this);
	    Chapters.create(frontPage, this);
    }
    
    protected void packInnerFiles(List<String> chapterEpubFiles) throws IOException {
	    FileOutputStream fos = new FileOutputStream(this.file);
	    BufferedOutputStream bos = new BufferedOutputStream(fos);
	    ZipOutputStream zos = new ZipOutputStream(bos, encodingCharset);
	    zos.setMethod(ZipOutputStream.DEFLATED);
	    for (String filePath : chapterEpubFiles) {
		String zenPath = filePath.substring(outputPath.length()+1).replaceAll("\\\\","/");
		ZipEntry zen = new ZipEntry(zenPath);
		File ipFile = new File(filePath);
		if (ipFile.getName().contentEquals("mimetype") ||
			ipFile.getName().endsWith(".jpg")) {
		    zen.setMethod(ZipEntry.STORED);
		    CRC32 crc = new CRC32();
		    crc.update(Files.readAllBytes(ipFile.toPath()));
		    zen.setCrc(crc.getValue());
		    zen.setSize(ipFile.length());
		    zen.setCompressedSize(ipFile.length());
		}
		zos.putNextEntry(zen);
		FileInputStream fis = new FileInputStream(ipFile);
		int len; byte[] buffer = new byte[(int) ipFile.length()];
		while ((len = fis.read(buffer)) >= 0) {
		    zos.write(buffer, 0, len);
		}
		System.out.println("[Zipped] " + zenPath);
		zos.closeEntry();
		fis.close();
		if (ipFile.getName().startsWith("chapter_")) {
		    Files.deleteIfExists(Paths.get(filePath));
		}
	    }
	    zos.close();
	    fos.close();
	    System.out.println("[Created Epub File] "+this.title());
    }
    void setTitle(String title) {
	this.title = title;
    }
}
