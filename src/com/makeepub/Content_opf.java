package com.makeepub;

import java.io.IOException;
import java.io.PrintWriter;

public class Content_opf extends CreateEpub {
    private static String path = "OEBPS/content.opf";
    private static String header = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n";
    private static String package_start = "<package xmlns=\"http://www.idpf.org/2007/opf\" unique-identifier=\"BookId\" version=\"2.0\">\r\n";
    private static String metadata_start = "  <metadata xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xmlns:opf=\"http://www.idpf.org/2007/opf\">\r\n";
    private static String identifier = "    <dc:identifier id=\"BookId\" opf:scheme=\"XN\">"+CreateEpub.bookID+"</dc:identifier>\r\n";
    private static String title = "    <dc:title>"+CreateEpub.title+"</dc:title>\r\n";
    private static String creator = "    <dc:creator opf:file-as=\""+CreateEpub.author+"\" opf:role=\"aut\">"+CreateEpub.author+"</dc:creator>\r\n";
    private static String language = "    <dc:language>en</dc:language>\r\n";
    private static String dateOfCreation = "    <dc:date opf:event=\"creation\">"+CreateEpub.timeNow+"</dc:date>\r\n"; 
    private static String publisher = "    <dc:publisher>WuxiaWorld.com</dc:publisher>\r\n";
    private static String rights = "    <dc:rights>All materials' copyrights reserved by their respective authors and the associated publishers. Please respect their rights. Works will be deleted upon request by copyright holders.</dc:rights>\r\n";
    private static String metaName = "    <meta name=\"cover\" content=\"cover.jpg\" />\r\n";
    private static String metadata_end = "  </metadata>\r\n";
    private static String manifest_start = "  <manifest>\r\n";
    private static String item_toc = "    <item href=\"toc.ncx\" id=\"ncx\" media-type=\"application/x-dtbncx+xml\" />\r\n";
    private static String item_cover = "    <item href=\"Text/cover.xhtml\" id=\"cover.xhtml\" media-type=\"application/xhtml+xml\" />\r\n";
    private static String item_stylesheet = "    <item href=\"Styles/stylesheet.css\" id=\"stylesheet.css\" media-type=\"text/css\" />\r\n"; 
    private static String manifest_end = "  </manifest>\r\n";
    private static String spine_toc_start = "  <spine toc=\"ncx\">\r\n";
    private static String itemref_cover = "    <itemref idref=\"cover.xhtml\" />\r\n";
    private static String spine_toc_end = "  </spine>\r\n";
    private static String guide_start = "  <guide>\r\n";
    private static String guide_coverRef = "    <reference href=\"Text/cover.xhtml\" title=\"Cover\" type=\"cover\" />\r\n";
    private static String guide_end = "  </guide>\r\n";
    private static String package_end = "</package>";
    
    protected static void create(int index) throws IOException {
	    PrintWriter writer = new PrintWriter(CreateEpub.path+path, encoding);
	    writer.print(header+package_start+metadata_start+identifier
		    +title+creator+language+dateOfCreation+publisher+rights+metaName+metadata_end
		    +manifest_start+item_toc+item_cover
		    +item_stylesheet);
	    for (int i = 1; i <= index; i++) {
		String chapterFileIndex = ""+i;
		while (chapterFileIndex.length()<(""+CreateEpub.lastChapterIndex).length()) {
		    chapterFileIndex = "0" + chapterFileIndex;
		}
		writer.print("    <item href=\"Text/chapter_" + chapterFileIndex + ".xhtml\" id=\"Chapter_" + chapterFileIndex
			+ ".xhtml\" media-type=\"application/xhtml+xml\" />\r\n");
	    }
	    writer.print(
		    manifest_end + spine_toc_start + itemref_cover);
	    for (int i = 1; i <= index; i++) {
		String chapterFileIndex = ""+i;
		while (chapterFileIndex.length()<(""+CreateEpub.lastChapterIndex).length()) {
		    chapterFileIndex = "0" + chapterFileIndex;
		}
		writer.print("    <itemref idref=\"Chapter_" + chapterFileIndex + ".xhtml\" />\r\n");
	    }
	    writer.print(spine_toc_end + guide_start
		    + guide_coverRef
		    + guide_end + package_end);
	    writer.close();
	    System.out.println("[Created] Content.opf");
    }

}
