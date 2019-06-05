package com.xiaoniang.epub.innerfiles;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.imageio.ImageIO;

import com.xiaoniang.epub.api.EpubBook;
import com.xiaoniang.epub.api.InnerFiles;

public final class CoverXHTML extends InnerFiles {

    private int coverHeight;
    private int coverWidth;

    public CoverXHTML(EpubBook epubBook) {
	setEpubBook(epubBook);
	setInnerPath(epubBook.innerFolderPath(3) + "cover.xhtml");
	setFile(new File(epubBook.tempPath() + innerPath()));
	BufferedImage image = null;
	while (image == null) {
	    try (InputStream in = new URL(epubBook.coverLink()).openStream()) {
		Path path = Paths.get(epubBook.tempPath() + epubBook.innerFolderPath(4) + "cover.jpg");
		Files.copy(in, path);
		image = ImageIO.read(path.toFile());
		epubBook.addToSupportFiles(path.toFile());
		coverHeight = image.getHeight();
		coverWidth = image.getWidth();
		break;
	    } catch (IOException e) {
		System.out.println("[!] Download of the cover image was met with issues");
	    }
	}
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
	addContent("<body class=\"color-alt\">\r\n");
	addContent("  <div>\r\n");
	addContent("    <svg xmlns=\"http://www.w3.org/2000/svg\"\r");
	addContent("    height=\"100%\" preserveAspectRatio=\"xMidYMid meet\" version=\"1.1\"\r");
	addContent("    viewBox=\"0 0 " + coverWidth + " " + coverHeight + "\r");
	addContent("    \" width=\"100%\" xmlns:xlink=\"http://www.w3.org/1999/xlink\">\r");
	addContent("    <image height=\"" + coverHeight + "\" width=\"" + coverWidth + "\r");
	addContent("    \" xlink:href=\"../Images/cover.jpg\" /></svg>\r\n");
	addContent("  </div>\r\n");
	addContent("</body>\r\n");
	addContent("</html>");
    }

}
