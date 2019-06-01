package com.xiaoniang.epub;

import java.io.File;

import com.xiaoniang.epub.api.EpubBook;
import com.xiaoniang.epub.api.InnerFiles;

public final class Mimetype extends InnerFiles {

	public Mimetype(EpubBook epubBook) {
	    setEpubBook(epubBook); 
	    setInnerPath("mimetype");
	    setFile(new File(epubBook.tempPath() + innerPath()));
	    addContent("application/epub+zip");
	}
	
}
