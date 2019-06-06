package com.xiaoniang.epub.innerfiles;

import com.xiaoniang.epub.api.EpubBook;
import com.xiaoniang.epub.api.InnerFile;

final class TocNCX extends InnerFile {
    
    TocNCX(EpubBook epubBook, int start, int end, int volume){
	setInnerPath(epubBook.innerFolderPath(0) + "toc.ncx");
	setEpubBook(epubBook);
	addContent("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\" ?>\r\n");
	addContent("<!DOCTYPE ncx PUBLIC \"-//NISO//DTD ncx 2005-1//EN\"\r\n");
	addContent(" \"http://www.daisy.org/z3986/2005/ncx-2005-1.dtd\">\r\n");
	addContent("<ncx xmlns=\"http://www.daisy.org/z3986/2005/ncx/\" version=\"2005-1\">\r\n");
	addContent("<head>\r\n");
	addContent("    <meta content=\""+epubBook.bookID()+epubBook.title()+epubBook.volumeTitle(volume)+"\" name=\"dtb:uid\"/>\r\n");
	addContent("    <meta content=\"1\" name=\"dtb:depth\"/>\r\n");
	addContent("    <meta content=\"0\" name=\"dtb:totalPageCount\"/>\r\n");
	addContent("    <meta content=\"0\" name=\"dtb:maxPageNumber\"/>\r\n");
	addContent("  </head>\r\n");
	addContent("  <docTitle>\r\n");
	addContent("    <text>"+epubBook.title()+epubBook.volumeTitle(volume)+"</text>\r\n");
	addContent("  </docTitle>\r\n");
	addContent("  <navMap>\r\n");
	addContent("    <navPoint id=\"navPoint-" + 1 + "\" playOrder=\"" + 1 + "\">\r\n");
	addContent("      <navLabel>\r\n");
	addContent("        <text>Cover</text>\r\n");
	addContent("      </navLabel>\r\n");
	addContent("      <content src=\"Text/cover.xhtml\"/>\r\n");
	addContent("    </navPoint>\r\n");
	addContent("    <navPoint id=\"navPoint-" + 2 + "\" playOrder=\"" + 2 + "\">\r\n");
	addContent("      <navLabel>\r\n");
	addContent("        <text>Description</text>\r\n");
	addContent("      </navLabel>\r\n");
	addContent("      <content src=\"Text/description.xhtml\"/>\r\n");
	addContent("    </navPoint>\r\n");
	for (int i = start, j = 3; i <= end; i++, j++) {
		String chapterFileIndex = ""+i;
		while (chapterFileIndex.length()<4) {
		    chapterFileIndex = "0" + chapterFileIndex;
		}
		addContent("    <navPoint id=\"navPoint-" + j + "\" playOrder=\"" + j + "\">\r\n");
		addContent("      <navLabel>\r\n");
		addContent("        <text>" + escapeHtml(epubBook.chapterTitle(volume, j-2)) + "</text>\r\n");
		addContent("      </navLabel>\r\n");
		addContent("      <content src=\"Text/chapter_" + chapterFileIndex + ".xhtml\"/>\r\n");
		addContent("    </navPoint>\r\n");
	    }
	addContent("  </navMap>\r\n");
	addContent("</ncx>");
    }

}
