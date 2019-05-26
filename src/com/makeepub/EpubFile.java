package com.makeepub;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.zip.CRC32;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

class EpubFile extends CreateEpub {
    
    private File epubFile = null;
    
    protected static void create(List<String> chapterEpubFiles) {
	
	try {
	    File dir = new File(CreateEpub.outputPath);
	    String dirPath = dir.getAbsolutePath();
	    File epubFile = new File(CreateEpub.outputPath+CreateEpub.title+".epub");
	    FileOutputStream fos = new FileOutputStream(epubFile);
	    BufferedOutputStream bos = new BufferedOutputStream(fos);
	    ZipOutputStream zos = new ZipOutputStream(bos, encodingCharset);
	    zos.setMethod(ZipOutputStream.DEFLATED);
	    for (String filePath : chapterEpubFiles) {
		String zenPath = filePath.substring(dirPath.length()+1).replaceAll("\\\\","/");
		System.out.println("[chapterEpubFiles is not empty]");
		ZipEntry zen = new ZipEntry(zenPath);
		File ipFile = new File(filePath);
		if (ipFile.getName().contentEquals("mimetype") ||
			ipFile.getName().endsWith(".jpg")) {
		    zen.setMethod(ZipEntry.STORED);
		    CRC32 crc = new CRC32();
		    crc.update(Files.readAllBytes(ipFile.toPath()));
		    zen.setCrc(crc.getValue());
		    zen.setSize(ipFile.length());
		    zen.setCompressedSize(ipFile.length());
		}
		zos.putNextEntry(zen);
		FileInputStream fis = new FileInputStream(ipFile);
		int len; byte[] buffer = new byte[(int) ipFile.length()];
		while ((len = fis.read(buffer)) >= 0) {
		    zos.write(buffer, 0, len);
		}
		System.out.println("[Zipped] " + zenPath);
		zos.closeEntry();
		fis.close();
		if (ipFile.getName().startsWith("chapter_")) {
		    Files.deleteIfExists(Paths.get(filePath));
		}
	    }
	    zos.close();
	    fos.close();
	    CreateEpub.epubFile = epubFile;
	    System.out.println("[Created Epub File] "+epubFile.getName());
	} catch (IOException e) {
	    System.out.println("[!] Can't create Epub file of volume "+ CreateEpub.title);
	}
	
    }
    
    File getFile() {
	return epubFile;
    }
    
}
