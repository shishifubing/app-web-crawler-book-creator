package com.xiaoniang.epub.innerfiles;

import com.xiaoniang.epub.api.EpubBook;
import com.xiaoniang.epub.api.InnerFile;

public class Cover extends InnerFile {

	public Cover(EpubBook epubBook, CoverSrc coverSrc) {
		setEpubBook(epubBook);
		setInnerPath(epubBook.innerFolderPath(3) + "cover.xhtml");
		addContent("<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"no\"?>\r\n");
		addContent("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.1//EN\"\r\n");
		addContent("  \"http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd\">\r\n");
		addContent("\r\n");
		addContent("<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\">\r\n");
		addContent("<head>\r\n");
		addContent("  <link href=\"../Styles/stylesheet.css\" rel=\"stylesheet\" type=\"text/css\" />\r\n");
		addContent("\r\n");
		addContent("  <title></title>\r\n");
		addContent("</head>\r\n");
		addContent("\r\n");
		addContent("<body>\r\n");
		addContent("  <div>\r\n");
		addContent("    <svg xmlns=\"http://www.w3.org/2000/svg\"\r");
		addContent("      height=\"100%\" width=\"100%\"\r");
		addContent("      preserveAspectRatio=\"xMidYMid meet\"\r");
		addContent("      version=\"1.1\"\r");
		addContent("      viewBox=\"0 0 " + coverSrc.width() + " " + coverSrc.height() + "\"\r");
		addContent("      xmlns:xlink=\"http://www.w3.org/1999/xlink\">\r");
		addContent("      <image height=\"" + coverSrc.height() + "\" width=\"" + coverSrc.width() + "\"\r");
		addContent("      xlink:href=\"../Images/cover." + CoverSrc.extension() + "\"/></svg>\r\n");
		addContent("  </div>\r\n");
		addContent("</body>\r\n");
		addContent("</html>");
	}
}
