package com.makeepub;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

public class Page_Styles_css {
	private static String encoding = StandardCharsets.UTF_8.name();
	private static String path = "OEBPS/Styles/page_styles.css";
	private static String content = 
			"@page {\r\n"
			+ "	margin-bottom: 5pt;\r\n"
			+ "	margin-top: 5pt\r\n"
			+ "}";
	
	Page_Styles_css(String epubDir) {
		try {
		    	PrintWriter writer = new PrintWriter((epubDir + path), encoding);
			writer.print(content);
			writer.close();
			System.out.println("[Created] Page_Styles.css");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} 
	}
}
