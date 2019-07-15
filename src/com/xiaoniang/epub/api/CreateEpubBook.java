package com.xiaoniang.epub.api;

import java.io.File;
import com.xiaoniang.epub.resources.Log;

public class CreateEpubBook {

	public static void main(String[] args) {
		String path = "output" + File.separator;
		new File(path).mkdir();
<<<<<<< HEAD
<<<<<<< HEAD
		Log.start();
		Links.fill();
		/*
		for (String link : Links.links().keySet()) {
			Log.println("\n[Start]");
			try {
				new EpubBook(path, link).create();
			} catch (Exception e) {
				Log.println("[!] Couldn't create the Epub book");
				e.printStackTrace(Log.stream());
			}
			Log.println("[End]\n");
=======
=======
>>>>>>> parent of 2493f63... Returned back to wuxiaworld, average time of downloading 500 chapters - 50s, all 55 books were downloaded in 1:30 hours, no errors
		try {
			Log.println("[Start]");
			new EpubBook(path, "https://www.novelupdates.com/series/renegade-immortal/").create();
		} catch (Exception e) {
			Log.println("[!] Couldn't create the Epub book");
			e.printStackTrace(Log.stream());
<<<<<<< HEAD
>>>>>>> parent of 2493f63... Returned back to wuxiaworld, average time of downloading 500 chapters - 50s, all 55 books were downloaded in 1:30 hours, no errors
=======
>>>>>>> parent of 2493f63... Returned back to wuxiaworld, average time of downloading 500 chapters - 50s, all 55 books were downloaded in 1:30 hours, no errors
		}
		*/
	}
}