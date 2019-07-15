package com.xiaoniang.epub.innerfiles;

import java.io.EOFException;
import java.io.IOException;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipOutputStream;

import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.xiaoniang.epub.api.EpubBook;
import com.xiaoniang.epub.api.InnerFile;
import com.xiaoniang.epub.resources.Links;
import com.xiaoniang.epub.resources.Log;

public class Chapter extends InnerFile implements Runnable {
	private final String url;
	private final int index;
	private final ZipOutputStream zos;
	private final Content content;
	private final Toc toc;
	private String title;

	public Chapter(EpubBook epubBook, ZipOutputStream zos, String chapterLink, int chapterIndex, String title, Content content, Toc toc) {
		setEpubBook(epubBook);
		url = chapterLink;
		this.index = chapterIndex;
		this.zos = zos;
		this.toc = toc;
		this.content = content;
		this.title = title;
		thread = new Thread(this);
		thread.setName("Chapter_" + chapterIndex);
		thread.start();
	}
	
	static public void getChapters(String urlNovelUpdates, EpubBook epubBook, ZipOutputStream zos, Content content, Toc toc) {
		if (Links.links().containsKey(urlNovelUpdates)) {
			Log.println("Contains "+urlNovelUpdates+". "+Links.link(urlNovelUpdates));
		} else {
			Log.println("Doesn't contain "+urlNovelUpdates);
		}
		Document document = null;
		while (document == null) {
			try {
				document = Jsoup.connect(urlNovelUpdates).timeout(10000).get();
			} catch (Exception e) {
			}
		}
		int chapterIndex = 0;
		switch (Links.link(urlNovelUpdates).split("/")[2].toLowerCase()) {
		case "www.wuxiaworld.com":
		}
		Elements chapters = document.select("div.p-15 > div.panel-group > * > a");
		for (Element chapter : chapters) {
			String chapterLink = chapter.attr("abs:hres");
			String chapterName = chapter.text();
			epubBook.addChapter(zos, chapterLink, ++chapterIndex, chapterName, content, toc);
		}
		
	}

	@Override
	public void run() {
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
		String url = null;
		while (chapter == null) {
			try {
				Connection.Response response = Jsoup.connect(this.url).followRedirects(true)
						.timeout(10000).execute();
				chapter = response.parse();
				url = response.url().toString();
			} catch (SSLHandshakeException e) {
			} catch (EOFException e) {
			} catch (SSLException e) {
			} catch (SocketException e) {
			} catch (Exception e) {
				Log.println("Cannot get Chapter " + index + ". url: " + this.url);
				e.printStackTrace(Log.stream());
			}
		}
		String chapterFileIndex = "" + index;
		while (chapterFileIndex.length() < 4) {
			chapterFileIndex = "0" + chapterFileIndex;
		}
		setInnerPath(epubBook().innerFolderPath(3) + "chapter_" + chapterFileIndex + ".xhtml");
		String chapterTitle = chapter.select(getTitleSelector(url)).text();
		addContent(" <h3 id=\"chapter_" + index + "\">" + escapeAllHtml(chapterTitle) + "</h3>\r\n");
		toc.addNavPoint(chapterTitle, "chapter_" + chapterFileIndex);
		content.addToManifestAndSpine("chapter_" + chapterFileIndex);
		chapterTitle = chapterTitle.replaceAll("[^a-zA-Z]", "");
		Elements lineElements = chapter.select(getTextSelector(url));
		for (Element lineElement : lineElements) {
			String line = escapeAllHtml(lineElement.text());
			if (!lineIsValid(lineElement.text(), chapterTitle)) {
				continue;
			} else if (!lineElement.text().equals(null) && !lineElement.text().isEmpty()) {
				addContent("  <p>" + line + "</p>\r\n");
			}
		}
		addContent("</body>\r\n");
		addContent("</html>");
		synchronized (zos) {
			addToZip(zos);
		}
	}

<<<<<<< HEAD
<<<<<<< HEAD
	private boolean lineIsValid(String line, String chapterTitle) {
		switch (line.toLowerCase()) {
		case "next chapter":
		case "bookmark":
		case "previous chapter":
		case "teaser":
		case "previous":
			return false;
		}
		String formattedLine = line.replaceAll("[^a-zA-Z]", "");
		if (formattedLine.contains(chapterTitle) 
				|| chapterTitle.contains(formattedLine)
				|| line.startsWith("[/expand]")
				|| line.startsWith("[caption id=")) {
=======
	private boolean lineIsValid(String text, String chapterTitle) {
		if (text.replaceAll("[^a-zA-Z]", "").startsWith(chapterTitle) || text.startsWith("[/expand]")
				|| text.startsWith("[caption id=")) {
			return false;
		}
=======
	private boolean lineIsValid(String text, String chapterTitle) {
		if (text.replaceAll("[^a-zA-Z]", "").startsWith(chapterTitle) || text.startsWith("[/expand]")
				|| text.startsWith("[caption id=")) {
			return false;
		}
>>>>>>> parent of 2493f63... Returned back to wuxiaworld, average time of downloading 500 chapters - 50s, all 55 books were downloaded in 1:30 hours, no errors
		switch (text) {
		case "Next Chapter":
		case "Bookmark":
		case "Previous Chapter":
<<<<<<< HEAD
>>>>>>> parent of 2493f63... Returned back to wuxiaworld, average time of downloading 500 chapters - 50s, all 55 books were downloaded in 1:30 hours, no errors
=======
>>>>>>> parent of 2493f63... Returned back to wuxiaworld, average time of downloading 500 chapters - 50s, all 55 books were downloaded in 1:30 hours, no errors
			return false;
		}
		return true;
	}

	public boolean join() {
		try {
			if (thread.isAlive()) {
				thread.join();
			}
		} catch (InterruptedException e) {
			Log.println("Thread " + thread.getName() + " was interrupted");
			e.printStackTrace(Log.stream());
			return false;
		}
		return true;
	}

	public String getTextSelector(String url) {
		String website = url.split("/")[2];
		switch (website.toLowerCase()) {
		case "www.wuxiaworld.com":
			return "div.p-15 > div.fr-view > p";
		}
		return "p";
	}

	public String getTitleSelector(String url) {
		String website = url.split("/")[2];
		switch (website.toLowerCase()) {
		case "www.wuxiaworld.com":
			return "div.p-15 > div.caption clearfix > div:eq(2)";
		}
		return "*";
	}

	public String url() {
		return url;
	}

}
