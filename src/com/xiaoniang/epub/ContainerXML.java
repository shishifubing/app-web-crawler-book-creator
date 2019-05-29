package com.xiaoniang.epub;

import java.io.IOException;
import java.io.PrintWriter;

final class ContainerXML {
    	private static String innerPath = "META-INF/container.xml";
    	private static String[] container = {
    		"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n",
    		"<container version=\"1.0\" xmlns=\"urn:oasis:names:tc:opendocument:xmlns:container\">\r\n",
    		"    <rootfiles>\r\n",
    		"        <rootfile full-path=\"OEBPS/content.opf\" media-type=\"application/oebps-package+xml\"/>\r\n",
    		"   </rootfiles>\r\n",
    		"</container>"
    	};
	
	protected static void create(EpubBook epubFile) throws IOException {
		    	try (PrintWriter writer = new PrintWriter(epubFile.getPath()+innerPath, epubFile.getEncoding())) {
		    	    for (String string : container) {
		    		writer.print(string);
		    	}
		    	epubFile.addToSupportFilesPaths(epubFile.getPath()+innerPath);
		    	} catch (IOException e) {
		    	    System.out.println("[!] Couldn't create container.xml");
			} 
		    	epubFile.addToSupportFilesPaths(epubFile.getPath()+innerPath);
			System.out.println("[Created] container.xml");
	}
}
