package com.xiaoniang.epub.api;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.CRC32;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.xiaoniang.epub.resources.Log;

public abstract class InnerFile {
    private final List<String> content = new ArrayList<String>();
    private String innerPath;
    protected EpubBook epubBook;
    protected Thread thread;
    private byte[] imageArray;
    protected long timeOfCreation;

    public void addToZip(ZipOutputStream zos) {
        try {
            String zenPath = innerPath.replaceAll("\\\\", "/");
            ZipEntry zen = new ZipEntry(zenPath);
            String fileContent = "";
            for (String line : content) {
                fileContent += line;
            }
            byte[] fileArray = null;
            if (imageArray != null) {
                fileArray = imageArray;
            } else {
                fileArray = fileContent.getBytes(epubBook.encodingCharset());
            }
            if (zenPath.endsWith("mimetype") || zenPath.endsWith(".jpg") || zenPath.endsWith(".png")) {
                zen.setMethod(ZipEntry.STORED);
                CRC32 crc = new CRC32();
                crc.update(fileArray);
                zen.setCrc(crc.getValue());
                zen.setSize(fileArray.length);
                zen.setCompressedSize(fileArray.length);
            }
            zos.putNextEntry(zen);
            ByteArrayInputStream fis = new ByteArrayInputStream(fileArray);
            int len;
            byte[] buffer = new byte[1024];
            while ((len = fis.read(buffer)) >= 0) {
                zos.write(buffer, 0, len);
            }

            // String[] temp = zenPath.split("/");
            // double timeElapsed =
            // ((double)System.currentTimeMillis()-(double)timeOfCreation)/1000;
            // Log.println(" [Added] " + temp[temp.length-1]+". Time elapsed:
            // "+timeElapsed+"s");

            zos.closeEntry();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace(Log.stream());
        } finally {
            System.gc();
        }
    }

    public void createTestFile() {
        try (PrintWriter writer = new PrintWriter(new File("test.xhtml"))) {
            for (String line : content) {
                writer.print(line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace(Log.stream());
        }
    }

    protected static String escapeHtml(String input) {
        return input.replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;");
    }

    /*
     * protected static String escapeHtml(String input, String url) { String string
     * = input; Matcher matcher =
     * Pattern.compile("<(\\s*[^>/\\s]+)(\\s*[^>]*)>").matcher(string); int
     * endTagStart = string.length(); while (matcher.find()) { String endTag = "</"
     * + matcher.group(1) + ">"; // System.out.println("[LoopStart]\n "+endTag);
     * while (matcher.start() >= endTagStart) { endTagStart = string.length(); }
     * Matcher closingMatcher =
     * Pattern.compile(endTag).matcher(string.substring(matcher.start(),
     * endTagStart)); if (!closingMatcher.find()) { //
     * System.out.println(" String: "+string.substring(matcher.start(), //
     * endTagStart)); string = string.substring(0, endTagStart) + endTag +
     * string.substring(endTagStart); //
     * System.out.println(" Valid String: "+string); endTagStart -= endTag.length();
     * } else { endTagStart = matcher.start() + closingMatcher.start(); //
     * System.out.println(" Valid string: "+string.substring(matcher.start(), //
     * endTagStart+endTag.length())); } if
     * (matcher.group(2).contains("data-cfemail")) { string = string.substring(0,
     * string.indexOf(matcher.group(0)) + matcher.group(1).length() + 1) +
     * ">[Content is protected]" + string.substring(string.indexOf(endTag)); } if
     * (matcher.group(1).contains("img")) { Matcher imageCorrector =
     * Pattern.compile("src=\"[^\"]*\"").matcher(matcher.group(2)); if
     * (imageCorrector.find()) { string = string.replace(matcher.group(0), "<a " +
     * imageCorrector.group(0) + ">") .replace("</img>", "</a>").replace("src=\"",
     * "href=\""); string = escapeHtml(string, url); } } //
     * System.out.println("[LoopEnd]"); } return string.replaceAll("&",
     * "&amp;").replaceAll("'", "&apos;").replaceAll("http://\\.", "http://")
     * .replaceAll("https://\\.", "https://").replaceAll("href=\"#", "href=\"" + url
     * + "#") .replaceAll("href=\"/cdn-cgi", "href=\"http://www.z-s.cc/cdn-cgi"); }
     */
    protected void addContent(String... line) {
        for (String string : line) {
            content.add(string);
        }
    }

    protected void addContent(List<String> list) {
        for (String string : list) {
            content.add(string);
        }
    }

    protected void addContent(int index, String line) {
        content.add(index, line);
    }

    public List<String> content() {
        return content;
    }

    public String content(int index) {
        return content.get(index);
    }

    public void setInnerPath(String string) {
        innerPath = string;
    }

    public String innerPath() {
        return innerPath;
    }

    public void setEpubBook(EpubBook book) {
        epubBook = book;
    }

    public EpubBook epubBook() {
        return epubBook;
    }

    public void setImageArray(byte[] array) {
        imageArray = array;
    }
}
