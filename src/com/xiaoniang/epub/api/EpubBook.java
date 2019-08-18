package com.xiaoniang.epub.api;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipOutputStream;

import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.adobe.epubcheck.api.EpubCheck;
import com.xiaoniang.epub.innerfiles.Chapter;
import com.xiaoniang.epub.innerfiles.Container;
import com.xiaoniang.epub.innerfiles.Content;
import com.xiaoniang.epub.innerfiles.CoverSrc;
import com.xiaoniang.epub.innerfiles.Cover;
import com.xiaoniang.epub.innerfiles.Description;
import com.xiaoniang.epub.innerfiles.Mimetype;
import com.xiaoniang.epub.innerfiles.Stylesheet;
import com.xiaoniang.epub.innerfiles.Toc;
import com.xiaoniang.epub.resources.Links;
import com.xiaoniang.epub.resources.Log;

public class EpubBook {

	private final List<String[]> genres;
	private final List<Chapter> chapters;
	private final List<String[]> chaptersInfo;
	private final List<String> tags;
	private final List<String> description;
	private final String[] storyType;
	private final String[] innerFoldersPaths = { "OEBPS" + File.separator, "META-INF" + File.separator,
			"OEBPS" + File.separator + "Styles" + File.separator, "OEBPS" + File.separator + "Text" + File.separator,
			"OEBPS" + File.separator + "Images" + File.separator };
	private final String urlNovelUpdates;
	private final String urlWuxiaWorld;
	private final String coverLink;
	private Map<String, String> cookies;
	private final String path;
	private final String encoding;
	private final Charset encodingCharset;
	private final String title;
	private final String author;
	private final String bookID;
	private final String timeOfCreation;
	private final String dateOfCreation;

	public EpubBook(String outputPath, String link) throws IOException {
		urlNovelUpdates = Links.link(link);
		urlWuxiaWorld = link;
		path = outputPath;
		Document novelUpdatesPage = null;
		while (novelUpdatesPage == null) {
			try {
				novelUpdatesPage = Jsoup.connect(urlNovelUpdates).timeout(10000).get();
			} catch (IOException e) {
				Log.println("[!] Cannot connect to the " + urlNovelUpdates);
			}
		}
		Document wuxiaWorldPage = null;
		while (wuxiaWorldPage == null) {
			try {
				Response response = Jsoup.connect(link).timeout(10000).execute();
				cookies = response.cookies();
				wuxiaWorldPage = response.parse();
			} catch (IOException e) {
				Log.println("[!] Cannot connect to the " + urlNovelUpdates);
			}
		}
		Elements chaptersElements = wuxiaWorldPage.select("li.chapter-item > a");
		chaptersInfo = new ArrayList<String[]>(chaptersElements.size());
		chapters = new ArrayList<Chapter>(chaptersElements.size());
		for (Element chapter : chaptersElements) {
			String chapterLink = chapter.attr("abs:href");
			String chapterName = InnerFile.escapeHtml(chapter.text());
			chaptersInfo.add(new String[] { chapterName, chapterLink });
		}
		title = novelUpdatesPage.select("div.w-blog-content > div.seriestitlenu").first().text();
		Log.println("Title: " + title);
		coverLink = novelUpdatesPage.select("div.seriesimg > *").first().attr("src");
		Elements descriptionElements = novelUpdatesPage.select("div#editdescription > p");
		description = new ArrayList<String>(descriptionElements.size());
		for (Element line : novelUpdatesPage.select("div#editdescription > p")) {
			description.add(InnerFile.escapeHtml(line.text()));
		}
		author = novelUpdatesPage.select("div#showauthors > *").first().text();
		Element storyTypeElement = novelUpdatesPage.select("div#showtype > *").first();
		storyType = new String[] { storyTypeElement.text(), storyTypeElement.attr("href") };
		Elements genreElements = novelUpdatesPage.select("div#seriesgenre > *");
		genres = new ArrayList<String[]>(genreElements.size());
		for (Element genre : genreElements) {
			genres.add(new String[] { genre.text(), genre.attr("href") });
		}
		Elements tagElements = novelUpdatesPage.select("div#showtags > *");
		tags = new ArrayList<String>(tagElements.size());
		for (Element tag : tagElements) {
			tags.add("<a href=\"" + tag.attr("href") + "\">" + tag.text() + "</a>");
		}
		dateOfCreation = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
		timeOfCreation = LocalTime.now().format(DateTimeFormatter.ofPattern("HH-mm-ss"));
		bookID = "WuxiaWorld.com-" + "XiaoNiang-" + dateOfCreation + "-";
		encodingCharset = StandardCharsets.UTF_8;
		encoding = encodingCharset.name();
	}

