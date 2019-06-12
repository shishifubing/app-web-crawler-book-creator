package com.xiaoniang.epub.innerfiles;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.imageio.ImageIO;

import com.xiaoniang.epub.api.EpubBook;
import com.xiaoniang.epub.api.InnerFile;

public class CoverJPG extends InnerFile {
	private int coverHeight;
	private int coverWidth;
	private byte[] imageArray;

	public CoverJPG(EpubBook epubBook) {
		BufferedImage image = null;
		setInnerPath(epubBook.innerFolderPath(4) + "cover.jpg");
		while (image == null) {
			try (InputStream in = new URL(epubBook.coverLink()).openStream()) {
				image = ImageIO.read(in);
				coverHeight = image.getHeight();
				coverWidth = image.getWidth();
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				ImageIO.write(image, "jpg", baos);
				imageArray = baos.toByteArray();
				baos.close();
			} catch (IOException e) {
				System.out.println("[!] Download of the cover image was met with issues");
			}
		}
	}

	public CoverJPG addCoverToZip(ZipOutputStream zos) throws IOException {
		String zenPath = innerPath().replaceAll("\\\\", "/");
		ZipEntry zen = new ZipEntry(zenPath);
		zos.putNextEntry(zen);
		ByteArrayInputStream fis = new ByteArrayInputStream(imageArray);
		int len;
		byte[] buffer = new byte[1024];
		while ((len = fis.read(buffer)) >= 0) {
			zos.write(buffer, 0, len);
		}
		System.out.println("[Added] " + innerPath());
		zos.closeEntry();
		fis.close();
		return this;
	}

	public int width() {
		return coverWidth;
	}

	public int height() {
		return coverHeight;
	}
}
