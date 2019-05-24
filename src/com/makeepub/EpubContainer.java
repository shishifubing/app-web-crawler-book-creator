package com.makeepub;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.zip.CRC32;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

final class EpubContainer {
    
    private File epubFile = null;
    
    EpubContainer(String epubDir, List<String> outputFiles) {
	
	try {
	    File dir = new File(epubDir);
	    String dirPath = dir.getAbsolutePath();
	    File zipFile = new File(epubDir+"output.epub");
	    //File epubFile = new File(epubDir+"output.epub");
	    FileOutputStream fos = new FileOutputStream(zipFile);
	    BufferedOutputStream bos = new BufferedOutputStream(fos);
	    ZipOutputStream zos = new ZipOutputStream(bos, StandardCharsets.UTF_8);
	    zos.setMethod(ZipOutputStream.DEFLATED);
	    for (String path : outputFiles) {
		String zenPath = path.substring(dirPath.length()+1).replaceAll("\\\\","/");
		ZipEntry zen = new ZipEntry(zenPath);
		File ipfile = new File(path);
		if (ipfile.getName().contentEquals("mimetype") ||
			ipfile.getName().endsWith(".jpg")) {
		    zen.setMethod(ZipEntry.STORED);
		    CRC32 crc = new CRC32();
		    crc.update(Files.readAllBytes(ipfile.toPath()));
		    zen.setCrc(crc.getValue());
		    zen.setSize(ipfile.length());
		    zen.setCompressedSize(ipfile.length());
		}
		zos.putNextEntry(zen);
		FileInputStream fis = new FileInputStream(ipfile);
		int len; byte[] buffer = new byte[(int) ipfile.length()];
		while ((len = fis.read(buffer)) >= 0) {
		    zos.write(buffer, 0, len);
		}
		System.out.println("[Zipped] " + zenPath);
		zos.closeEntry();
		fis.close();
		Files.deleteIfExists(Paths.get(path));
	    }
	    zos.close();
	    fos.close();
	    Folders.delete(epubDir);
	    //zipFile.renameTo(epubFile);
	    if (zipFile.exists()) {
		this.epubFile = zipFile;
		System.out.println("[EPUB FILE IS CREATED]");
	    } else {
		System.out.println("[CAN'T CREATE EPUB FILE]");
	    }
	} catch (IOException e) {
	    e.printStackTrace();
	}
	
    }
    
    File getFile() {
	return epubFile;
    }
    
}
