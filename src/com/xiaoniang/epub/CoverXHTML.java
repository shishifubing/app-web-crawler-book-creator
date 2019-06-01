package com.xiaoniang.epub;

import java.io.File;

import com.xiaoniang.epub.api.EpubBook;
import com.xiaoniang.epub.api.InnerFiles;

public final class CoverXHTML extends InnerFiles {

    public CoverXHTML(EpubBook epubBook) {
	setEpubBook(epubBook);
	setInnerPath(epubBook.innerFolderPath(3) + "cover.xhtml");
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
	addContent("  <div>\r\n");
	addContent("    <span style=\"font-weight: normal;\"><br/></span>\r\n");
	addContent("  </div>\r\n");
	addContent("\r\n");
	addContent("  <blockquote style=\"margin: 0 0 0 40px; border: none; padding: 5%;\">\r\n");
	addContent("      <div>\r\n");
	addContent("        <br/>" + escapeHtml(epubBook.description()) + "\r\n");
	addContent("      </div>\r\n");
	addContent("\r\n");
	addContent("  </blockquote>\r\n");
	addContent("\r\n");
	addContent("  <div>\r\n");
	addContent("    <br/>\r\n");
	addContent("  </div>\r\n");
	addContent("\r\n");
	addContent("  <div>\r\n");
	addContent(
		"    The story is taken from here: <a href=\"" + epubBook.url() + "\">" + epubBook.url() + "</a>\r\n");
	addContent("  </div>\r\n");
	addContent("</body>\r\n");
	addContent("</html>");
    }

}
