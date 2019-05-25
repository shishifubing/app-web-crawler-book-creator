package com.makeepub;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.zip.CRC32;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

class EpubFile extends CreateEpub {
    
    private File epubFile = null;
    
    protected static void create() throws IOException {
	
	try {
	    File dir = new File(CreateEpub.path);
	    String dirPath = dir.getAbsolutePath();
	    File zipFile = new File(CreateEpub.path+CreateEpub.title+".epub");
	    FileOutputStream fos = new FileOutputStream(zipFile);
	    BufferedOutputStream bos = new BufferedOutputStream(fos);
	    ZipOutputStream zos = new ZipOutputStream(bos, encodingCharset);
	    zos.setMethod(ZipOutputStream.DEFLATED);
	    for (String oldPath : CreateEpub.epubFiles) {
		String zenPath = oldPath.substring(dirPath.length()+1).replaceAll("\\\\","/");
		ZipEntry zen = new ZipEntry(zenPath);
		File ipfile = new File(oldPath);
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
		Files.deleteIfExists(Paths.get(oldPath));
	    }
	    zos.close();
	    fos.close();
	    Folders.delete();
	    if (zipFile.exists()) {
		CreateEpub.epubFile = zipFile;
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
