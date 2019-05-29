package com.xiaoniang.epub;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

final class Toc_ncx {
    private static String path = "OEBPS/toc.ncx";
    private static String header = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\" ?>\r\n";
    private static String docType = "<!DOCTYPE ncx PUBLIC \"-//NISO//DTD ncx 2005-1//EN\"\r\n"
	    				+ " \"http://www.daisy.org/z3986/2005/ncx-2005-1.dtd\">\r\n";
    private static String ncx_start = "<ncx xmlns=\"http://www.daisy.org/z3986/2005/ncx/\" version=\"2005-1\">\r\n";
    private static String head_start = "<head>\r\n";
    private static String meta_id = "    <meta content=\""+CreateEpub.bookID+"\" name=\"dtb:uid\"/>\r\n";
    private static String meta_depth = "    <meta content=\"1\" name=\"dtb:depth\"/>\r\n";
    private static String meta_pageCount = "    <meta content=\"0\" name=\"dtb:totalPageCount\"/>\r\n";
    private static String meta_maxPageNumber = "    <meta content=\"0\" name=\"dtb:maxPageNumber\"/>\r\n";
    private static String head_end = "  </head>\r\n";
    private static String docTitle = "  <docTitle>\r\n"+ "    <text>"+CreateEpub.title+"</text>\r\n" + "  </docTitle>\r\n";
    private static String navMap_start = "  <navMap>\r\n";
    private static String navLabel_start = "      <navLabel>\r\n";
    private static String navLabel_end = "      </navLabel>\r\n";
    private static String navPoint_end = "    </navPoint>\r\n";
    private static String navMap_end = "  </navMap>\r\n";
    private static String ncx_end = "</ncx>";
    
    protected static void create(EpubFile epubFile, int start, int end) {
	    try (PrintWriter writer = new PrintWriter(epubFile.tempDir()+path, epubFile.encoding())) {
	    writer.print(header+docType+ncx_start+head_start
		    +meta_id+meta_depth+meta_pageCount
		    +meta_maxPageNumber+head_end+docTitle+navMap_start);
	    for (int i = start; i <= end; i++) {
		String chapterFileIndex = ""+i;
		while (chapterFileIndex.length()<(""+end).length()) {
		    chapterFileIndex = "0" + chapterFileIndex;
		}
		writer.print(
			"    <navPoint id=\"navPoint-" + i + "\" playOrder=\"" + i + "\">\r\n" + navLabel_start
				+ "        <text>Chapter " + i + "</text>\r\n" + navLabel_end
				+ "      <content src=\"Text/chapter_" + chapterFileIndex + ".xhtml\"/>\r\n" + navPoint_end);
	    }
	    writer.print(navMap_end+ncx_end);
	    } catch (FileNotFoundException | UnsupportedEncodingException e) {
		System.out.println("[!] Couldn't create toc.ncx");
	    }
	    epubFile.addToSupportFiles(epubFile.outputPath()+path);
	    System.out.println("[Created] Toc.ncx");
    }

}
