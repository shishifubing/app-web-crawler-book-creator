package com.xiaoniang.epub.api;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.xiaoniang.epub.resources.LinkPairs;

public class CreateEpubBook {

	public static void main(String[] args) {
		
		String path = "output" + File.separator;
		new File(path).mkdir();
		for (String url : LinkPairs.links().keySet()) {
			try {
				System.out.println("[Creation - start] url: " + url);
				EpubBook epubBook = new EpubBook(path, url);
				epubBook.create(new int[] { 0 });
				System.out.println("[Creation - end]");
			} catch (IOException e) {
				System.out.println("[!] Couldn't create the Epub book. url: " + url);
				e.printStackTrace();
			}
		}
		
		System.out.println("[result]: " + InnerFile.escapeHtml("<img src=\"link\" alt=\"metal\">]</p>", ""));

	}
}