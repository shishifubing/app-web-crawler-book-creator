package com.xiaoniang.epub.api;

import java.io.File;
import java.io.IOException;
import com.xiaoniang.epub.resources.Resources;

public class CreateEpubBook {

	public static void main(String[] args) {
		
		String path = "output" + File.separator;
		new File(path).mkdir();
		for (String url : Resources.links().keySet()) {
			try {
				System.out.println("[Start] url: " + url);
				EpubBook epubBook = new EpubBook(path, url);
				epubBook.create(new int[] { 0 });
			} catch (IOException e) {
				System.out.println("[!] Couldn't create the Epub book. url: " + url);
				e.printStackTrace();
			}
		}
	}
}