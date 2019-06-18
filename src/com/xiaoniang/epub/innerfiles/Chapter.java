package com.xiaoniang.epub.innerfiles;

import java.io.IOException;
import java.net.ConnectException;
import java.util.zip.ZipOutputStream;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.xiaoniang.epub.api.EpubBook;
import com.xiaoniang.epub.api.InnerFile;
import com.xiaoniang.epub.resources.Log;

public class Chapter extends InnerFile implements Runnable {
	private final String url;
	private final int index;
	private final ZipOutputStream zos;

	public Chapter(EpubBook epubBook, ZipOutputStream zos, String chapterLink, int index) {
		setEpubBook(epubBook);
		url = chapterLink;
		this.index = index;
		this.zos = zos;
		thread = new Thread(this); thread.setName(url); thread.start();
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
		while (chapter == null) {
			try {
				chapter = Jsoup.connect(this.url).cookies(epubBook.cookies()).followRedirects(true)
						.execute().parse();
			} catch (ConnectException e) {
			}  catch (IOException e) {
			}
		}
		Log.println("Added chapter: "+chapter.title());
		
		while (chapter == null) {
			try {
				chapter = Jsoup.parse(Jsoup.connect(url).cookies(epubBook().cookies()).timeout(10000).get().html());
			} catch (IOException e) {
				Log.println("[!] Cannot connect to the " + url);
				e.printStackTrace(Log.stream());
			}
		}
		Elements lineElements = chapter.select("div.p-15 > div.fr-view > p");
		String chapterFileIndex = "" + index;
		while (chapterFileIndex.length() < 4) {
			chapterFileIndex = "0" + chapterFileIndex;
		}
		setInnerPath(epubBook().innerFolderPath(3) + "chapter_" + chapterFileIndex + ".xhtml");
		String chapterTitle = ""; // REPLACE OLD ALGORITHM= epubBook().chapterLink(volume, chapterTitleIndex, 0);
		// REPLACE addContent(" <h3 id=\"chapter_" + chapterIndex + "\">" +
		// escapeAllHtml(chapterTitle) + "</h3>\r\n");
		chapterTitle = chapterTitle.replaceAll("[^a-zA-Z]", "");
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
		addToZip(zos);
		
	}

	private boolean lineIsValid(String text, String chapterTitle) {
		if (text.replaceAll("[^a-zA-Z]", "").startsWith(chapterTitle) || text.startsWith("[/expand]")
				|| text.startsWith("[caption id=")) {
			return false;
		}
		switch (text) {
		case "Next Chapter":
		case "Bookmark":
		case "Previous Chapter":
			return false;
		}
		return true;
	}

	public boolean join() {
		try {
			thread.join();
		} catch (InterruptedException e) {
			Log.println("Thread " + thread.getName() + " was interrupted");
			e.printStackTrace(Log.stream());
			return false;
		}
		return true;
	}

}
