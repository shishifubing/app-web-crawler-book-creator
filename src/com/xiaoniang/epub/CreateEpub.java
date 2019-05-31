package com.xiaoniang.epub;

import java.io.IOException;

public class CreateEpub {

    @SuppressWarnings("unused")
    public static void main(String[] args) {
	try {
	    EpubBook epubBook = new EpubBook("", "https://www.wuxiaworld.com/novel/renegade-immortal", 0,1,2,3,4,5,6,7,8,9,10,11);
	    System.out.println("Epub book is created");
	} catch (IOException e) {
	    System.out.println("[!] Couldn't create the Epub book");
	    e.printStackTrace();
	}
    }
}