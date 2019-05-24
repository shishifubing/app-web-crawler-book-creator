package com.makeepub;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

public final class Stylesheet_css {
    private static String encoding = StandardCharsets.UTF_8.name();
    private static String path = "OEBPS/Styles/stylesheet.css";
    private static String h1_start = "h1 {\r\n";
    private static String h1_1 = "	text-align: center;\r\n";
    private static String h1_2 = "	page-break-before:always;\r\n";
    private static String h1_3 = "	margin-bottom: 10%;\r\n";
    private static String h1_4 = "	margin-top: 10%;\r\n";
    private static String h1_end = "}\r\n";
    private static String p_start = "p {\r\n";
    private static String p_1 = "	text-align : justify;\r\n";
    private static String p_end = "}\r\n";
    private static String img_start = "img {\r\n";
    private static String img_1 = "	width : 100%;\r\n";
    private static String img_2 = "	text-align : center;\r\n";
    private static String image_end = "}\r\n";

    Stylesheet_css(String epubDir) {
	try {
	    PrintWriter writer = new PrintWriter((epubDir+path), encoding);
	    writer.print(h1_start+h1_1+h1_2+h1_3+h1_4+h1_end
		    +p_start+p_1+p_end+img_start+img_1+img_2+image_end);
	    writer.close();
	    System.out.println("[Created] Stylesheet.css");
	} catch (FileNotFoundException e) {
	    e.printStackTrace();
	} catch (UnsupportedEncodingException e) {
	    e.printStackTrace();
	}
    }
}
