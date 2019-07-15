package com.xiaoniang.epub.innerfiles;

import java.util.ArrayList;
import java.util.List;

import com.xiaoniang.epub.api.EpubBook;
import com.xiaoniang.epub.api.InnerFile;

public final class Toc extends InnerFile {
	private final List<String> navPoints;
	private int playOrder = 1;

	public Toc(EpubBook epubBook) {
		setInnerPath(epubBook.innerFolderPath(0) + "toc.ncx");
		setEpubBook(epubBook);
		navPoints = new ArrayList<String>();
		addNavPoint("Cover", "cover.xhtml");
		addNavPoint("Description", "description.xhtml");
	}

	public void addNavPoint(String navPointLabel, String fileName) {
		navPoints.add("    <navPoint id=\"navPoint-" + playOrder + "\" playOrder=\"" + playOrder++ + "\">\r\n");
		navPoints.add("      <navLabel>\r\n");
		navPoints.add("        <text>" + navPointLabel + "</text>\r\n");
		navPoints.add("      </navLabel>\r\n");
		navPoints.add("      <content src=\"Text/" + fileName + "\"/>\r\n");
		navPoints.add("    </navPoint>\r\n");
	}

	public Toc fill() {
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
