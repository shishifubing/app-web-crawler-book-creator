package com.xiaoniang.epub;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

final class CoverXHTML {
    private static String path = "OEBPS/Text/cover.xhtml";
    private static String[] cover = { 
	    "<?xml version=\"1.0\" encoding=\"utf-8\"?>\r\n",
	    "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.1//EN\"\r\n",
	    "  \"http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd\">\r\n", 
	    "\r\n",
	    "<html xmlns=\"http://www.w3.org/1999/xhtml\">\r\n", 
	    "<head>\r\n", 
	    "  <title></title>\r\n",
	    "  <link href=\"../Styles/stylesheet.css\" rel=\"stylesheet\" type=\"text/css\"/>\r\n", 
	    "</head>\r\n",
	    "\r\n", 
	    "<body>\r\n", 
	    "  <h1 style=\"text-align: center;\">" + title + "<br/></h1>\r\n", 
	    "\r\n", 
	    "  <h4 style=\"text-align: center;\">" + author + "</h4>\r\n", 
	    "\r\n", 
	    "  <div>\r\n",
	    "    <span style=\"font-weight: normal;\"><br/></span>\r\n", 
	    "  </div>\r\n", 
	    "\r\n",
	    "  <blockquote style=\"margin: 0 0 0 40px; border: none; padding: 5%;\">\r\n"};
    private static String[] coverEnd = { "  </blockquote>\r\n", 
	    "\r\n", 
	    "  <div>\r\n", 
	    "    <br/>\r\n", 
	    "  </div>\r\n",
	    "\r\n", 
	    "  <div>\r\n", 
	    "    The story is taken from here: <a href=\"" + url + "\">"+url+"</a>\r\n", 
	    "  </div>\r\n",
	    "</body>\r\n", 
	    "</html>" };

    protected static void create(EpubBook epubFile) {
	try (PrintWriter writer = new PrintWriter(epubFile.tempDir() + path, epubFile.encoding())) {
	for (String string : cover) {
	    writer.print(string);
	}
	for (String string : epubFile.summary()) {
	    writer.print("      <div>\r\n" + "        <br/>" + string + "\r\n" + "      </div>\r\n" + "\r\n");
	}
	for (String string : coverEnd) {
	    writer.print(string);
	}
	} catch (FileNotFoundException | UnsupportedEncodingException e) {
	    System.out.println("[!] Can't create cover.xhtml");
	}
	epubFile.addToSupportFiles(epubFile.outputPath()+path);
	System.out.println("[Created] cover.xhtml");
    }

}
