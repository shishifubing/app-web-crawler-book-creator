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
import org.jsoup.select.Elements;

class EpubFile {
    
    private final File file;  
    private final List<String> supportFilesPaths; 
    private final List<String> chapterFilesPaths; 
    private final List<String> summary; 
    private final String[] innerFoldersPaths = { "OEBPS"+File.separator, "META-INF"+File.separator, "OEBPS"+File.separator+"Styles"+File.separator, "OEBPS"+File.separator+"Text"+File.separator};
    private final String url;
    private final Document frontPage;
    private final String path;
    private final String tempPath;
    private final String encoding;
    private final Charset encodingCharset;
    private final String title;
    private final String author;
    private final String bookID;
    private final String timeOfCreation;
    private final String dateOfCreation;
        
    EpubFile (String outputPath, String link) throws IOException {
	url = link;
	file = new File(outputPath);
	path = file.getAbsolutePath();
	supportFilesPaths = new ArrayList<String>();
	chapterFilesPaths = new ArrayList<String>();
	summary = new ArrayList<String>();
	Document document = null;
	while (document == null) {
	    try {
		document = Jsoup.connect(link).get();
	    } catch (IOException e) {
		System.out.println("[!] Cannot connect to the " + link);
	    }
	}
	frontPage = document;
	Element titleElement = frontPage.select("div.p-15 > *").first();
	title = titleElement.text();
	Elements summary = frontPage.select("div.p-15 > div.fr-view > *");
	for (Element paragraph : summary) {
	    String text = paragraph.text();
	    if ((!getSummary().equals(null) && !getSummary().isEmpty())
		    && (text.equals(null) || text.isEmpty())) {
		break;
	    }
	    addToSummary(text);
	}
	dateOfCreation = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
	timeOfCreation = LocalTime.now().format(DateTimeFormatter.ofPattern("HH-mm-ss"));
	bookID = "WuxiaWorld.com-" + "XiaoNiang-" + title+ "-" + dateOfCreation;
	File tempDir = new File ("WuxiaWorld.com-" + "XiaoNiang-" + dateOfCreation + '-' + timeOfCreation + File.separator);
	tempDir.mkdir();
	tempPath = tempDir.getAbsolutePath();
	encodingCharset = StandardCharsets.UTF_8;
	encoding = encodingCharset.name();
	author = "UNDEFINED_AUTHOR";
	createInnerFiles();
	packInnerFiles();
    }
    
    protected void createInnerFiles() throws IOException {
	    Folders.createIn(this);
	    Mimetype.create(this);
	    ContainerXML.create(this);
	    StylesheetCSS.create(this);
	    Chapters.create(this);
    }
    
    protected void packInnerFiles() throws IOException {
	    FileOutputStream fos = new FileOutputStream(this.file);
	    BufferedOutputStream bos = new BufferedOutputStream(fos);
	    ZipOutputStream zos = new ZipOutputStream(bos, encodingCharset);
	    zos.setMethod(ZipOutputStream.DEFLATED);
	    for (String filePath : chapterFilesPaths) {
		String zenPath = filePath.substring(this.path.length()+1).replaceAll("\\\\","/");
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
	    System.out.println("[Created Epub File] "+this.getTitle());
    }
    public File getFile() {
	return this.file;
    }
    public List<String> getSupportFilesPaths() {
	return this.supportFilesPaths;
    }
    public void addToSupportFilesPaths(String filePath) {
	this.supportFilesPaths.add(filePath);
    }
    public void addToSupportFilesPaths(int index, String filePath) {
	this.supportFilesPaths.add(index, filePath);
    }
    public List<String> getChapterFilesPaths() {
	return this.chapterFilesPaths;
    }
    public void addToChapterFilesPaths(String filePath) {
	this.chapterFilesPaths.add(filePath);
    }
    public List<String> getSummary() {
	return this.summary;
    }
    public void addToSummary(String line) {
	this.summary.add(line);
    }
    
    public String[] getInnerFoldersPaths() {
	return this.innerFoldersPaths;
    }
    public String getInnerFoldersPath(int index) {
	return this.innerFoldersPaths[index];
    }
    public String getUrl() {
	return this.url;
    }
    public String getPath() {
	return this.path;
    }
    public String getTempPath() {
	return this.tempPath;
    }
    public String getEncoding() {
	return this.encoding;
    }
    public Charset getEncodingCharset() {
	return this.encodingCharset;
    }
    public String getTitle() {
	return this.title;
    }
    public String getAuthor() {
	return this.author;
    }
    public String getBookID() {
	return this.bookID;
    }
    public String getTimeOfCreation() {
	return this.timeOfCreation;
    }
    public String getDateOfCreation() {
	return this.dateOfCreation;
    }
    public Document getFrontPage() {
	return this.frontPage;
    }
}
