package com.xiaoniang.epub.innerfiles;

import java.io.File;

import com.xiaoniang.epub.api.EpubBook;
import com.xiaoniang.epub.api.InnerFiles;

public class DescriptionXHTML extends InnerFiles {
    public DescriptionXHTML(EpubBook epubBook) {
	setEpubBook(epubBook);
	setInnerPath(epubBook.innerFolderPath(3) + "description.xhtml");
	setFile(new File(epubBook.tempPath() + innerPath()));
	addContent("<?xml version=\"1.0\" encoding=\"utf-8\"?>\r\n");
	addContent("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.1//EN\"\r\n");
	addContent("  \"http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd\">\r\n");
	addContent("\r\n");
	addContent("<html xmlns=\"http://www.w3.org/1999/xhtml\">\r\n");
	addContent("<head>\r\n");
	addContent("  <title></title>\r\n");
	addContent("  <link href=\"../Styles/stylesheet.css\" rel=\"stylesheet\" type=\"text/css\"/>\r\n");
	addContent("</head>\r\n");
	addContent("\r\n");
	addContent("<body>\r\n");
	addContent("  <h1 style=\"text-align: center;\">" + epubBook.title() + "<br/></h1>\r\n");
	addContent("\r\n");
	addContent("  <h4 style=\"text-align: center;\">" + epubBook.author() + "</h4>\r\n");
	addContent("\r\n");
	addContent("  <div>\r");
	addContent("    <span style=\"font-weight: normal;\"><br/></span>\r\n");
	addContent("  </div>\r");
	addContent("  <blockquote style=\"margin: 0 0 0 40px; border: none; padding: 5%;\">\r\n");
	addContent("      <p>\r");
	addContent("         " + escapeHtml(epubBook.description()) + "\r\n");
	addContent("      </p>\r");
	addContent("  </blockquote>\r\n");
	addContent("    <p>Story type: <a href=\"" + epubBook.storyType(1) + "\">" + epubBook.storyType(0)
		+ "</a></p>\r\n");
	addContent("    <p>Story source: <a href=\"" + epubBook.urlWuxiaWorld() + "\">" + epubBook.urlWuxiaWorld()
		+ "</a></p>\r\n");
	addContent("    <p>Information source: <a href=\"" + epubBook.urlNovelUpdates() + "\">"
		+ epubBook.urlNovelUpdates() + "</a></p>\r\n");
	addContent("    <p>Genres: \r");
	int i = 0;
	for (String genre : epubBook.genres(1)) {
	    addContent("       "+genre);
	    if (++i == epubBook.genres(1).size()) {
		addContent(". \r\n");
		addContent("    </p>\r\n");
	    } else {
		addContent(", \r");
	    }
	}
	addContent("    <p>Tags: \r");
	i = 0;
	for (String tag : epubBook.tags()) {
	    addContent("       "+tag);
	    if (++i == epubBook.tags().size()) {
		addContent(". \r\n");
		addContent("    </p>\r\n");
	    } else {
		addContent(", \r");
	    }
	}
	addContent("</body>\r\n");
	addContent("</html>");
    }
}
