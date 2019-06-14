package com.xiaoniang.epub.innerfiles;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import javax.imageio.ImageIO;

import com.xiaoniang.epub.api.EpubBook;
import com.xiaoniang.epub.api.InnerFile;

public class CoverSrc extends InnerFile {
	private int coverHeight;
	private int coverWidth;
	private static String extension;
	private static String type;

	public CoverSrc(EpubBook epubBook) {
		BufferedImage image = null;
		extension = epubBook.coverLink().split("\\.")[epubBook.coverLink().split("\\.").length - 1];
		setInnerPath(epubBook.innerFolderPath(4) + "cover." + extension);
		if (extension.contentEquals("jpg")) {
			type = "jpeg";
		} else {
			type = extension;
		}
		while (image == null) {
			try (InputStream in = new URL(epubBook.coverLink()).openStream()) {
				image = ImageIO.read(in);
				coverHeight = image.getHeight();
				coverWidth = image.getWidth();
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				ImageIO.write(image, extension, baos);
				setImageArray(baos.toByteArray());
				baos.close();

			} catch (FileNotFoundException e) {
				try {
					image = ImageIO.read(new File("src/com/xiaoniang/epub/resources/cover.jpg"));
					coverHeight = image.getHeight();
					coverWidth = image.getWidth();
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					ImageIO.write(image, extension, baos);
					setImageArray(baos.toByteArray());
					baos.close();
				} catch (IOException e1) {
					System.out.println("[!] Issues with stock cover image");
				}
			} catch (IOException e) {
				System.out.println("[!] Download of the cover image was met with issues");
			}
		}
	}

	public int width() {
		return coverWidth;
	}

	public int height() {
		return coverHeight;
	}

	public static String extension() {
		return CoverSrc.extension;
	}

	public static String type() {
		return CoverSrc.type;
	}
}
