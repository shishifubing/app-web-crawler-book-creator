package com.xiaoniang.epub.innerfiles;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipOutputStream;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.adobe.epubcheck.api.EpubCheck;
import com.xiaoniang.epub.api.EpubBook;
import com.xiaoniang.epub.api.InnerFile;

public class ChapterXHTML extends InnerFile {
    ChapterXHTML(EpubBook epubBook, String url, int chapterIndex) {
	setEpubBook(epubBook);
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
	Document chapter = null;
	while (chapter == null) {
	    try {
		chapter = Jsoup.connect(url).get();
	    } catch (IOException e) {
		System.out.println("[!] Cannot connect to the " + url);
	    }
	}
	Elements text = chapter.select("div.p-15 > * > *");
	String chapterFileIndex = "" + chapterIndex;
	while (chapterFileIndex.length() < 4) {
	    chapterFileIndex = "0" + chapterFileIndex;
	}
	setInnerPath(epubBook.innerFolderPath(3) + "chapter_" + chapterFileIndex + ".xhtml");
	boolean check = false;
	String chapterTitle = "";
	for (Element paragraph : text) {
	    String line = escapeHtml(paragraph.text());
	    if (!check && !line.contentEquals("Previous Chapter") && !line.equals(null) && !line.isEmpty()) {
		chapterTitle = line.replaceAll("[^a-zA-Z]", "");
		line = "  <h1 id=\"chapter_" + chapterIndex + "\">" + line + "</h1>\r\n";
		addContent(line);
		check = true;

	    } else if (line.contentEquals("Next Chapter") || line.contentEquals("Bookmark")) {
		break;
	    } else if (line.contentEquals("Previous Chapter")
		    || line.replaceAll("[^a-zA-Z]", "").contentEquals(chapterTitle)) {
		continue;
	    } else if (!line.equals(null) && !line.isEmpty()) {
		addContent("  <p>" + line + "</p>\r\n");
	    }
	}
	addContent("</body>\r\n");
	addContent("</html>");
    }

    public static void downloadChapters(EpubBook epubBook, int targetVolume) {
	File epubFile = new File(epubBook.path() + epubBook.title() + ".epub");
	try (ZipOutputStream zos = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(epubFile)),
		epubBook.encodingCharset())) {
	    zos.setMethod(ZipOutputStream.DEFLATED);
	    new Mimetype(epubBook).addToZip(zos);
	    new ContainerXML(epubBook).addToZip(zos);
	    CoverJPG coverJPG = new CoverJPG(epubBook);
	    coverJPG.addToZip(zos);
	    new CoverXHTML(epubBook, coverJPG).addToZip(zos);
	    new DescriptionXHTML(epubBook).addToZip(zos);
	    new StylesheetCSS(epubBook).addToZip(zos);
	    Elements volumes = epubBook.frontPage().select("div.panel-group > *");
	    int volumeIndex = 0;
	    int chapterIndex = 1;
	    int chapterIndexStart = 1;
	    for (Element volumeChapters : volumes) {
		Elements chaptersLinks = volumeChapters.select("li.chapter-item > a");
		volumeIndex++;
		if (chaptersLinks.isEmpty()) {
		    continue;
		}
		if (targetVolume != 0 && volumeIndex != targetVolume) {
		    chapterIndex += chaptersLinks.size();
		} else {
		    if (targetVolume != 0) {
			chapterIndexStart = chapterIndex;
		    }
		    for (Element chapterLink : chaptersLinks) {
			new ChapterXHTML(epubBook, chapterLink.attr("abs:href"),
				chapterIndex++).addToZip(zos);;
		    }
		    if (targetVolume != 0)
			break;
		}
	    }
	    
	    new TocNCX(epubBook, chapterIndexStart, chapterIndex - 1, targetVolume).addToZip(zos);
	    new ContentOPF(epubBook, chapterIndexStart, chapterIndex - 1, targetVolume).addToZip(zos);
	} catch (IOException e) {

	}
	EpubCheck check = new EpubCheck(epubFile);
	check.validate();
    }

}
