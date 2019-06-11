package com.xiaoniang.epub.innerfiles;

import java.io.IOException;
import java.util.zip.ZipOutputStream;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.xiaoniang.epub.api.EpubBook;
import com.xiaoniang.epub.api.InnerFile;

public class ChapterXHTML extends InnerFile implements Runnable{
	private final String url;
	private final int volume;
	private final int chapterIndex;
	private final int chapterTitleIndex;
	private final ZipOutputStream zos;
	
	ChapterXHTML(EpubBook epubBook, String url, int volume, int chapterIndex, int chapterTitleIndex, ZipOutputStream zos) {
		setEpubBook(epubBook);
		this.url = url;
		this.volume = volume;
		this.chapterIndex = chapterIndex;
		this.chapterTitleIndex = chapterTitleIndex;
		this.zos = zos;
	}

	public static void downloadChapters(EpubBook epubBook, int targetVolume, ZipOutputStream zos) throws IOException {
		Elements volumes = epubBook.frontPage().select("div.panel-group > *");
		epubBook.addVolumesToChapterTitles(volumes.size());
		int volumeIndex = 0;
		int chapterIndex = 1;
		int chapterIndexVolumeStart = 1;
		for (Element volumeChapters : volumes) {
			Elements chaptersLinks = volumeChapters.select("li.chapter-item > a");
			if (chaptersLinks.isEmpty()) {
				break;
			}
			volumeIndex++;
			if (targetVolume != 0 && volumeIndex != targetVolume) {
				chapterIndex += chaptersLinks.size();
			} else {
				chapterIndexVolumeStart = chapterIndex;
				for (Element chapterLink : chaptersLinks) {
					epubBook.addChapterTitle(volumeIndex, chapterLink.select("span").text());
					Thread newThread = new Thread (new ChapterXHTML(epubBook, chapterLink.attr("abs:href"), volumeIndex, chapterIndex++,
							chapterIndex - chapterIndexVolumeStart, zos));
					newThread.setName(chapterLink.select("span").text());
					newThread.start();
					try {
						newThread.join();
					} catch (InterruptedException e) {
						System.out.println("[!] Interrupted thread " + newThread.getName());
					}
				}
				if (targetVolume != 0) {
					break;
				}
			}
		}
		chapterIndex--;
		if (targetVolume == 0) {
			chapterIndexVolumeStart = 1;
			volumeIndex = 0;
		}
		new TocNCX(epubBook, chapterIndexVolumeStart, chapterIndex, volumeIndex).addToZip(zos);
		new ContentOPF(epubBook, chapterIndexVolumeStart, chapterIndex, volumeIndex).addToZip(zos);
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
		setInnerPath(epubBook().innerFolderPath(3) + "chapter_" + chapterFileIndex + ".xhtml");
		String chapterTitle = epubBook().chapterTitle(volume, chapterTitleIndex);
		addContent("  <h3 id=\"chapter_" + chapterIndex + "\">" + escapeHtml(chapterTitle) + "</h3>\r\n");
		chapterTitle = chapterTitle.replaceAll("[^a-zA-Z]", "");
		for (Element paragraph : text) {
			String line = paragraph.text();
			if (line.contentEquals("Next Chapter") || line.contentEquals("Bookmark")) {
				break;
			} else if (line.contentEquals("Previous Chapter")
					|| line.replaceAll("[^a-zA-Z]", "").contentEquals(chapterTitle)) {
				continue;
			} else if (!line.equals(null) && !line.isEmpty()) {
				addContent("  <p>" + escapeHtml(line) + "</p>\r\n");
			}
		}
		addContent("</body>\r\n");
		addContent("</html>");
		try {
			addToZip(zos);
		} catch (IOException e) {
			System.out.println("[!] Failure to add chapter "+chapterIndex+" to epub file");
		}
	}

}