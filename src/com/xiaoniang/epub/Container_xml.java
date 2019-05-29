package com.xiaoniang.epub;

import java.io.IOException;
import java.io.PrintWriter;

final class Container_xml {
    	private static String path = "META-INF/container.xml";
    	private static String[] container = {
    		"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n",
    		"<container version=\"1.0\" xmlns=\"urn:oasis:names:tc:opendocument:xmlns:container\">\r\n",
    		"    <rootfiles>\r\n",
    		"        <rootfile full-path=\"OEBPS/content.opf\" media-type=\"application/oebps-package+xml\"/>\r\n",
    		"   </rootfiles>\r\n",
    		"</container>"
    	};
	
	protected static void create(EpubFile epubFile) throws IOException {
		    	try (PrintWriter writer = new PrintWriter(epubFile.outputPath()+path, epubFile.encoding())) {
		    	    for (String string : container) {
		    		writer.print(string);
		    	}
		    	epubFile.addToSupportFiles(epubFile.outputPath()+path);
		    	} catch (IOException e) {
		    	    System.out.println("[!] Couldn't create container.xml");
			} 
			System.out.println("[Created] container.xml");
	}
}
