package com.xiaoniang.epub;

import java.io.File;

final class TocNCX extends InnerFiles {
    
    TocNCX(EpubBook epubBook, int start, int end){
	setInnerPath(epubBook.innerFolderPath(0) + "toc.ncx");
	setFile(new File(epubBook.tempPath()+innerPath()));
	setEpubBook(epubBook);
	addContent("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\" ?>\r\n");
	addContent("<!DOCTYPE ncx PUBLIC \"-//NISO//DTD ncx 2005-1//EN\"\r\n");
	addContent(" \"http://www.daisy.org/z3986/2005/ncx-2005-1.dtd\">\r\n");
	addContent("<ncx xmlns=\"http://www.daisy.org/z3986/2005/ncx/\" version=\"2005-1\">\r\n");
	addContent("<head>\r\n");
	addContent("    <meta content=\""+epubBook.bookID()+"\" name=\"dtb:uid\"/>\r\n");
	addContent("    <meta content=\"1\" name=\"dtb:depth\"/>\r\n");
	addContent("    <meta content=\"0\" name=\"dtb:totalPageCount\"/>\r\n");
	addContent("    <meta content=\"0\" name=\"dtb:maxPageNumber\"/>\r\n");
	addContent("  </head>\r\n");
	addContent("  <docTitle>\r\n");
	addContent("    <text>"+epubBook.title()+"</text>\r\n");
	addContent("  </docTitle>\r\n");
	addContent("  <navMap>\r\n");
	for (int i = start; i <= end; i++) {
		String chapterFileIndex = ""+i;
		while (chapterFileIndex.length()<4) {
		    chapterFileIndex = "0" + chapterFileIndex;
		}
		addContent("    <navPoint id=\"navPoint-" + i + "\" playOrder=\"" + i + "\">\r\n");
		addContent("      <navLabel>\r\n");
		addContent("        <text>Chapter " + i + "</text>\r\n");
		addContent("      </navLabel>\r\n");
		addContent("      <content src=\"Text/chapter_" + chapterFileIndex + ".xhtml\"/>\r\n");
		addContent("    </navPoint>\r\n");
	    }
	addContent("  </navMap>\r\n");
	addContent("</ncx>");
    }

}
