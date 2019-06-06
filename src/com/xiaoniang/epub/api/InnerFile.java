package com.xiaoniang.epub.api;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.CRC32;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public abstract class InnerFile {
    private final List<String> content = new ArrayList<String>();
    private String innerPath;
    private EpubBook epubBook;
    private int volume = -1;

    public void addToZip(ZipOutputStream zos) throws IOException {
	String zenPath = innerPath.replaceAll("\\\\", "/");
	ZipEntry zen = new ZipEntry(zenPath);
	String fileContent = "";
	for (String line : content) {
	    fileContent += line;
	}
	byte[] fileArray = fileContent.getBytes(epubBook.encodingCharset());
	if (zenPath.endsWith("mimetype") || zenPath.endsWith(".jpg")) {
	    zen.setMethod(ZipEntry.STORED);
	    CRC32 crc = new CRC32();
	    crc.update(fileArray);
	    zen.setCrc(crc.getValue());
	    zen.setSize(fileArray.length);
	    zen.setCompressedSize(fileArray.length);
	}
	zos.putNextEntry(zen);
	ByteArrayInputStream fis = new ByteArrayInputStream(fileArray);
	int len;
	byte[] buffer = new byte[1024];
	while ((len = fis.read(buffer)) >= 0) {
	    zos.write(buffer, 0, len);
	}
	System.out.println("[Added] " + innerPath);
	zos.closeEntry();
	fis.close();
    }

    protected static String escapeHtml(String input) {
	return input.replaceAll("&", "&amp;").replaceAll("'", "&apos;").replaceAll("<", "&lt;").replaceAll(">", "&gt;");
    }
    protected void addContent(String... line) {
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
