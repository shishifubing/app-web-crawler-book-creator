package com.borinskikh.book_creator.innerFiles;

import com.borinskikh.book_creator.main.Book;
import com.borinskikh.book_creator.main.InnerFile;

public final class Mimetype extends InnerFile {

    public Mimetype(Book epubBook) {
        timeOfCreation = System.currentTimeMillis();
        setEpubBook(epubBook);
        setInnerPath("mimetype");
        addContent("application/epub+zip");
    }

}
