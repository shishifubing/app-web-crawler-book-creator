package com.xiaoniang.epub.resources;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Connection.Response;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Links {
	private static final Map<String, String> linkPairs = new HashMap<>();

	public static String link(String key) {
		return linkPairs.get(key);
	}

	public static Map<String, String> links() {
		return linkPairs;
	}
	public static void fill() {
		try {
			Files.lines(Paths.get("src/com/xiaoniang/epub/resources/linkPairs.txt")).forEach(
					(String line)->{
						String[] string = line.split(",");
						linkPairs.put(string[0], string[1]);
					});
			/*
			PrintStream stream = new PrintStream(new File("src/com/xiaoniang/epub/resources/linkPairs.txt"));
			for (String key : linkPairs.keySet()) {
				stream.println(key+","+linkPairs.get(key));
			}
			*/
			/*
			Document allNovels = null;
			while (allNovels == null) {
				try {
					allNovels = Jsoup.connect("https://www.wuxiaworld.co/all/").get();
				} catch (IOException e) {
					Log.println("[!] Cannot connect to the " + "https://www.wuxiaworld.co/all/");
				}
			}
			PrintStream stream1 = new PrintStream(new File("src/com/xiaoniang/epub/resources/temp.txt"));
			Elements linksElements = allNovels.select("div#main").select("a");
			List<String> links = new ArrayList<String>(linksElements.size());
			for (Element linkElement : linksElements) {
				Response response = null;
				int attempt = 0;
				while (response == null) {
					String link = "https://www.novelupdates.com/series"+linkElement.attr("href");
					try {
						response = Jsoup.connect(link).execute();
						stream1.print(link);
					} catch (HttpStatusException e) {
						stream1.print("[UNDEFINED]");
						break;
					} catch (IOException e) {
						Log.println(++attempt+" [!] Cannot connect to the " + link);
						if (attempt%10==0) {
							stream1.print("[UNDEFINED]");
							break;
						}
					}
				}
				stream1.println(","+linkElement.attr("abs:href"));
			}
			*/
		} catch (IOException e) {
			e.printStackTrace(Log.stream());
		}
		
	}
}
