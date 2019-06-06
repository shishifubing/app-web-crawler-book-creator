package com.xiaoniang.epub.api;

import java.io.IOException;

public class CreateEpubBook {

    @SuppressWarnings("unused")
    public static void main(String[] args) {
	try {
	    EpubBook epubBook = new EpubBook("", "https://www.wuxiaworld.com/novel/stop-friendly-fire");
	    epubBook.make(new int[] {1,2});
	    System.out.println("Epub book is created");
	} catch (IOException e) {
	    System.out.println("[!] Couldn't create the Epub book");
	    e.printStackTrace();
	}
    }
}