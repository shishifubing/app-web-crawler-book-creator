package com.makeepub;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

final class Container_xml {
	private static String encoding = "UTF-8";
	private static String path = "META-INF/container.xml";
	private static String header = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n";
	private static String container_start = "<container version=\"1.0\" xmlns=\"urn:oasis:names:tc:opendocument:xmlns:container\">\r\n";
	private static String rootFiles_start = "    <rootfiles>\r\n";
	private static String fullPath = "        <rootfile full-path=\"OEBPS/content.opf\" media-type=\"application/oebps-package+xml\"/>\r\n";
	private static String rootFiles_end = "   </rootfiles>\r\n";
	private static String container_end = "</container>";
			
	
	Container_xml(String epubDir) {
		try {
		    	PrintWriter writer = new PrintWriter((epubDir+path), encoding);
			writer.print(header+container_start+rootFiles_start
				+fullPath+rootFiles_end+container_end);
			writer.close();
			System.out.println("[Created] Container.xml");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
		    e.printStackTrace();
		} 
	}
}
