package com.xiaoniang.epub.innerfiles;

import com.xiaoniang.epub.api.EpubBook;
import com.xiaoniang.epub.api.InnerFile;

public final class Mimetype extends InnerFile {

	public Mimetype(EpubBook epubBook) {
	    setEpubBook(epubBook); 
	    setInnerPath("mimetype");
	    addContent("application/epub+zip");
	}
	
}