	public void create() {
		File epubFile = new File(path() + title() + ".epub");
		int duplicateIndex = 0;
		while (epubFile.exists()) {
			epubFile = new File(path() + title() + " (" + ++duplicateIndex + ")" + ".epub");
		}
		try (ZipOutputStream zos = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(epubFile)),
				encodingCharset())) {
			new Mimetype(this).addToZip(zos);
			new Container(this).addToZip(zos);
			CoverSrc coverSrc = new CoverSrc(this);
			coverSrc.addToZip(zos);
			new Cover(this, coverSrc).addToZip(zos);
			new Description(this).addToZip(zos);
			new Stylesheet(this).addToZip(zos);
			Content content = new Content(this);
			Toc toc = new Toc(this);
			int chapterIndex = 0;
			long time = System.currentTimeMillis();
			Log.println("Started fetching chapters");
			double tempTime = System.currentTimeMillis();
			for (String[] chapterInfo : chaptersInfo) {
				chapters.add(new Chapter(this, zos, chapterInfo[1], ++chapterIndex, chapterInfo[0], content, toc));
				if (chapterIndex%500==0) {
					for (Chapter chapter : chapters) {
						if (chapter.thread.isAlive()) {
							chapter.join();
						}
					}
					Log.println(chapterIndex+" chapters are fetched");
					double timeNow = System.currentTimeMillis();
					double timeElapsed = (timeNow - tempTime) / 1000;
					tempTime = timeNow;
					Log.println("Time elapsed: " + timeElapsed + "s");
				}
			}
			for (Chapter chapter : chapters) {
				if (chapter.thread.isAlive()) {
					chapter.join();
				}
				toc.addNavPoint(chapter);
				content.addToManifestAndSpine(chapter);
			}
			Log.println("All "+chapterIndex+" chapters are fetched");
			double timeElapsed = ((double) System.currentTimeMillis() - (double) time) / 1000;
			Log.println("Time elapsed: " + timeElapsed + "s");
			toc.fill().addToZip(zos);
			content.fill().addToZip(zos);
		} catch (IOException e) {
			Log.println("[!] Cannot create the book: " + title);
			e.printStackTrace(Log.stream());
		}
		if (new EpubCheck(epubFile).validate()) {
			Log.println("[Valdidation] Success!");
		} else {
			Log.println("[Valdidation] Failure!");
		}
	}

	public List<String> description() {
		return description;
	}

	public String[] innerFoldersPaths() {
		return innerFoldersPaths;
	}

	public String innerFolderPath(int index) {
		return innerFoldersPaths[index];
	}

	public String urlNovelUpdates() {
		return urlNovelUpdates;
	}

	public String path() {
		return path;
	}

	public String encoding() {
		return encoding;
	}

	public Charset encodingCharset() {
		return encodingCharset;
	}

	public String title() {
		return title;
	}

	public String author() {
		return author;
	}

	public String bookID() {
		return bookID;
	}

	public String timeOfCreation() {
		return timeOfCreation;
	}

	public String dateOfCreation() {
		return dateOfCreation;
	}

	public String coverLink() {
		return coverLink;
	}

	public List<String> tags() {
		return tags;
	}

	public String tag(int index) {
		return tags.get(index);
	}

	public List<String[]> genres() {
		return genres;
	}

	public String[] genre(int index) {
		return genres.get(index);
	}

	public String genre(int index, int type) {
		return genres.get(index)[type];
	}

	public String storyType(int index) {
		return storyType[index];
	}

	public Map<String, String> cookies() {
		return cookies;
	}

	public String urlWuxiaWorld() {
		return urlWuxiaWorld;
	}
}
