package com.xiaoniang.epub;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

class Chapters {

    static void download(EpubBook epubBook) {
	Elements volumes = epubBook.frontPage().select("div.panel-group > *");
	Elements allChapters = epubBook.frontPage().select("li.chapter-item > a");
	int volumeIndex = 0;
	int[] volumeChaptersAmounts = new int[volumes.size()];
	for (Element volumeChapters : volumes) {
	    Elements chapterLinks = volumeChapters.select("li.chapter-item > a");
	    if (chapterLinks.isEmpty()) {
		continue;
	    }
	    volumeChaptersAmounts[volumeIndex++] = chapterLinks.size();
	}
	int index = 0;
	for (Element chapterLink : allChapters) {
	    	Chapter chapter = new Chapter(epubBook, chapterLink.attr("abs:href"), ++index);
	    	while (!chapter.fill());
	    	if (index>5) break;
	}
	TocNCX toc = new TocNCX(epubBook, 1, index);
	toc.fill();
	ContentOPF content = new ContentOPF(epubBook, 1, index);
	content.fill();
    }


}
