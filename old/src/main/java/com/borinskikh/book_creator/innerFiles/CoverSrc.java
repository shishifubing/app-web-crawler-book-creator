package com.borinskikh.book_creator.innerFiles;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.net.URL;
import javax.imageio.ImageIO;

import com.borinskikh.book_creator.main.Book;
import com.borinskikh.book_creator.main.InnerFile;
import com.borinskikh.book_creator.resources.Log;

public class CoverSrc extends InnerFile {
    private int coverHeight;
    private int coverWidth;
    private static String extension;
    private static String type;

    public CoverSrc(Book epubBook) {
        timeOfCreation = System.currentTimeMillis();
        BufferedImage image = null;
        extension = epubBook.coverLink().split("\\.")[epubBook.coverLink().split("\\.").length - 1];
        if (extension.contentEquals("jpg")) {
            type = "jpeg";
        } else {
            type = extension;
        }
        setInnerPath(epubBook.innerFolderPath(4) + "cover." + extension);
        while (image == null) {
            if (epubBook.coverLink().equals(null) || epubBook.coverLink().isEmpty()) {
                image = setDefaultCover();
            }
            try (InputStream in = new URL(epubBook.coverLink()).openStream()) {
                image = ImageIO.read(in);
                coverHeight = image.getHeight();
                coverWidth = image.getWidth();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(image, extension, baos);
                setImageArray(baos.toByteArray());
                baos.close();
            } catch (FileNotFoundException e) {
                image = setDefaultCover();
            } catch (ConnectException e) {
            } catch (IOException e) {
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

    private BufferedImage setDefaultCover() {
        try {
            BufferedImage image = ImageIO.read(new File("src/com/xiaoniang/epub/resources/cover.jpg"));
            coverHeight = image.getHeight();
            coverWidth = image.getWidth();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, extension, baos);
            setImageArray(baos.toByteArray());
            baos.close();
            return image;
        } catch (IOException e) {
            Log.println("[!] Issues with stock cover image");
            e.printStackTrace(Log.stream());
        }
        return null;
    }
}
