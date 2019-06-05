package com.xiaoniang.epub.api;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.xiaoniang.epub.innerfiles.ChapterXHTML;

public class EpubBook {

    private final List<ArrayList<String>> genres;
    private final List<String> tags;
    private final String description;
    private final String[] storyType;
    private final List<String> volumeTitles;
    private final String[] innerFoldersPaths = { "OEBPS" + File.separator, "META-INF" + File.separator,
	    "OEBPS" + File.separator + "Styles" + File.separator, "OEBPS" + File.separator + "Text" + File.separator,
	    "OEBPS" + File.separator + "Images" + File.separator };
    private final String urlWuxiaWorld;
    private final String urlNovelUpdates;
    private final String coverLink;
    private final Document wuxiaWorldPage;
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

    EpubBook(String outputPath, String link, int[] volumes) throws IOException {
	urlWuxiaWorld = link;
	urlNovelUpdates = "https://www.novelupdates.com/series/" + link.split("novel")[1].substring(1);
	path = outputPath;
	genres = new ArrayList<ArrayList<String>>();
	genres.add(new ArrayList<String>());
	genres.add(new ArrayList<String>());
	volumeTitles = new ArrayList<String>();
	tags = new ArrayList<String>();
	storyType = new String[2];
	Document wuxiaWorldPageDocument = null;
	Document novelUpdatesPageDocument = null;
	while (wuxiaWorldPageDocument == null) {
	    try {
		wuxiaWorldPageDocument = Jsoup.connect(urlWuxiaWorld).get();
	    } catch (IOException e) {
		System.out.println("[!] Cannot connect to the " + urlWuxiaWorld);
	    }
	}
	while (novelUpdatesPageDocument == null) {
	    try {
		novelUpdatesPageDocument = Jsoup.connect(urlNovelUpdates).get();
	    } catch (IOException e) {
		System.out.println("[!] Cannot connect to the " + urlNovelUpdates);
	    }
	}
	wuxiaWorldPage = wuxiaWorldPageDocument;
	novelUpdatesPage = novelUpdatesPageDocument;
	title = wuxiaWorldPage.select("div.p-15 > *").first().text();
	coverLink = novelUpdatesPage.select("div.seriesimg > *").first().attr("src");
	description = novelUpdatesPage.select("div#editdescription").text();
	author = novelUpdatesPage.select("div#showauthors > *").first().text();
	translator = wuxiaWorldPage.select("div.media-body > dl.dl-horizontal > dd").text();
	storyType[0] = novelUpdatesPage.select("div#showtype > *").first().text();
	storyType[1] = novelUpdatesPage.select("div#showtype > *").first().attr("href");
	Elements volumeTitlesElements = wuxiaWorldPage.select("div.panel-group").select("span.title");
	volumeTitles.add("");
	for (Element volumeTitle : volumeTitlesElements) {
	    volumeTitles.add(" " + volumeTitle.text());
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
	for (int volume : volumes) {
	    ChapterXHTML.downloadChapters(this, volume);
	}
    }

    public String volumeTitle(int index) {
	return volumeTitles.get(index);
    }

    public String description() {
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

    public Document frontPage() {
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
}
