package com.xiaoniang.epub.innerfiles;

import com.xiaoniang.epub.api.EpubBook;
import com.xiaoniang.epub.api.InnerFile;

public final class Container extends InnerFile {

	public Container(EpubBook epubBook) {
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
