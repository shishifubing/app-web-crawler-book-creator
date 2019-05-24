package com.makeepub;

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

import com.adobe.epubcheck.api.EpubCheck;

final class EpubContainer {
    
    EpubContainer(String epubDir, List<String> outputFiles) {
	
	try {
	    File dir = new File(epubDir);
	    String dirPath = dir.getAbsolutePath();
	    File epubFile = new File(epubDir+"output.zip");
	    FileOutputStream fos = new FileOutputStream(epubFile);
	    ZipOutputStream zos = new ZipOutputStream(fos);
	    zos.setMethod(ZipOutputStream.STORED);
	    byte[] buffer = new byte[1024]; int len;
	    for (String path : outputFiles) {
		File ipfile = new File(path);
		ZipEntry zen = new ZipEntry(path.substring(dirPath.length()+1)); 
		zen.setMethod(ZipEntry.STORED);
		CRC32 crc = new CRC32();
		crc.update(Files.readAllBytes(ipfile.toPath()));
		zen.setCrc(crc.getValue());
		zen.setSize(ipfile.length());
		zen.setCompressedSize(ipfile.length());
		FileInputStream fis = new FileInputStream(ipfile);
		zos.putNextEntry(zen);
		while ((len = fis.read(buffer)) >= 0) {
		    zos.write(buffer, 0, len);
		}
		System.out.println("[Zipped] " + path);
		zos.closeEntry();
		fis.close();
		Files.deleteIfExists(Paths.get(path));
	    }
	    zos.close();
	    fos.close();
	    Folders.delete(epubDir);
	    epubFile.renameTo(new File(epubDir+"output.epub"));
	    System.out.println("[EPUB IS CREATED]");
	    EpubCheck epubcheck = new EpubCheck(epubFile);
	    if (epubcheck.validate()) {
		System.out.println("[NO ERRORS]");
	    }
	    System.out.println("[REACHED THE END]");
	} catch (IOException e) {
	    e.printStackTrace();
	}
	
    }
    
}
