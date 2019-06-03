package com.xiaoniang.epub.api;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.CRC32;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.adobe.epubcheck.api.EpubCheck;
import com.xiaoniang.epub.Chapter;
import com.xiaoniang.epub.ContainerXML;
import com.xiaoniang.epub.CoverXHTML;
import com.xiaoniang.epub.Folders;
import com.xiaoniang.epub.Mimetype;
import com.xiaoniang.epub.StylesheetCSS;

public class EpubBook {

    private final List<ArrayList<File>> chapterFiles;
    private final List<File> supportFiles;
    private final String description;
    private final List<String> volumeTitles;
    private final String[] innerFoldersPaths = { "OEBPS" + File.separator, "META-INF" + File.separator,
	    "OEBPS" + File.separator + "Styles" + File.separator, "OEBPS" + File.separator + "Text" + File.separator };
    private final String urlWuxiaWorld;
    private final String urlNovelUpdates;
    private final String coverLink;
    private final Document wuxiaWorldPage;
    private final Document novelUpdatesPage;
    private final String path;
    private final String tempPath;
    private final String encoding;
    private final Charset encodingCharset;
    private final String title;
    private final String author;
    private final String bookID;
    private final String timeOfCreation;
    private final String dateOfCreation;

    EpubBook(String outputPath, String link, int... volumes) throws IOException {
	urlWuxiaWorld = link;
	urlNovelUpdates = "https://www.novelupdates.com/series/"+link.split("novel")[1].substring(1);
	path = outputPath;
	chapterFiles = new ArrayList<ArrayList<File>>();
	supportFiles = new ArrayList<File>();
	volumeTitles = new ArrayList<String>();
	Document wuxiaWorldPageDocument = null;
	Document novelUpdatesPageDocument = null;
	while (wuxiaWorldPageDocument == null) {
	    try {
		wuxiaWorldPageDocument = Jsoup.connect(urlWuxiaWorld).get();
	    } catch (IOException e) {
		System.out.println("[!] Cannot connect to the " + urlWuxiaWorld);
	    }
	}
	while (novelUpdatesPageDocument == null) {
	    try {
		novelUpdatesPageDocument = Jsoup.connect(urlNovelUpdates).get();
	    } catch (IOException e) {
		System.out.println("[!] Cannot connect to the " + urlNovelUpdates);
	    }
	}
	wuxiaWorldPage = wuxiaWorldPageDocument;
	novelUpdatesPage = novelUpdatesPageDocument;
	title = wuxiaWorldPage.select("div.p-15 > *").first().text();
	coverLink = novelUpdatesPage.select("div.seriesimg > img").first().attr("src");
	description = novelUpdatesPage.select("div#editdescription").text();
	author = novelUpdatesPage.select("div#showauthors > *").first().text();
	Elements volumeTitlesElements = wuxiaWorldPage.select("div.panel-group").select("span.title");
	volumeTitles.add("");
	for (Element volumeTitle : volumeTitlesElements) {
	    volumeTitles.add(" "+volumeTitle.text());
	}
	dateOfCreation = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
	timeOfCreation = LocalTime.now().format(DateTimeFormatter.ofPattern("HH-mm-ss"));
	bookID = "WuxiaWorld.com-" + "XiaoNiang-" + dateOfCreation + "-";
	File tempDir = new File(
		"WuxiaWorld.com-" + "XiaoNiang-" + dateOfCreation + '-' + timeOfCreation + File.separator);
	tempDir.mkdir();
	tempPath = tempDir.getAbsolutePath() + File.separator;
	encodingCharset = StandardCharsets.UTF_8;
	encoding = encodingCharset.name();
	Folders.createFor(this);
	Mimetype mimetype = new Mimetype(this);
	mimetype.fill();
	ContainerXML container = new ContainerXML(this);
	container.fill();
	StylesheetCSS stylesheet = new StylesheetCSS(this);
	stylesheet.fill();
	CoverXHTML cover = new CoverXHTML(this);
	cover.fill();
	Arrays.sort(volumes);
	if (volumes[0] == 0) {
	    Chapter.downloadChapters(this, 0);
	    for (int volume : volumes) {
		packInnerFiles(volume);
	    }
	} else {
	    for (int volume : volumes) {
		Chapter.downloadChapters(this, volume);
		packInnerFiles(volume);
	    }
	}
    }

