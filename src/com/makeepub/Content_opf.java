package com.makeepub;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class Content_opf {
    private static String encoding = "UTF-8";
    private static String path = "OEBPS/content.opf";
    private static String header = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n";
    private static String package_start = "<package xmlns=\"http://www.idpf.org/2007/opf\" unique-identifier=\"BookId\" version=\"2.0\">\r\n";
    private static String metadata_start = "  <metadata xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xmlns:opf=\"http://www.idpf.org/2007/opf\">\r\n";
    private static String identifier = "    <dc:identifier id=\"BookId\" opf:scheme=\"XN\">XiaoNiang-2019-05-18-WuxiaWorld.com</dc:identifier>\r\n";
    private static String title = "    <dc:title>Martial God Asura</dc:title>\r\n";
    private static String creator = "    <dc:creator opf:file-as=\"Kindhearted Bee [善良的蜜蜂]\" opf:role=\"aut\">Kindhearted Bee</dc:creator>\r\n";
    private static String language = "    <dc:language>en</dc:language>\r\n";
    private static String dateOfCreation = "    <dc:date opf:event=\"creation\">2019-05-18</dc:date>\r\n"; 
    private static String contributor1 = 	    "    <dc:contributor opf:role=\"programmer\">XiaoNiang</dc:contributor>\r\n";
    private static String contributor2 = "    <dc:contributor opf:role=\"translator\">Yang Wenli</dc:contributor>\r\n";
    private static String publisher = "    <dc:publisher>WuxiaWorld.com</dc:publisher>\r\n";
    private static String rights = "    <dc:rights>All materials' copyrights reserved by their respective authors and the associated publishers. Please respect their rights. Works will be deleted upon request by copyright holders.</dc:rights>\r\n";
    private static String metaName = "    <meta name=\"cover\" content=\"Cover.jpg\" />\r\n";
    private static String metadata_end = "  </metadata>\r\n";
    private static String manifest_start = "  <manifest>\r\n";
    private static String item_toc = "    <item href=\"toc.ncx\" id=\"ncx\" media-type=\"application/x-dtbncx+xml\" />\r\n";
    private static String item_cover = "    <item href=\"Text/Cover.xhtml\" id=\"Cover.xhtml\" media-type=\"application/xhtml+xml\" />\r\n";
    private static String item_stylesheet = "    <item href=\"Styles/stylesheet.css\" id=\"stylesheet.css\" media-type=\"text/css\" />\r\n"; 
    private static String item_pageStyle = "    <item href=\"Styles/page_styles.css\" id=\"page_styles.css\" media-type=\"text/css\" />\r\n";
    private static String item_cover_src = "    <item href=\"Images/Cover.jpg\" id=\"Cover.jpg\" media-type=\"image/jpeg\" />\r\n";
    private static String manifest_end = "  </manifest>\r\n";
    private static String spine_toc_start = "  <spine toc=\"ncx\">\r\n";
    private static String itemref_cover = "    <itemref idref=\"Cover.xhtml\" />\r\n";
    private static String spine_toc_end = "  </spine>\r\n";
    private static String guide_start = "  <guide>\r\n";
    private static String guide_coverRef = "    <reference href=\"Text/Cover.xhtml\" title=\"Cover\" type=\"cover\" />\r\n";
    private static String guide_end = "  </guide>\r\n";
    private static String package_end = "</package>";
    
    Content_opf(String epubDir, int index) {
	try {
	    PrintWriter writer = new PrintWriter((epubDir+path), encoding);
	    writer.print(header+package_start+metadata_start+identifier
		    +title+creator+language+dateOfCreation+contributor1
		    +contributor2+publisher+rights+metaName+metadata_end
		    +manifest_start+item_toc+item_cover
		    +item_stylesheet+item_pageStyle+item_cover_src);
	    for (int i = 1; i < index; i++) {
		writer.print("    <item href=\"Text/Chapter_" + i + ".xhtml\" id=\"Chapter_" + i
			+ ".xhtml\" media-type=\"application/xhtml+xml\" />");
	    }
	    writer.print(
		    manifest_end + spine_toc_start + itemref_cover);
	    for (int i = 1; i < index; i++) {
		writer.print("    <itemref idref=\"Chapter_" + i + ".xhtml\" />\r\n");
	    }
	    writer.print(spine_toc_end + guide_start
		    + guide_coverRef
		    + guide_end + package_end);
	    writer.close();
	    System.out.println("[Created] Content.opf");
	} catch (FileNotFoundException e) {
	    e.printStackTrace();
	} catch (UnsupportedEncodingException e) {
	    e.printStackTrace();
	} 
    }

}
