package com.xiaoniang.epub.api;

import java.io.IOException;

public class CreateEpubBook {

    public static void main(String[] args) {
	try {
	    EpubBook epubBook = new EpubBook("", "https://www.wuxiaworld.com/novel/renegade-immortal");
	    epubBook.create(new int[] {1});
	    System.out.println("Epub book is created");
	} catch (IOException e) {
	    System.out.println("[!] Couldn't create the Epub book");
	    e.printStackTrace();
	}
    }
}