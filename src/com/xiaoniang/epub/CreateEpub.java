package com.xiaoniang.epub;

import java.io.IOException;
import com.adobe.epubcheck.api.EpubCheck;

public class CreateEpub {

    public static void main(String[] args) {
	try {
	    EpubFile epubFile = new EpubFile("", "https://www.wuxiaworld.com/novel/sage-monarch");
	    EpubCheck check = new EpubCheck(epubFile.getFile());
	    check.validate();
	    System.out.println("Epub book is created");
	} catch (IOException e) {
	    System.out.println("[!] Couldn't create the Epub book");
	}
    }
}