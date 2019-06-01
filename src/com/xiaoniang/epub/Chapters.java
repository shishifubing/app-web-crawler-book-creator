package com.xiaoniang.epub;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

class Chapters {

    static void download(EpubBook epubBook, int targetVolume) {
	Elements volumes = epubBook.frontPage().select("div.panel-group > *");
	int volumeIndex = 0;
	int chapterIndex = 1;
	int chapterIndexStart = 1;
	for (Element volumeChapters : volumes) {
	    Elements chaptersLinks = volumeChapters.select("li.chapter-item > a");
	    epubBook.addVolumeToChapterFiles(chaptersLinks.size());
	    volumeIndex++;
	    if (chaptersLinks.isEmpty()) {
		continue;
	    }
	    if (targetVolume != 0 && volumeIndex != targetVolume) {
		chapterIndex += chaptersLinks.size();
	    } else {
		if (targetVolume != 0) {
		    chapterIndexStart = chapterIndex;
		}
		for (Element chapterLink : chaptersLinks) {
		    Chapter chapter = new Chapter(epubBook, chapterLink.attr("abs:href"), chapterIndex++);
		    chapter.setVolume(volumeIndex);
		    while (!chapter.fill())
			;
		}
		if (targetVolume != 0) break;
	    }
	}
	TocNCX toc = new TocNCX(epubBook, chapterIndexStart, chapterIndex-1, targetVolume);
	toc.fill();
	ContentOPF content = new ContentOPF(epubBook, chapterIndexStart, chapterIndex - 1, targetVolume);
	content.fill();
    }

}
