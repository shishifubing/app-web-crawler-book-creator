package com.xiaoniang.epub;

import java.io.IOException;
import java.io.PrintWriter;

public final class Stylesheet_css {
    private static String path = "OEBPS/Styles/stylesheet.css";
    private static String[] stylesheet = {
    	"h1, h2, h3, h4, h5, h6 {\r\n" , 
    	"	color:#333333;\r\n" , 
    	"	text-decoration:none;\r\n" , 
    	"	padding:0px;\r\n" , 
    	"	margin: 0px;\r\n" , 
    	"}\r\n" , 
    	"\r\n" , 
    	"h1 {\r\n" , 
    	"	font-family: \"Ovo\", Sans-serif;\r\n" , 
    	"	font-size: 56px;\r\n" , 
    	"}\r\n" , 
    	"\r\n" , 
    	"h2 {\r\n" , 
    	"	font-family: \"Andalus\", Sans-serif;\r\n" , 
    	"	font-size: 30px;\r\n" , 
    	"}\r\n" , 
    	"\r\n" , 
    	"h3 {\r\n" , 
    	"	font-family: \"Ovo\", Sans-serif;\r\n" , 
    	"	font-size: 36px;\r\n" , 
    	"}\r\n" , 
    	"\r\n" , 
    	"h4 {\r\n" , 
    	"	font-family: \"Ovo\", Sans-serif;\r\n" , 
    	"	font-size: 26px;\r\n" , 
    	"}\r\n" , 
    	"\r\n" , 
    	"h5 {\r\n" , 
    	"	font-family: \"Mage Script\", Sans-serif;\r\n" , 
    	"	font-size: 36px;\r\n" , 
    	"}\r\n" , 
    	"\r\n" , 
    	"h6 {\r\n" , 
    	"	font-family: \"Moon Rune\", Sans-serif;\r\n" , 
    	"	font-size: 36px;\r\n" , 
    	"}\r\n" , 
    	"\r\n" , 
    	"img.alignleft {\r\n" , 
    	"	float: left;\r\n" , 
    	"	padding-right: 30px\r\n" , 
    	"}\r\n" , 
    	"\r\n" , 
    	"body {\r\n" , 
    	"	font-family: \"Ovo\", Sans-serif;\r\n" , 
    	"	font-size: 20px;\r\n" , 
    	"	color: #000000;\r\n" ,
    	"	margin-left: 2%;\r\n" , 
    	"    	margin-right: 2%;\r\n" , 
    	"    	margin-top: 2%;\r\n" , 
    	"    	margin-bottom: 2%\r\n",
    	"    	padding: 5%\r\n",
    	"}\r\n" , 
    	"\r\n" , 
    	"a {\r\n" , 
    	"	color: #3366aa;\r\n" , 
    	"}\r\n",};

    protected static void create(EpubFile epubFile) {
	try (PrintWriter writer = new PrintWriter(epubFile.outputPath()+path, epubFile.encoding())){
	    for (String string : stylesheet) {
		writer.print(string);
	    }
	    epubFile.addToSupportFiles(epubFile.outputPath()+path);
	    System.out.println("[Created] stylesheet.css");
	} catch (IOException e) {
	    System.out.println("[!] Couldn't create stylesheet.css");
	}
    }
}
