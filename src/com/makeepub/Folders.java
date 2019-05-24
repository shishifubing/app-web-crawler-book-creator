package com.makeepub;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

final class Folders {
	static String[] path = {"", "OEBPS", "META-INF",
				"OEBPS/Styles", "OEBPS/Text",
				"OEBPS/Images"};
	
	Folders(String targetDir) {
			File file;
			for (String string: path) {
				file = new File(targetDir+string);
				file.mkdir();
			}
			System.out.println("[Created] Folders");
	}
	
	protected static void delete(String targetDir) {
	    	for (int i = path.length-1; i>=1; i--) {
		    Path pathPath = Paths.get(targetDir+path[i]);
		    try {
			Files.deleteIfExists(pathPath);
		    } catch (IOException e) {
			e.printStackTrace();
		    }
		}
	}
}