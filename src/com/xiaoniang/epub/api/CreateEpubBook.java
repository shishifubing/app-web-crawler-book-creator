package com.xiaoniang.epub.api;

import java.io.File;

import com.xiaoniang.epub.gui.MainWindow;
import com.xiaoniang.epub.resources.Links;
import com.xiaoniang.epub.resources.Log;

public class CreateEpubBook {

	public static void main(String[] args) {
		String path = "output" + File.separator;
		new File(path).mkdir();
		Links.fill();
		MainWindow.create(args);
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
		}
		*/
	}
}