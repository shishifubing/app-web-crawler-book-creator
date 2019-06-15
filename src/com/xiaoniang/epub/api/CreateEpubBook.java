package com.xiaoniang.epub.api;

import java.io.File;
import java.io.IOException;
import com.xiaoniang.epub.resources.Links;
import com.xiaoniang.epub.resources.Log;

public class CreateEpubBook {

	public static void main(String[] args) {
		
		String path = "output" + File.separator;
		new File(path).mkdir();
			try {
				Log.start();
				Log.println("[Start]");
				new EpubBook(path, "https://www.novelupdates.com/series/pure-love-%e2%9c%95-insult-complex/");
				Log.end();
			} catch (IOException e) {
				Log.println("[!] Couldn't create the Epub book");
				e.printStackTrace();
			}
	}
}