package com.xiaoniang.epub;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.imageio.ImageIO;

import com.xiaoniang.epub.api.EpubBook;
import com.xiaoniang.epub.api.InnerFiles;

public final class CoverXHTML extends InnerFiles {

    public CoverXHTML(EpubBook epubBook) {
	BufferedImage coverImage;
	epubBook.setCoverImageFile(new File(epubBook.tempPath() + epubBook.innerFolderPath(4) + "cover.jpg"));
	try (InputStream in = new URL(epubBook.coverLink()).openStream()) {
	    Files.copy(in, Paths.get(epubBook.coverImageFile().getAbsolutePath()));
	    coverImage = ImageIO.read(epubBook.coverImageFile());
	    epubBook.setCoverHeight(coverImage.getHeight());
	    epubBook.setCoverWidth(coverImage.getWidth());
	} catch (IOException e) {
	    System.out.println("[!] Creation of the cover image was met with issues");
	}
	epubBook.addToSupportFiles(epubBook.coverImageFile());
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
	addContent("      <div>\r\n");
	addContent("<svg xmlns=\"http://www.w3.org/2000/svg\" height=\"100%\" preserveAspectRatio=\"xMidYMid meet\" version=\"1.1\" viewBox=\"0 0 "+epubBook.coverWidth()+" "+epubBook.coverHeight()+"\" width=\"100%\" xmlns:xlink=\"http://www.w3.org/1999/xlink\"><image height=\""+epubBook.coverHeight()+"\" width=\""+epubBook.coverWidth()+"\" xlink:href=\"../Images/cover.jpg\" /></svg>");
	addContent("      </div>\r\n");
	addContent("</body>\r\n");
	addContent("</html>");
    }
}
