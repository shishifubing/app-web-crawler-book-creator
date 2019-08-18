package com.xiaoniang.epub.innerfiles;

import java.io.IOException;
import java.util.zip.ZipOutputStream;

import org.jsoup.Connection.Response;
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
	private String fileName;
	private String title;

	public Chapter(EpubBook epubBook, ZipOutputStream zos, String url, int index, String title, Content content, Toc toc) {
		setEpubBook(epubBook);
		this.url = url;
		this.index = index;
		this.zos = zos;
		this.title = title;
		thread = new Thread(this);
		thread.setName("Chapter_" + index);
		thread.start();
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
			timeOfCreation = System.currentTimeMillis();
			try {
				Response response = Jsoup.connect(url).cookies(epubBook.cookies())
						.execute();
				if (response.statusCode()!=200) {
					Log.println("\n\n[Status Message]\n----------------------------------------");
					Log.println(response.statusMessage());
					Log.println("----------------------------------------\n");
					continue;
				} 
				chapter = response.parse();
			} catch (IOException e) {
				//Log.println("[Error]\n----------------------------------------");
				//Log.println("Cannot get Chapter " + index + ". url: " + url);
				//e.printStackTrace(Log.stream());
				//Log.println("----------------------------------------\n");
			}
		}
		String chapterFileIndex = "" + index;
		while (chapterFileIndex.length() < 4) {
			chapterFileIndex = "0" + chapterFileIndex;
		}
		setInnerPath(epubBook().innerFolderPath(3) + "chapter_" + chapterFileIndex + ".xhtml");
		fileName = "chapter_" + chapterFileIndex + ".xhtml";
		String chapterTitle = title;
		addContent(" <h3 id=\"chapter_" + index + "\">" + escapeHtml(chapterTitle) + "</h3>\r\n");
		chapterTitle = chapterTitle.replaceAll("[^a-zA-Z]", "");
		Elements lineElements = chapter.select("div.p-15 > div.fr-view > p");
		for (Element lineElement : lineElements) {
			String line = escapeHtml(lineElement.text());
			if (!lineIsValid(lineElement.text(), chapterTitle)) {
				continue;
			} else if (!lineElement.text().equals(null) && !lineElement.text().isEmpty()) {
				addContent("  <p>" + line + "</p>\r\n");
			}
		}
		addContent("    \r\r\n\n\n<p>Chapter source: <a href=\"" + url + "\">"
				+ url + "</a></p>\r\n");
		addContent("</body>\r\n");
		addContent("</html>");
		synchronized (zos) {
			addToZip(zos);
		}
	}

	private boolean lineIsValid(String line, String chapterTitle) {
		if (line.replaceAll("[^a-zA-Z]", "").startsWith(chapterTitle) || line.startsWith("[/expand]")
				|| line.startsWith("[caption id=")
				|| line.replaceAll("[^a-zA-Z]", "").contains(chapterTitle)) {
			return false;
		}
		switch (line) {
		case "Next Chapter":
		case "Bookmark":
		case "Previous Chapter":
		case "Teaser":
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
	public int index() {
		return index;
	}
	public String fileName() {
		return fileName;
	}
	public String title() {
		return title;
	}
}