    protected void packInnerFiles(int volume) throws IOException {
	String epubTitle = null;
	if (volume == 0) {
	    epubTitle = title;
	} else {
	    epubTitle = title + volumeTitle(volume);
	}
	File epubFile = new File(path + epubTitle + ".epub");
	FileOutputStream fos = new FileOutputStream(epubFile);
	BufferedOutputStream bos = new BufferedOutputStream(fos);
	ZipOutputStream zos = new ZipOutputStream(bos, encodingCharset);
	zos.setMethod(ZipOutputStream.DEFLATED);
	for (File file : supportFiles) {
	    packFile(zos, file);
	}
	if (volume == 0) {
	    for (List<File> list : chapterFiles) {
		for (File file : list) {
		    packFile(zos, file);
		}
	    }
	} else {
	    for (File file : chapterFiles.get(volume - 1)) {
		packFile(zos, file);
	    }
	}
	zos.close();
	fos.close();
	EpubCheck check = new EpubCheck(epubFile);
	check.validate();
	System.out.println("[Created Epub File] " + this.title());
    }

    private void packFile(ZipOutputStream zos, File file) throws IOException {
	String filePath = file.getAbsolutePath();
	String zenPath = filePath.substring(this.tempPath.length()).replaceAll("\\\\", "/");
	ZipEntry zen = new ZipEntry(zenPath);
	if (file.getName().contentEquals("mimetype") || file.getName().endsWith(".jpg")) {
	    zen.setMethod(ZipEntry.STORED);
	    CRC32 crc = new CRC32();
	    crc.update(Files.readAllBytes(file.toPath()));
	    zen.setCrc(crc.getValue());
	    zen.setSize(file.length());
	    zen.setCompressedSize(file.length());
	}
	zos.putNextEntry(zen);
	FileInputStream fis = new FileInputStream(file);
	int len;
	byte[] buffer = new byte[(int) file.length()];
	while ((len = fis.read(buffer)) >= 0) {
	    zos.write(buffer, 0, len);
	}
	System.out.println("[Zipped] " + file.getName());
	zos.closeEntry();
	fis.close();
    }

    public List<File> supportFiles() {
	return supportFiles;
    }

    public void addToSupportFiles(File file) {
	supportFiles.add(file);
    }

    public void addToSupportFiles(int index, File file) {
	supportFiles.add(index, file);
    }

    public List<ArrayList<File>> chapterFiles() {
	return chapterFiles;
    }

    public void addVolumeToChapterFiles(int chaptersAmount) {
	chapterFiles.add(new ArrayList<File>(chaptersAmount));
    }

    public void addToChapterFiles(int volume, File file) {
	chapterFiles.get(volume - 1).add(file);
    }
    public String volumeTitle(int index) {
	return volumeTitles.get(index);
    }
    public String description() {
	return this.description;
    }

    public String[] innerFoldersPaths() {
	return this.innerFoldersPaths;
    }

    public String innerFolderPath(int index) {
	return this.innerFoldersPaths[index];
    }

    public String url() {
	return this.urlWuxiaWorld;
    }

    public String path() {
	return this.path;
    }

    public String tempPath() {
	return this.tempPath;
    }

    public String encoding() {
	return this.encoding;
    }

    public Charset encodingCharset() {
	return this.encodingCharset;
    }

    public String title() {
	return this.title;
    }

    public String author() {
	return this.author;
    }

    public String bookID() {
	return this.bookID;
    }

    public String timeOfCreation() {
	return this.timeOfCreation;
    }

    public String dateOfCreation() {
	return this.dateOfCreation;
    }

    public Document frontPage() {
	return this.wuxiaWorldPage;
    }
}
