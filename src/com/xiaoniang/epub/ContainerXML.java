package com.xiaoniang.epub;

import java.io.File;

final class ContainerXML extends InnerFiles {
	
    	ContainerXML(EpubBook epubBook) {
    	    setInnerPath(epubBook.innerFolderPath(1)+"container.xml");
    	    setFile(new File(epubBook.tempPath()+innerPath()));
    	    setEpubBook(epubBook);
    	    addContent("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n");
    	    addContent("<container version=\"1.0\" xmlns=\"urn:oasis:names:tc:opendocument:xmlns:container\">\r\n");
    	    addContent("    <rootfiles>\r\n");
    	    addContent("        <rootfile full-path=\"OEBPS/content.opf\" media-type=\"application/oebps-package+xml\"/>\r\n");
    	    addContent("   </rootfiles>\r\n");
    	    addContent("</container>");
    	}
    	
}
