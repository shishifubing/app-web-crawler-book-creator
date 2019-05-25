package com.makeepub;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.Duration;
import java.time.Instant;
import java.util.ListIterator;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

final class Chapters extends CreateEpub {
    
    private static String[] chapterHeader = { 
	    "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\r\n",
	    "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.1//EN\"\r\n",
	    "  \"http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd\">\r\n", 
	    "\r\n",
	    "<html xmlns=\"http://www.w3.org/1999/xhtml\">\r\n", 
	    "<head>\r\n", 
	    "  <title></title>\r\n",
	    "  <link href=\"../Styles/stylesheet.css\" rel=\"stylesheet\" type=\"text/css\" />\r\n", 
	    "</head>\r\n",
	    "\r\n", 
	    "<body>\r\n" };

    protected static void create() {
	Document frontPage = null;
	try {
	    frontPage = Jsoup.connect(CreateEpub.url).get();
	} catch (IOException e) {
	    System.out.println("[!] Cannot connect to " + url);
	}
	Element title = frontPage.select("div.p-15 > *").first();
	CreateEpub.title = title.text();
	Elements summary = frontPage.select("div.p-15 > div.fr-view > *");
	for (Element paragraph : summary) {
	    String text = paragraph.text();
	    if ((!CreateEpub.summary.equals(null) && !CreateEpub.summary.isEmpty())
		    && (text.equals(null) || text.isEmpty())) break;
	    CreateEpub.summary.add(text);
	}
	Elements chapterLinks = frontPage.select("li.chapter-item > a");
	int startIndex = 1; 
	int endIndex = chapterLinks.size();
	CreateEpub.lastChapterIndex = chapterLinks.size();
	String linkRoot = "https://www.wuxiaworld.com";
	ListIterator<Element> itStart = chapterLinks.listIterator();
	ListIterator<Element> itEnd = chapterLinks.listIterator(chapterLinks.size());
	Instant start = Instant.now();
	while (itStart.hasNext() && itEnd.hasPrevious()) {
	    getChapter(linkRoot+itStart.next().attr("href"), startIndex);
	    System.out.println("[Created] Chapter " + startIndex++);
	    if (startIndex>endIndex) {
		break;
	    }
	    getChapter(linkRoot+itEnd.previous().attr("href"), endIndex);
	    System.out.println("[Created] Chapter " + endIndex--);
	}
	CreateEpub.timeTakenMills = Duration.between(start, Instant.now()).toMillis();
    }

    private static void getChapter(String url, int index) {
	Document chapter = null;
	try {
	    chapter = Jsoup.connect(url).get();
	} catch (IOException e) {
	    System.out.println("[!] Cannot connect to " + url);
	}
	Elements text = chapter.select("div.p-15 > * > *");
	String chapterFileIndex = ""+index;
	while (chapterFileIndex.length()<(""+CreateEpub.lastChapterIndex).length()) {
	    chapterFileIndex = "0" + chapterFileIndex;
	}
	try (PrintWriter chapterWriter = new PrintWriter("output/OEBPS/Text/chapter_" + chapterFileIndex + ".xhtml", encoding)) {
	    for (String string : chapterHeader) {
		chapterWriter.print(string);
	    }
	    boolean check = false;
	    String chapterTitle = "";
	    for (Element paragraph : text) {
		String line = escapeXHtml(paragraph.text());
		if (!check && !line.contentEquals("Previous Chapter") &&
			!line.equals(null) && !line.isEmpty()) {
		    chapterTitle = line;
		    line = "  <h1 id=\"chapter_" + index + "\">" + line + "</h1>\r\n";
		    chapterWriter.print(line);
		    check = true;
		
		} else if (line.contentEquals("Next Chapter") || 
			line.contentEquals("Bookmark")) {
		    break;
		} else if (line.contentEquals("Previous Chapter") ||
			line.contentEquals(chapterTitle)) {
		    continue;
		} else if (!line.equals(null) && !line.isEmpty()) {
		    chapterWriter.print("  <p>" + line + "</p>\r\n");
		} 
	    }
	    chapterWriter.print("</body>\r\n" + "</html>");
	    chapterWriter.close();
	} catch (IOException e) {
	    System.out.println("[!] Issues with the file of chapter " + index);
	}
    }
    
    private static String escapeXHtml(String input) {
	return input.replaceAll("&", "&amp;")
		.replaceAll("'", "&apos;")
		.replaceAll("<", "&lt;")
		.replaceAll(">", "&gt;");
    }
}
