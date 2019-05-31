package com.xiaoniang.epub;

import java.io.File;

final class Mimetype extends InnerFiles {

	Mimetype(EpubBook epubBook) {
	    setEpubBook(epubBook); 
	    setInnerPath("mimetype");
	    setFile(new File(epubBook.tempPath() + innerPath()));
	    addContent("application/epub+zip");
	}
	
}
