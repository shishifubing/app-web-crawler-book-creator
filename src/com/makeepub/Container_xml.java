package com.makeepub;

import java.io.IOException;
import java.io.PrintWriter;

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
	
	protected static void create() throws IOException {
		    	PrintWriter writer = new PrintWriter(CreateEpub.path+path, encoding);
		    	for (String string : container) {
		    	    writer.print(string);
		    	}
			writer.close();
			System.out.println("[Created] Container.xml");
	}
}
