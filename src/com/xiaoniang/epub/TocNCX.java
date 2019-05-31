package com.xiaoniang.epub;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

final class TocNCX extends InnerFiles {
    
    private List<String> content = new ArrayList<String>();
    
    TocNCX(EpubBook epubBook, int start, int end){
	setInnerPath(epubBook.innerFolderPath(0) + "toc.ncx");
	setFile(new File(epubBook.tempPath()+innerPath()));
	setEpubBook(epubBook);
	content.add("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\" ?>\r\n");
	content.add("<!DOCTYPE ncx PUBLIC \"-//NISO//DTD ncx 2005-1//EN\"\r\n");
	content.add(" \"http://www.daisy.org/z3986/2005/ncx-2005-1.dtd\">\r\n");
	content.add("<ncx xmlns=\"http://www.daisy.org/z3986/2005/ncx/\" version=\"2005-1\">\r\n");
	content.add("<head>\r\n");
	content.add("    <meta content=\""+epubBook.bookID()+"\" name=\"dtb:uid\"/>\r\n");
	content.add("    <meta content=\"1\" name=\"dtb:depth\"/>\r\n");
	content.add("    <meta content=\"0\" name=\"dtb:totalPageCount\"/>\r\n");
	content.add("    <meta content=\"0\" name=\"dtb:maxPageNumber\"/>\r\n");
	content.add("  </head>\r\n");
	content.add("  <docTitle>\r\n");
	content.add("    <text>"+epubBook.title()+"</text>\r\n");
	content.add("  </docTitle>\r\n");
	content.add("  <navMap>\r\n");
	for (int i = start; i <= end; i++) {
		String chapterFileIndex = ""+i;
		while (chapterFileIndex.length()<(""+end).length()) {
		    chapterFileIndex = "0" + chapterFileIndex;
		}
		content.add("    <navPoint id=\"navPoint-" + i + "\" playOrder=\"" + i + "\">\r\n");
		content.add("      <navLabel>\r\n");
		content.add("        <text>Chapter " + i + "</text>\r\n");
		content.add("      </navLabel>\r\n");
		content.add("      <content src=\"Text/chapter_" + chapterFileIndex + ".xhtml\"/>\r\n");
		content.add("    </navPoint>\r\n");
	    }
	content.add("  </navMap>\r\n");
	content.add("</ncx>");
    }

}
