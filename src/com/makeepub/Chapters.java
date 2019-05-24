package com.makeepub;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

final class Chapters {
	private static String encoding = StandardCharsets.UTF_8.name();
	
	Chapters(String url, int lastChapterIndex) {
		int chapterIndex = 1;
		while (chapterIndex<=lastChapterIndex) {
		    	getChapter(url.substring(0, url.length() - 1), chapterIndex);
			System.out.println("[Created] Chapter_" + chapterIndex++ + ".xhtml");
		}
	}
	
	private static void getChapter(String url, int index) {
		try {
			Document chapter = null;
			try {
			    chapter = Jsoup.connect(url + index).get();
			} catch (IOException e) {
			    System.out.println("[!] Connection issues in the process of creating chapter " +index);
			}
			Elements text = chapter.select("div.fr-view").select("p");
			PrintWriter chapterWriter = new PrintWriter("output/OEBPS/Text/Chapter_"+index+".xhtml", encoding);
			chapterWriter.print(
				"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\r\n" + 
				"<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.1//EN\"\r\n" + 
				"  \"http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd\">\r\n" + 
				"\r\n" + 
				"<html xmlns=\"http://www.w3.org/1999/xhtml\">\r\n" + 
				"<head>\r\n" + 
				"  <title></title>\r\n" + 
				"  <link href=\"../Styles/stylesheet.css\" rel=\"stylesheet\" type=\"text/css\" />\r\n" + 
				"  <link href=\"../Styles/page_styles.css\" rel=\"stylesheet\" type=\"text/css\" />\r\n" + 
				"</head>\r\n" + 
				"\r\n" + 
				"<body>\r\n");
			boolean check = false;
			for (Element paragraph : text) {
				if (!check) {
					String line = getChapterTitle(paragraph.text());
					if (!line.equals("NOT_FOUND")) {
						line = "  <h1 id=\"Chapter_" + index + "\">" + line + "</h1>";
						chapterWriter.print(line);
						check = true;
					}
				} else if (check && !paragraph.text().equals(null) && !paragraph.text().isEmpty()) {
					chapterWriter.print("  <p>" + paragraph.text() + "</p>");
				} else if (!paragraph.text().equals(null) || !paragraph.text().isEmpty()) {
					break;
				}
			}
			chapterWriter.print("</body>\r\n" + 
				"</html>");
			chapterWriter.close();
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
		    System.out.println("[!] Issues with the file of chapter " +index);
		}
	}


	private static String getChapterTitle(String line) {
		Pattern pattern = Pattern.compile("(.*)(Chapter.+)");
		Matcher matcher = pattern.matcher(line);
		if (!matcher.find())
			return "NOT_FOUND";
		else
			return matcher.group(2);
	}
}
