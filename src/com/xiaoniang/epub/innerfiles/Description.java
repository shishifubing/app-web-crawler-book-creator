package com.xiaoniang.epub.innerfiles;

import com.xiaoniang.epub.api.EpubBook;
import com.xiaoniang.epub.api.InnerFile;

public class Description extends InnerFile {
	public Description(EpubBook epubBook) {
		setEpubBook(epubBook);
		setInnerPath(epubBook.innerFolderPath(3) + "description.xhtml");
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
		addContent("  <h3 style=\"text-align: center;\">" + epubBook.title() + "<br/></h3>\r\n");
		addContent("\r\n");
		addContent("  <h4 style=\"text-align: center;\">" + epubBook.author() + "</h4>\r\n");
		addContent("\r\n");
		addContent("      <p>Summary:</p>\r\n");
		addContent("  <blockquote style=\"margin: 0 0 0 0 px; border: none; padding: 0%;\">\r");
		for (String line : epubBook.description()) {
			addContent("      <p>\r");
			addContent("         " + line + "\r");
			addContent("      </p>\r");
		}
		addContent("  </blockquote>\r");
		addContent("    <p>Story type: <a href=\"" + epubBook.storyType(1) + "\">" + epubBook.storyType(0)
				+ "</a></p>\r\n");
		addContent("    <p>Information source: <a href=\"" + epubBook.urlNovelUpdates() + "\">"
				+ epubBook.urlNovelUpdates() + "</a></p>\r\n");
		addContent("    <p>Genres: \r");
		int i = 0;
		for (String[] genre : epubBook.genres()) {
			addContent("       <a href=\"" + genre[1] + "\">" + genre[0] + "</a>");
			if (++i == epubBook.genres().size()) {
				addContent(". \r\n");
				addContent("    </p>\r\n");
			} else {
				addContent(", \r");
			}
		}
		addContent("    <p>Tags: \r");
		i = 0;
		for (String tag : epubBook.tags()) {
			addContent("       " + tag);
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
