package com.xiaoniang.epub;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.adobe.epubcheck.api.EpubCheck;

final class Chapters extends InnerFiles {

    Chapters(EpubBook file) {
	addContent("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\r\n");
	addContent("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.1//EN\"\r\n");
	addContent("  \"http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd\">\r\n");
	addContent("\r\n");
	addContent("<html xmlns=\"http://www.w3.org/1999/xhtml\">\r\n");
	addContent("<head>\r\n");
	addContent("  <title></title>\r\n");
	addContent("  <link href=\"../Styles/stylesheet.css\" rel=\"stylesheet\" type=\"text/css\" />\r\n");
	addContent("</head>\r\n");
	addContent("\r\n");
	addContent("<body>\r\n");
	
    }
    
    protected void download(EpubBook epubBook) {
	String linkRoot = "https://www.wuxiaworld.com";
	Elements volumes = epubBook.getFrontPage().select("div.panel-group > *");
	Elements allChapters = epubBook.getFrontPage().select("li.chapter-item");
	int volumeIndex = 0;
	int[] volumeChaptersAmounts = new int[volumes.size()-1];
	addContent("" + allChapters.size());
	for (Element volumeChapters : volumes) {
	    Elements chapterLinks = volumeChapters.select("li.chapter-item");
	    if (chapterLinks.isEmpty()) {
		continue;
	    }
	    volumeChaptersAmounts[volumeIndex++] = chapterLinks.size();
	}
	int index = 1;
	for (Element chapterLink : allChapters) {
		while (!getChapter(linkRoot + chapterLink.attr("href"), index));
		epubBook.addToChapterFilesPaths(epubBook.getTempPath() + epubBook.getInnerFoldersPath(4) + "chapter_" + index++ + ".xhtml");
		    System.out.println("[Created Chapter] " + index);
	    }
    }

    private boolean getChapter(String url, int chapterIndex) {
	Document chapter = null;
	try {
	    chapter = Jsoup.connect(url).get();
	} catch (IOException e) {
	    System.out.println("[!] Cannot connect to the " + url);
	    return false;
	}
	Elements text = chapter.select("div.p-15 > * > *");
	String chapterFileIndex = "" + chapterIndex;
	while (chapterFileIndex.length() < getContent(11).length()) {
	    chapterFileIndex = "0" + chapterFileIndex;
	}
	try (PrintWriter chapterWriter = new PrintWriter(
		epubBook.getTempDir() + "OEBPS/Text/chapter_" + chapterFileIndex + ".xhtml", encoding)) {
	    for (String string : getContent()) {
		chapterWriter.print(string);
	    }
	    boolean check = false;
	    String chapterTitle = "";
	    for (Element paragraph : text) {
		String line = escapeXHtml(paragraph.text());
		if (!check && !line.contentEquals("Previous Chapter") && !line.equals(null) && !line.isEmpty()) {
		    chapterTitle = line;
		    line = "  <h1 id=\"chapter_" + chapterIndex + "\">" + line + "</h1>\r\n";
		    chapterWriter.print(line);
		    check = true;

		} else if (line.contentEquals("Next Chapter") || line.contentEquals("Bookmark")) {
		    break;
		} else if (line.contentEquals("Previous Chapter") || line.contentEquals(chapterTitle)) {
		    continue;
		} else if (!line.equals(null) && !line.isEmpty()) {
		    chapterWriter.print("  <p>" + line + "</p>\r\n");
		}
	    }
	    chapterWriter.print("</body>\r\n" + "</html>");
	} catch (IOException e) {
	    System.out.println("[!] Issues with the file of the chapter " + chapterIndex);
	    return false;
	}
	return true;
    }

    private static String escapeXHtml(String input) {
	return input.replaceAll("&", "&amp;").replaceAll("'", "&apos;").replaceAll("<", "&lt;").replaceAll(">", "&gt;");
    }
}
