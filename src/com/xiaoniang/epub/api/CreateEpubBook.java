package com.xiaoniang.epub.api;

import java.io.IOException;

public class CreateEpubBook {

    public static void main(String[] args) {
	try {
	    EpubBook epubBook = new EpubBook("", "https://www.wuxiaworld.com/novel/stop-friendly-fire");
	    epubBook.create(new int[] {0,1,2,3,4,1,2,1,1,3});
	    System.out.println("Epub book is created");
	} catch (IOException e) {
	    System.out.println("[!] Couldn't create the Epub book");
	    e.printStackTrace();
	}
    }
}