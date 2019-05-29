package com.xiaoniang.epub;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

final class TocNCX extends InnerFiles {
    
    private List<String> content = new ArrayList<String>();

    final String innerPath;
    
    TocNCX(EpubBook epubFile){
	innerPath = epubFile.getInnerFoldersPath(1)+ "toc.ncx";
	content.add("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\" ?>\r\n");
	content.add("<!DOCTYPE ncx PUBLIC \"-//NISO//DTD ncx 2005-1//EN\"\r\n");
	content.add(" \"http://www.daisy.org/z3986/2005/ncx-2005-1.dtd\">\r\n");
	content.add("<ncx xmlns=\"http://www.daisy.org/z3986/2005/ncx/\" version=\"2005-1\">\r\n");
	content.add("<head>\r\n");
	content.add("    <meta content=\"");
	content.add(epubFile.getBookID());
	content.add("\" name=\"dtb:uid\"/>\r\n");
	content.add("    <meta content=\"1\" name=\"dtb:depth\"/>\r\n");
	content.add("    <meta content=\"0\" name=\"dtb:totalPageCount\"/>\r\n");
	content.add("    <meta content=\"0\" name=\"dtb:maxPageNumber\"/>\r\n");
	content.add("  </head>\r\n");
	content.add("  <docTitle>\r\n");
	content.add("    <text>");
	content.add(epubFile.getTitle());
	content.add("</text>\r\n");
	content.add("  </docTitle>\r\n");
	content.add("  <navMap>\r\n");
	content.add("    <navPoint id=\"navPoint-");//18
	content.add("\" playOrder=\"");
	content.add("\">\r\n");
	content.add("      <navLabel>\r\n");
	content.add("        <text>Chapter ");
	content.add("</text>\r\n");//23
	content.add("      </navLabel>\r\n");
	content.add("      <content src=\"Text/chapter_");
	content.add(".xhtml\"/>\r\n");
	content.add("    </navPoint>\r\n");
	content.add("  </navMap>\r\n");
	content.add("</ncx>");
    }
    
    protected void fill(EpubBook epubFile, int start, int end) {
	try (PrintWriter writer = new PrintWriter(innerPath, epubFile.getEncoding())) {
	    for (int i=0; i<=16; i++) {
		writer.print(content.get(i));
	    }
	    for (int i = start; i <= end; i++) {
		String chapterFileIndex = ""+i;
		while (chapterFileIndex.length()<(""+end).length()) {
		    chapterFileIndex = "0" + chapterFileIndex;
		}
		writer.print(content.get(18) + i + content.get(19) + i + content.get(20));
		writer.print(content.get(21));
		writer.print(content.get(22) + i + content.get(23));
		writer.print(content.get(24));
		writer.print(content.get(25) + chapterFileIndex + content.get(26));
		writer.print(content.get(27));
	    }
	    writer.print(content.get(28));
	    writer.print(content.get(29));
	    } catch (FileNotFoundException | UnsupportedEncodingException e) {
		System.out.println("[!] Couldn't create toc.ncx");
	    }
	    epubFile.addToSupportFilesPaths(epubFile.getPath()+innerPath);
	    System.out.println("[Created] Toc.ncx");
    }

}
