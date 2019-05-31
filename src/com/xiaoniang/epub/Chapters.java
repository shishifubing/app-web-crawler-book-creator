package com.xiaoniang.epub;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

class Chapters {

    static void download(EpubBook epubBook, int targetVolume) {
	Elements volumes = epubBook.frontPage().select("div.panel-group > *");
	int volumeIndex = 1;
	int chapterIndex = 1;
	int chapterIndexStart = 1;
	for (Element volumeChapters : volumes) {
	    Elements chapterLinks = volumeChapters.select("li.chapter-item > a");
	    epubBook.addVolumeToChapterFiles(chapterLinks.size());
	    if (chapterLinks.isEmpty()) {
		continue;
	    }
	    if (targetVolume != 0 && volumeIndex != targetVolume) {
		volumeIndex++;
		chapterIndex += chapterLinks.size();
	    } else {
		if (targetVolume!= 0) {
		    chapterIndexStart = chapterIndex;
		}
		for (Element chapterLink : chapterLinks) {
		    Chapter chapter = new Chapter(epubBook, chapterLink.attr("abs:href"), chapterIndex++);
		    chapter.setVolume(volumeIndex);
		    while (!chapter.fill())
			;
		    if (chapterIndex >= 5)
			break;
		}
	    }
	}
	TocNCX toc = new TocNCX(epubBook, chapterIndexStart, chapterIndex);
	toc.fill();
	ContentOPF content = new ContentOPF(epubBook, chapterIndexStart, chapterIndex);
	content.fill();
    }

}
