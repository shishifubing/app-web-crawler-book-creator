package com.xiaoniang.epub;

import java.io.IOException;

public class CreateEpub {

    @SuppressWarnings("unused")
    public static void main(String[] args) {
	try {
	    EpubBook epubBook = new EpubBook("", "https://www.wuxiaworld.com/novel/sage-monarch", 0);
	    System.out.println("Epub book is created");
	} catch (IOException e) {
	    System.out.println("[!] Couldn't create the Epub book");
	    e.printStackTrace();
	}
    }
}