package com.xiaoniang.epub.innerfiles;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipOutputStream;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.xiaoniang.epub.api.EpubBook;
import com.xiaoniang.epub.api.InnerFile;

public class Chapter extends InnerFile implements Runnable {
	private final String url;
	private final int volume;
	private final int chapterIndex;
	private final int chapterTitleIndex;
	private final ZipOutputStream zos;

	Chapter(EpubBook epubBook, String url, int volume, int chapterIndex, int chapterTitleIndex, ZipOutputStream zos) {
		setEpubBook(epubBook);
		this.url = url;
		this.volume = volume;
		this.chapterIndex = chapterIndex;
		this.chapterTitleIndex = chapterTitleIndex;
		this.zos = zos;
		thread = new Thread(this);
		thread.setName(url);
		thread.start();
	}

	public static void downloadChapters(EpubBook epubBook, int targetVolume, ZipOutputStream zos) throws IOException {
		int volumeIndex = 0;
		int chapterIndex = 1;
		int chapterIndexVolumeStart = 1;
		Toc toc = new Toc(epubBook, volumeIndex);
		Content content = new Content(epubBook, volumeIndex);
		List<Chapter> chapters = new ArrayList<Chapter>();
		for (ArrayList<String[]> volumeChapters : epubBook.chapterLinks()) {
			volumeIndex++;
			if (targetVolume != 0 && volumeIndex != targetVolume) {
				chapterIndex += volumeChapters.size();
			} else {
				chapterIndexVolumeStart = chapterIndex;
				for (String[] chapterLink : epubBook.chapterLinksVolume(volumeIndex)) {
					String chapterFileName = "" + chapterIndex;
					while (chapterFileName.length() < 4) {
						chapterFileName = "0" + chapterFileName;
					}
					chapterFileName = "chapter_" + chapterFileName + ".xhtml";
					Chapter chapter = new Chapter(epubBook, chapterLink[1], volumeIndex, chapterIndex++,
							chapterIndex - chapterIndexVolumeStart, zos);
					chapters.add(chapter);
					toc.addNavPoint(escapeAllHtml(chapterLink[0]), chapterFileName);
					content.addToManifestAndSpine(chapterFileName);
				}
				if (targetVolume != 0) {
					break;
				}
			}
		}
		for (Chapter chapter : chapters) {
			try {
				chapter.thread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		toc.fill();
		toc.addToZip(zos);
		content.fill();
		content.addToZip(zos);
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
				chapter = Jsoup.parse(Jsoup.connect(url).cookies(epubBook().cookies()).timeout(10000).get().html());
			} catch (IOException e) {
				// System.out.println("[!] Cannot connect to the " + url);
			}
		}
		Elements text = chapter.select("div.p-15 > div.fr-view > p");
		String chapterFileIndex = "" + chapterIndex;
		while (chapterFileIndex.length() < 4) {
			chapterFileIndex = "0" + chapterFileIndex;
		}
		setInnerPath(epubBook().innerFolderPath(3) + "chapter_" + chapterFileIndex + ".xhtml");
		String chapterTitle = epubBook().chapterLink(volume, chapterTitleIndex, 0);
		addContent("  <h3 id=\"chapter_" + chapterIndex + "\">" + escapeAllHtml(chapterTitle) + "</h3>\r\n");
		chapterTitle = chapterTitle.replaceAll("[^a-zA-Z]", "");
		for (Element paragraph : text) {
			String line = escapeAllHtml(paragraph.text());
			if (!isParagraphTextValid(paragraph.text(), chapterTitle)) {
				continue;
			} else if (!paragraph.text().equals(null) && !paragraph.text().isEmpty()) {
				addContent("  <p>" + line + "</p>\r\n");
			}
		}
		addContent("</body>\r\n");
		addContent("</html>");
		synchronized (zos) {
			addToZip(zos);
		}
	}
/*
	private boolean isNodeValid(String name) {
		switch (name) {
		case "p":
		case "a":
		case "b":
		case "strong":
		case "i":
		case "em":
		case "mark":
		case "small":
		case "del":
		case "ins":
		case "sub":
		case "sup":
			return true;
		}
		return false;
	}
*/
	private boolean isParagraphTextValid(String text, String chapterTitle) {
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

}
