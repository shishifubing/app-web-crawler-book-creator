package com.makeepub;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

final class Container_xml {
    	private static String encoding = StandardCharsets.UTF_8.name();
    	private static String path = "META-INF/container.xml";
    	private static String[] container = {
    		"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n",
    		"<container version=\"1.0\" xmlns=\"urn:oasis:names:tc:opendocument:xmlns:container\">\r\n",
    		"    <rootfiles>\r\n",
    		"        <rootfile full-path=\"OEBPS/content.opf\" media-type=\"application/oebps-package+xml\"/>\r\n",
    		"   </rootfiles>\r\n",
    		"</container>"
    	};
	
	Container_xml(String epubDir) {
		try {
		    	PrintWriter writer = new PrintWriter(epubDir+path, encoding);
		    	for (String string : container) {
		    	    writer.print(string);
		    	}
			writer.close();
			System.out.println("[Created] Container.xml");
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
}
