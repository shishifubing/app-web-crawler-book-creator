package com.xiaoniang.epub;

import java.io.File;

public class ContentOPF extends InnerFiles {

    ContentOPF(EpubBook epubBook, int start, int end) {
	addContent("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n");
	addContent(
		"<package xmlns=\"http://www.idpf.org/2007/opf\" unique-identifier=\"BookId\" version=\"2.0\">\r\n");
	addContent(
		"  <metadata xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xmlns:opf=\"http://www.idpf.org/2007/opf\">\r\n");
	addContent("    <dc:identifier id=\"BookId\" opf:scheme=\"XN\">"+epubBook.bookID()
		+"</dc:identifier>\r\n");
	addContent("    <dc:title>"+epubBook.title()+"</dc:title>\r\n");
	addContent("    <dc:creator opf:file-as=\""+epubBook.author()+"\" opf:role=\"aut\">"
		+epubBook.author());
	addContent("</dc:creator>\r\n");
	addContent("    <dc:language>en</dc:language>\r\n");
	addContent("    <dc:date opf:event=\"creation\">"+epubBook.timeOfCreation()
		+"</dc:date>\r\n");
	addContent("    <dc:publisher>WuxiaWorld.com</dc:publisher>\r\n");
	addContent(
		"    <dc:rights>All materials' copyrights reserved by their respective authors and the associated publishers. Please respect their rights. Works will be deleted upon request by copyright holders.</dc:rights>\r\n");
	addContent("    <meta name=\"cover\" content=\"cover.jpg\" />\r\n");
	addContent("  </metadata>\r\n");
	addContent("  <manifest>\r\n");
	addContent("    <item href=\"toc.ncx\" id=\"ncx\" media-type=\"application/x-dtbncx+xml\" />\r\n");
	addContent(
		"    <item href=\"Text/cover.xhtml\" id=\"cover.xhtml\" media-type=\"application/xhtml+xml\" />\r\n");
	addContent("    <item href=\"Styles/stylesheet.css\" id=\"stylesheet.css\" media-type=\"text/css\" />\r\n");
	setInnerPath(epubBook.innerFolderPath(0) + "content.opf");
	setFile(new File(epubBook.tempPath() + innerPath()));
	for (int i = start; i <= end; i++) {
	    String chapterFileIndex = "" + i;
	    while (chapterFileIndex.length() < ("" + end).length()) {
		chapterFileIndex = "0" + chapterFileIndex;
	    }
	    addContent("    <item href=\"Text/chapter_" + chapterFileIndex + ".xhtml\" id=\"Chapter_" + chapterFileIndex
		    + ".xhtml\" media-type=\"application/xhtml+xml\" />\r\n");
	}
	addContent("  </manifest>\r\n");
	addContent("  <spine toc=\"ncx\">\r\n");
	addContent("    <itemref idref=\"cover.xhtml\" />\r\n");
	for (int i = start; i <= end; i++) {
	    String chapterFileIndex = "" + i;
	    while (chapterFileIndex.length() < ("" + end).length()) {
		chapterFileIndex = "0" + chapterFileIndex;
	    }
	    addContent("    <itemref idref=\"Chapter_" + chapterFileIndex + ".xhtml\" />\r\n");
	}
	addContent("  </spine>\r\n");
	addContent("  <guide>\r\n");
	addContent("    <reference href=\"Text/cover.xhtml\" title=\"Cover\" type=\"cover\" />\r\n");
	addContent("  </guide>\r\n");
	addContent("</package>");
	addContent("    <item href=\"Text/chapter_");
	addContent(".xhtml\" id=\"Chapter_");
	addContent(".xhtml\" media-type=\"application/xhtml+xml\" />\r\n");
	addContent("    <itemref idref=\"Chapter_");
	addContent(".xhtml\" />\r\n");
    }

}
