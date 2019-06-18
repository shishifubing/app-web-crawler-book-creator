package com.xiaoniang.epub.api;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

import org.apache.commons.logging.LogFactory;

import com.xiaoniang.epub.resources.Log;

public class CreateEpubBook {

	public static void main(String[] args) {
		LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog");
		java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF);
		java.util.logging.Logger.getLogger("org.apache.commons.httpclient").setLevel(Level.OFF);
		String path = "output" + File.separator;
		new File(path).mkdir();
		try {
			Log.println("[Start]");
			new EpubBook(path, "https://www.novelupdates.com/series/if-its-for-my-daughter-id-even-defeat-a-demon-lord/");
		} catch (IOException e) {
			Log.println("[!] Couldn't create the Epub book");
			e.printStackTrace(Log.writer());
		} finally {
			Log.end();
		}
	}
}