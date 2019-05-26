package com.makeepub;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

final class Container_xml extends CreateEpub {
    	private static String path = "META-INF/container.xml";
    	private static String[] container = {
    		"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n",
    		"<container version=\"1.0\" xmlns=\"urn:oasis:names:tc:opendocument:xmlns:container\">\r\n",
    		"    <rootfiles>\r\n",
    		"        <rootfile full-path=\"OEBPS/content.opf\" media-type=\"application/oebps-package+xml\"/>\r\n",
    		"   </rootfiles>\r\n",
    		"</container>"
    	};
	
	protected static void create() {
		    	try (PrintWriter writer = new PrintWriter(CreateEpub.tempDir+path, encoding)) {
		    	for (String string : container) {
		    	    writer.print(string);
		    	}
		    	} catch (FileNotFoundException | UnsupportedEncodingException e) {
		    	    System.out.println("[!] Couldn't create container.xml");
			} 
			System.out.println("[Created] container.xml");
	}
}
