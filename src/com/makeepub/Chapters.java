package com.makeepub;

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

final class Chapters extends CreateEpub {

    private static String[] chapterHeader = { "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\r\n",
	    "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.1//EN\"\r\n",
	    "  \"http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd\">\r\n", "\r\n",
	    "<html xmlns=\"http://www.w3.org/1999/xhtml\">\r\n", "<head>\r\n", "  <title></title>\r\n",
	    "  <link href=\"../Styles/stylesheet.css\" rel=\"stylesheet\" type=\"text/css\" />\r\n", "</head>\r\n",
	    "\r\n", "<body>\r\n" };

    protected static void create() {
	Document frontPage = null;
	try {
	    frontPage = Jsoup.connect(CreateEpub.url).get();
	} catch (IOException e) {
	    System.out.println("[!] Cannot connect to the " + url);
	}
	Element title = frontPage.select("div.p-15 > *").first();
	CreateEpub.title = title.text();
	Cover_xhtml.create();
	Elements summary = frontPage.select("div.p-15 > div.fr-view > *");
	for (Element paragraph : summary) {
	    String text = paragraph.text();
	    if ((!CreateEpub.summary.equals(null) && !CreateEpub.summary.isEmpty())
		    && (text.equals(null) || text.isEmpty()))
		break;
	    CreateEpub.summary.add(text);
	}
	String linkRoot = "https://www.wuxiaworld.com";
	Elements volumes = frontPage.select("div.panel-group > *");
	String volumeName = "";
	int index = 1;
	int volumeIndex = 1;
	for (Element volumeChapters : volumes) {
	    Elements chapterLinks = volumeChapters.select("li.chapter-item");
	    if (chapterLinks.isEmpty()) {
		continue;
	    }
	    int volumeStart = index;
	    int volumeEnd = index + chapterLinks.size();
	    for (Element chapterLink : chapterLinks) {
		if (getChapter(linkRoot + chapterLink.attr("href"), index, volumeEnd)) {
		    System.out.println("[Created Chapter] " + index++);
		}
	    }
	    Toc_ncx.create(volumeStart, volumeEnd);
	    Content_opf.create(volumeStart, volumeEnd);
	    CreateEpub.title = CreateEpub.title +" Volume " + volumeIndex++;
	    try {
		allFiles = supportFiles;
		listFiles(new File(CreateEpub.tempDir + "OEBPS/Text/"), allFiles);
		EpubFile.create(allFiles);
		EpubCheck epubcheck = new EpubCheck(CreateEpub.epubFile);
		if (epubcheck.validate()) {
		    System.out.println("[EPUB FILE IS VALID]");
		} else {
		    System.out.println("[!] EPUB FILE HAS ERRORS");
		}
	    } catch (IOException e) {
		System.out.println("[!] Can't list files in OEBPS/Text");
	    }
	}
    }

    private static boolean getChapter(String url, int index, int volumeEnd) {
	Document chapter = null;
	try {
	    chapter = Jsoup.connect(url).get();
	} catch (IOException e) {
	    System.out.println("[!] Cannot connect to the " + url);
	    return false;
	}
	Elements text = chapter.select("div.p-15 > * > *");
	String chapterFileIndex = "" + index;
	while (chapterFileIndex.length() < ("" + volumeEnd).length()) {
	    chapterFileIndex = "0" + chapterFileIndex;
	}
	try (PrintWriter chapterWriter = new PrintWriter(
		CreateEpub.tempDir + "OEBPS/Text/chapter_" + chapterFileIndex + ".xhtml", encoding)) {
	    for (String string : chapterHeader) {
		chapterWriter.print(string);
	    }
	    boolean check = false;
	    String chapterTitle = "";
	    for (Element paragraph : text) {
		String line = escapeXHtml(paragraph.text());
		if (!check && !line.contentEquals("Previous Chapter") && !line.equals(null) && !line.isEmpty()) {
		    chapterTitle = line;
		    line = "  <h1 id=\"chapter_" + index + "\">" + line + "</h1>\r\n";
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
	    System.out.println("[!] Issues with the file of the chapter " + index);
	    return false;
	}
	return true;
    }

    private static String escapeXHtml(String input) {
	return input.replaceAll("&", "&amp;").replaceAll("'", "&apos;").replaceAll("<", "&lt;").replaceAll(">", "&gt;");
    }
}
