package com.makeepub;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

final class Mimetype {
	private static String encoding = StandardCharsets.UTF_8.name();
	static String path = "mimetype";
	static String content = "application/epub+zip";
	
	Mimetype(String epubDir) {
		try {
		    	PrintWriter writer = new PrintWriter(epubDir+path, encoding);
			writer.print(content);
			writer.close();
			System.out.println("[Created] mimetype");
		} catch (IOException e) {
		    e.printStackTrace();
		} 
	}
	
}
