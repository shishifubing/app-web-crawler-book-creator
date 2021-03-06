package com.borinskikh.book_creator.innerFiles;

import com.borinskikh.book_creator.main.Book;
import com.borinskikh.book_creator.main.InnerFile;

public final class Container extends InnerFile {

    public Container(Book epubBook) {
        timeOfCreation = System.currentTimeMillis();
        setInnerPath(epubBook.innerFolderPath(1) + "container.xml");
        setEpubBook(epubBook);
        addContent("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n");
        addContent("<container version=\"1.0\" xmlns=\"urn:oasis:names:tc:opendocument:xmlns:container\">\r\n");
        addContent("    <rootfiles>\r\n");
        addContent(
                "        <rootfile full-path=\"OEBPS/content.opf\" media-type=\"application/oebps-package+xml\"/>\r\n");
        addContent("   </rootfiles>\r\n");
        addContent("</container>");
    }

}
