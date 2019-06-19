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
import com.xiaoniang.epub.resources.Log;

public class EpubBook {

	private final List<String[]> genres;
	private final List<Chapter> chapters;
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
	private int chapterNavigationPagesAmount;

	public EpubBook(String outputPath, String link) throws IOException {
		if (link.endsWith("/")) {
			urlNovelUpdates = link;
		} else {
			urlNovelUpdates = link + "/";
		}
		path = outputPath;
		Document novelUpdatesPageDocument = null;
		while (novelUpdatesPageDocument == null) {
			try {
				Response response = Jsoup.connect(urlNovelUpdates).userAgent(
						"Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/535.21 (KHTML, like Gecko) Chrome/19.0.1042.0 Safari/535.21")
						.timeout(10000).execute();
				cookies = response.cookies();
				novelUpdatesPageDocument = response.parse();
			} catch (IOException e) {
				Log.println("[!] Cannot connect to the " + urlNovelUpdates);
			}
		}
		Log.println("Connected");
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
		Log.println("Got meta info");
		Elements chapterNavigationPages = novelUpdatesPage.select("div.digg_pagination > a");
		chapterNavigationPagesAmount = 0;
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
		chapters = new ArrayList<Chapter>(chapterNavigationPagesAmount * 15);
		Log.println("Found the number of pages: " + chapterNavigationPagesAmount);
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
			for (int i = chapterNavigationPagesAmount; i > 0; i--) {
				String navigationPageUrl = urlNovelUpdates + "?pg=" + i;
				Document chapterNavigationPage = null;
				while (chapterNavigationPage == null) {
					try {
						chapterNavigationPage = Jsoup.connect(navigationPageUrl).userAgent(
								"Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/535.21 (KHTML, like Gecko) Chrome/19.0.1042.0 Safari/535.21")
								.timeout(10000).cookies(cookies).get();
					} catch (IOException e) {
					}
				}
				Elements chapterElements = chapterNavigationPage.select("table#myTable > tbody > tr");
				ListIterator<Element> chapterElementsIterator = chapterElements.listIterator(chapterElements.size());
				while (chapterElementsIterator.hasPrevious()) {
					Element chapterElement = chapterElementsIterator.previous();
					String chapterLink = chapterElement.select("td:eq(2) > a").attr("href").replace("//", "https://");
					chapters.add(new Chapter(this, zos, chapterLink, ++chapterIndex, content, toc));
				}
			}
			for (Chapter chapter : chapters) {
				chapter.join();
			}
			toc.fill().addToZip(zos);
			content.fill().addToZip(zos);
		} catch (IOException e) {
			Log.println("   [!] Cannot create book: " + title);
			e.printStackTrace(Log.stream());
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

	public Map<String, String> cookies() {
		return cookies;
	}

}
