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
import java.util.ListIterator;
import java.util.Map;
import java.util.zip.ZipOutputStream;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.adobe.epubcheck.api.EpubCheck;
import com.xiaoniang.epub.innerfiles.Chapter;
import com.xiaoniang.epub.innerfiles.Container;
import com.xiaoniang.epub.innerfiles.CoverSrc;
import com.xiaoniang.epub.innerfiles.Cover;
import com.xiaoniang.epub.innerfiles.Description;
import com.xiaoniang.epub.innerfiles.Mimetype;
import com.xiaoniang.epub.innerfiles.Stylesheet;
import com.xiaoniang.epub.resources.Log;

public class EpubBook {

	private final List<String[]> genres;
	private final List<String> chapterLinks;
	private final List<String> tags;
	private final List<String> description;
	private final String[] storyType;
	private final String[] innerFoldersPaths = { "OEBPS" + File.separator, "META-INF" + File.separator,
			"OEBPS" + File.separator + "Styles" + File.separator, "OEBPS" + File.separator + "Text" + File.separator,
			"OEBPS" + File.separator + "Images" + File.separator };
	private final String urlNovelUpdates;
	private final String coverLink;
	private Map<String, String> cookies;
	private final Document novelUpdatesPage;
	private final String path;
	private final String encoding;
	private final Charset encodingCharset;
	private final String title;
	private final String author;
	private final String bookID;
	private final String timeOfCreation;
	private final String dateOfCreation;
	private ZipOutputStream zos;

	public EpubBook(String outputPath, String link) throws IOException {
		if (link.endsWith("/")) {
			urlNovelUpdates = link;
		} else {
			urlNovelUpdates = link + "/";
		}
		path = outputPath;
		chapterLinks = new ArrayList<String>();
		Document novelUpdatesPageDocument = null;
		while (novelUpdatesPageDocument == null) {
			try {
				cookies = Jsoup.connect(urlNovelUpdates).timeout(10000).execute().cookies();
				novelUpdatesPageDocument = Jsoup.connect(urlNovelUpdates).cookies(cookies).timeout(10000).get();
			} catch (IOException e) {
				Log.println("[!] Cannot connect to the " + urlNovelUpdates);
				//e.printStackTrace(Log.writer());
			}
		}
		novelUpdatesPage = novelUpdatesPageDocument;
		title = novelUpdatesPage.select("div.w-blog-content > div.seriestitlenu").first().text();
		Log.println("Title: " + title);
		coverLink = novelUpdatesPage.select("div.seriesimg > *").first().attr("src");
		Elements descriptionElements = novelUpdatesPage.select("div#editdescription > p");
		description = new ArrayList<String>(descriptionElements.size());
		for (Element line : novelUpdatesPage.select("div#editdescription > p")) {
			description.add(InnerFile.escapeAllHtml(line.text()));
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
		Elements chapterNavigationPages = novelUpdatesPage.select("div.digg_pagination > a");
		int chapterNavigationPagesAmount = 0;
		for (Element chapterNavigationPage : chapterNavigationPages) {
			String maybeNumber = chapterNavigationPage.text().replaceAll("[^0-9]", "");
			if (maybeNumber.isEmpty() || maybeNumber == null) {
				continue;
			}
			int index = Integer.parseInt(chapterNavigationPage.text());
			if (index > chapterNavigationPagesAmount) {
				chapterNavigationPagesAmount = index;
			}
		}
		for (int i = chapterNavigationPagesAmount; i > 0; i--) {
			String navigationPageUrl = urlNovelUpdates;
			if (i != 1) {
				navigationPageUrl += "?pg=" + i;
			}
			Log.println(navigationPageUrl);
			Document chapterNavigationPageDocument = null;
			while (chapterNavigationPageDocument == null) {
				try {
					chapterNavigationPageDocument = Jsoup.connect(navigationPageUrl).timeout(10000).get();
				} catch (IOException e) {
					Log.println("   [!] Cannot connect to the " + navigationPageUrl);
					//e.printStackTrace(Log.writer());
				}
				Elements chapterElements = novelUpdatesPageDocument.select("table#myTable > tbody > tr");
				ListIterator<Element> chapterElementsIterator = chapterElements.listIterator(chapterElements.size());
				while (chapterElementsIterator.hasPrevious()) {
					Element chapterElement = chapterElementsIterator.previous();
					chapterLinks.add(chapterElement.select("td:eq(2) > a").attr("abs:href"));
					Log.println("   "+chapterElement.select("td:eq(2) > a").attr("title"));
				}
			}
		}
		dateOfCreation = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
		timeOfCreation = LocalTime.now().format(DateTimeFormatter.ofPattern("HH-mm-ss"));
		bookID = "WuxiaWorld.com-" + "XiaoNiang-" + dateOfCreation + "-";
		encodingCharset = StandardCharsets.UTF_8;
		encoding = encodingCharset.name();
		//Log.println(chapterLinks);
	}

	public void create() {
		File epubFile = new File(path() + title() + ".epub");
		int duplicateIndex = 0;
		while (epubFile.exists()) {
			epubFile = new File(path() + title() + " (" + ++duplicateIndex + ")" + ".epub");
		}
		try (ZipOutputStream zos = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(epubFile)),
				encodingCharset())) {
			this.zos = zos;
			new Mimetype(this).addToZip();
			new Container(this).addToZip();
			CoverSrc coverSrc = new CoverSrc(this);
			coverSrc.addToZip();
			new Cover(this, coverSrc).addToZip();
			new Description(this).addToZip();
			new Stylesheet(this).addToZip();
			int index = 0;
			for (String chapterLink : chapterLinks) {
				new Chapter(this, chapterLink, ++index);
			}
		} catch (IOException e) {
			Log.println("   [!] Cannot create book: " + title);
			e.printStackTrace(Log.writer());
		} finally {
			zos = null;
		}
		EpubCheck check = new EpubCheck(epubFile);
		if (check.validate()) {
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

	public List<String> chapterLinks() {
		return chapterLinks;
	}

	public String chapterLink(int index) {
		return chapterLinks.get(index);
	}

	public Map<String, String> cookies() {
		return cookies;
	}

	public ZipOutputStream zos() {
		return zos;
	}
}
