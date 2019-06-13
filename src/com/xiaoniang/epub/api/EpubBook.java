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
import com.xiaoniang.epub.resources.LinkPairs;

public class EpubBook {

	private final List<ArrayList<String>> genres;
	private final List<ArrayList<String[]>> chapterLinks;
	private final List<String> tags;
	private final List<String> description;
	private final String[] storyType;
	private final List<String> volumeTitles;
	private final String[] innerFoldersPaths = { "OEBPS" + File.separator, "META-INF" + File.separator,
			"OEBPS" + File.separator + "Styles" + File.separator, "OEBPS" + File.separator + "Text" + File.separator,
			"OEBPS" + File.separator + "Images" + File.separator };
	private final String urlWuxiaWorld;
	private final String urlNovelUpdates;
	private final String coverLink;
	private final Document wuxiaWorldPage;
	private Map<String, String> cookies;
	private final Document novelUpdatesPage;
	private final String path;
	private final String encoding;
	private final Charset encodingCharset;
	private final String title;
	private final String author;
	private final String translator;
	private final String bookID;
	private final String timeOfCreation;
	private final String dateOfCreation;

	public EpubBook(String outputPath, String link) throws IOException {
		urlWuxiaWorld = link;
		urlNovelUpdates = LinkPairs.link(link);
		path = outputPath;
		genres = new ArrayList<ArrayList<String>>();
		genres.add(new ArrayList<String>());
		genres.add(new ArrayList<String>());
		chapterLinks = new ArrayList<ArrayList<String[]>>();
		volumeTitles = new ArrayList<String>();
		description = new ArrayList<String>();
		tags = new ArrayList<String>();
		storyType = new String[2];
		Document wuxiaWorldPageDocument = null;
		Document novelUpdatesPageDocument = null;
		while (wuxiaWorldPageDocument == null) {
			try {
				cookies = Jsoup.connect(urlWuxiaWorld).timeout(10000).execute().cookies();
				wuxiaWorldPageDocument = Jsoup.connect(urlWuxiaWorld).timeout(10000).cookies(cookies).get();
			} catch (IOException e) {
				System.out.println("[!] Cannot connect to the " + urlWuxiaWorld);
			}
		}
		while (novelUpdatesPageDocument == null) {
			try {
				novelUpdatesPageDocument = Jsoup.connect(urlNovelUpdates).timeout(10000).get();
			} catch (IOException e) {
				System.out.println("[!] Cannot connect to the " + urlNovelUpdates);
			}
		}
		wuxiaWorldPage = wuxiaWorldPageDocument;
		novelUpdatesPage = novelUpdatesPageDocument;
		title = wuxiaWorldPage.select("div.p-15 > *").first().text();
		coverLink = novelUpdatesPage.select("div.seriesimg > *").first().attr("src");
		for (Element line : novelUpdatesPage.select("div#editdescription > p")) {
			description.add(InnerFile.escapeAllHtml(line.text()));
		}
		author = novelUpdatesPage.select("div#showauthors > *").first().text();
		translator = wuxiaWorldPage.select("div.media-body > dl.dl-horizontal > dd").text();
		storyType[0] = novelUpdatesPage.select("div#showtype > *").first().text();
		storyType[1] = novelUpdatesPage.select("div#showtype > *").first().attr("href");
		volumeTitles.add("");
		for (Element volumeTitle : wuxiaWorldPage.select("div.panel-group").select("span.title")) {
			volumeTitles.add(" " + volumeTitle.text());
		}
		for (Element volume : wuxiaWorldPage.select("div.panel-group > *")) {
			Elements chapterLinkElements = volume.select("li.chapter-item > a");
			ArrayList<String[]> chapterLinksTemp = new ArrayList<String[]>(chapterLinks.size());
			for (Element chapterLink : chapterLinkElements) {
				chapterLinksTemp.add(new String[] { chapterLink.select("span").text(), chapterLink.attr("abs:href") });
			}
			chapterLinks.add(chapterLinksTemp);
		}
		for (Element genre : novelUpdatesPage.select("div#seriesgenre > *")) {
			genres.get(0).add(genre.text());
			genres.get(1).add("<a href=\"" + genre.attr("href") + "\">" + genre.text() + "</a>");
		}
		for (Element tag : novelUpdatesPage.select("div#showtags > *")) {
			tags.add("<a href=\"" + tag.attr("href") + "\">" + tag.text() + "</a>");
		}
		dateOfCreation = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
		timeOfCreation = LocalTime.now().format(DateTimeFormatter.ofPattern("HH-mm-ss"));
		bookID = "WuxiaWorld.com-" + "XiaoNiang-" + dateOfCreation + "-";
		encodingCharset = StandardCharsets.UTF_8;
		encoding = encodingCharset.name();
	}

	public void create(int[] volumes) {
		for (int volume : volumes) {
			File epubFile = new File(path() + title() + volumeTitle(volume) + ".epub");
			int duplicateIndex = 1;
			while (epubFile.exists()) {
				epubFile = new File(path() + title() + volumeTitle(volume) + " (" + duplicateIndex++ + ")" + ".epub");
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
				Chapter.downloadChapters(this, volume, zos);
			} catch (IOException e) {
				System.out.println("[!] Cannot create volume " + volume);
			}
			EpubCheck check = new EpubCheck(epubFile);
			if (check.validate()) {
				System.out.println("[Valdidation] Success!");
			} else {
				System.out.println("[Valdidation] Failure!");
			}
		}
	}

	public String volumeTitle(int index) {
		return volumeTitles.get(index);
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

	public String urlWuxiaWorld() {
		return urlWuxiaWorld;
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

	public Document wuxiaWorldPage() {
		return wuxiaWorldPage;
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

	public List<ArrayList<String>> genres() {
		return genres;
	}

	public List<String> genres(int type) {
		return genres.get(type);
	}

	public String genre(int type, int index) {
		return genres.get(type).get(index);
	}

	public String storyType(int index) {
		return storyType[index];
	}

	public String translator() {
		return translator;
	}

	public List<ArrayList<String[]>> chapterLinks() {
		return chapterLinks;
	}

	public List<String[]> chapterLinksVolume(int index) {
		return chapterLinks.get(index - 1);
	}

	public String chapterLink(int volume, int chapter, int type) {
		return chapterLinks.get(volume - 1).get(chapter - 1)[type];
	}

	public Map<String, String> cookies() {
		return cookies;
	}
}
