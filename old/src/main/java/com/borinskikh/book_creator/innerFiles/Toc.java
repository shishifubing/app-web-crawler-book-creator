package com.borinskikh.book_creator.innerFiles;

import java.util.ArrayList;
import java.util.List;
import com.borinskikh.book_creator.main.Book;
import com.borinskikh.book_creator.main.InnerFile;

public final class Toc extends InnerFile {
    private final List<String> navPoints;

    public Toc(Book epubBook) {
        setInnerPath(epubBook.innerFolderPath(0) + "toc.ncx");
        setEpubBook(epubBook);
        navPoints = new ArrayList<String>();
        addNavPoint("Cover", "cover.xhtml", 1);
        addNavPoint("Description", "description.xhtml", 2);
    }

    synchronized public void addNavPoint(Chapter chapter) {
        navPoints.add("    <navPoint id=\"navPoint-" + (chapter.index() + 2) + "\" playOrder=\"" + (chapter.index() + 2)
                + "\">\r\n");
        navPoints.add("      <navLabel>\r\n");
        navPoints.add("        <text>" + chapter.title() + "</text>\r\n");
        navPoints.add("      </navLabel>\r\n");
        navPoints.add("      <content src=\"Text/" + chapter.fileName() + "\"/>\r\n");
        navPoints.add("    </navPoint>\r\n");
    }

    synchronized public void addNavPoint(String navPointLabel, String fileName, int index) {
        navPoints.add("    <navPoint id=\"navPoint-" + index + "\" playOrder=\"" + index + "\">\r\n");
        navPoints.add("      <navLabel>\r\n");
        navPoints.add("        <text>" + navPointLabel + "</text>\r\n");
        navPoints.add("      </navLabel>\r\n");
        navPoints.add("      <content src=\"Text/" + fileName + "\"/>\r\n");
        navPoints.add("    </navPoint>\r\n");
    }

    public Toc fill() {
        timeOfCreation = System.currentTimeMillis();
        addContent("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\" ?>\r\n");
        addContent("<!DOCTYPE ncx PUBLIC \"-//NISO//DTD ncx 2005-1//EN\"\r\n");
        addContent(" \"http://www.daisy.org/z3986/2005/ncx-2005-1.dtd\">\r\n");
        addContent("<ncx xmlns=\"http://www.daisy.org/z3986/2005/ncx/\" version=\"2005-1\">\r\n");
        addContent("<head>\r\n");
        addContent("    <meta content=\"" + epubBook().bookID() + epubBook().title() + "\" name=\"dtb:uid\"/>\r\n");
        addContent("    <meta content=\"1\" name=\"dtb:depth\"/>\r\n");
        addContent("    <meta content=\"0\" name=\"dtb:totalPageCount\"/>\r\n");
        addContent("    <meta content=\"0\" name=\"dtb:maxPageNumber\"/>\r\n");
        addContent("  </head>\r\n");
        addContent("  <docTitle>\r\n");
        addContent("    <text>" + epubBook().title() + "</text>\r\n");
        addContent("  </docTitle>\r\n");
        addContent("  <navMap>\r\n");
        addContent(navPoints);
        addContent("  </navMap>\r\n");
        addContent("</ncx>");
        return this;
    }
}
