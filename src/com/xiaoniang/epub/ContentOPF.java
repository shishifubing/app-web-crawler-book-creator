package com.xiaoniang.epub;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class ContentOPF {
    
    private List<String> content = new ArrayList<String>();
    
    ContentOPF (EpubBook epubFile) {
	content.add("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n");
	content.add("<package xmlns=\"http://www.idpf.org/2007/opf\" unique-identifier=\"BookId\" version=\"2.0\">\r\n");
	content.add("  <metadata xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xmlns:opf=\"http://www.idpf.org/2007/opf\">\r\n");
	content.add("    <dc:identifier id=\"BookId\" opf:scheme=\"XN\">");
	content.add(epubFile.getBookID());
	content.add("</dc:identifier>\r\n");
	content.add("    <dc:title>");
	content.add(epubFile.getTitle());
	content.add("</dc:title>\r\n");
	content.add("    <dc:creator opf:file-as=\"");
	content.add(epubFile.getAuthor());
	content.add("\" opf:role=\"aut\">");
	content.add(epubFile.getAuthor());
	content.add("</dc:creator>\r\n");
	content.add("    <dc:language>en</dc:language>\r\n");
	content.add("    <dc:date opf:event=\"creation\">");
	content.add(epubFile.getTimeOfCreation());
	content.add("</dc:date>\r\n");
	content.add("    <dc:publisher>WuxiaWorld.com</dc:publisher>\r\n");
	content.add("    <dc:rights>All materials' copyrights reserved by their respective authors and the associated publishers. Please respect their rights. Works will be deleted upon request by copyright holders.</dc:rights>\r\n");
	content.add("    <meta name=\"cover\" content=\"cover.jpg\" />\r\n");
	content.add("  </metadata>\r\n");
	content.add("  <manifest>\r\n");
	content.add("    <item href=\"toc.ncx\" id=\"ncx\" media-type=\"application/x-dtbncx+xml\" />\r\n");
	content.add("    <item href=\"Text/cover.xhtml\" id=\"cover.xhtml\" media-type=\"application/xhtml+xml\" />\r\n");
	content.add("    <item href=\"Styles/stylesheet.css\" id=\"stylesheet.css\" media-type=\"text/css\" />\r\n");
	content.add("  </manifest>\r\n");
	content.add("  <spine toc=\"ncx\">\r\n");
	content.add("    <itemref idref=\"cover.xhtml\" />\r\n");
	content.add("  </spine>\r\n");
	content.add("  <guide>\r\n");
	content.add("    <reference href=\"Text/cover.xhtml\" title=\"Cover\" type=\"cover\" />\r\n");
	content.add("  </guide>\r\n");
	content.add("</package>");
	content.add("    <item href=\"Text/chapter_");
	content.add(".xhtml\" id=\"Chapter_");
	content.add(".xhtml\" media-type=\"application/xhtml+xml\" />\r\n");
	content.add("    <itemref idref=\"Chapter_");
	content.add(".xhtml\" />\r\n");
	
    }
    
    protected void fill(EpubBook epubFile, int start, int end) {
	String innerPath = epubFile.getInnerFoldersPath(0)+"content.opf";
	File file = new File(innerPath);    
	try (PrintWriter writer = new PrintWriter(epubFile.getPath()+innerPath, epubFile.getEncoding())) {
	    writer.print(header+package_start+metadata_start+identifier
		    +title+creator+language+dateOfCreation+publisher+rights+metaName+metadata_end
		    +manifest_start+item_toc+item_cover
		    +item_stylesheet);
	    for (int i = start; i <= end; i++) {
		String chapterFileIndex = ""+i;
		while (chapterFileIndex.length()<(""+end).length()) {
		    chapterFileIndex = "0" + chapterFileIndex;
		}
		writer.print("    <item href=\"Text/chapter_" + chapterFileIndex + ".xhtml\" id=\"Chapter_" + chapterFileIndex
			+ ".xhtml\" media-type=\"application/xhtml+xml\" />\r\n");
	    }
	    writer.print(
		    manifest_end + spine_toc_start + itemref_cover);
	    for (int i = start; i <= end; i++) {
		String chapterFileIndex = ""+i;
		while (chapterFileIndex.length()<(""+end).length()) {
		    chapterFileIndex = "0" + chapterFileIndex;
		}
		writer.print("    <itemref idref=\"Chapter_" + chapterFileIndex + ".xhtml\" />\r\n");
	    }
	    writer.print(spine_toc_end + guide_start
		    + guide_coverRef
		    + guide_end + package_end);
	    } catch (IOException e) {
		System.out.println("[!] Couldn't create content.opf");
	    }
	    epubFile.addToSupportFilesPaths(epubFile.getPath()+innerPath);
	    System.out.println("[Created] content.opf");
    }

}
