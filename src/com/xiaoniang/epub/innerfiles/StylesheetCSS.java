package com.xiaoniang.epub.innerfiles;

import java.io.File;

import com.xiaoniang.epub.api.EpubBook;
import com.xiaoniang.epub.api.InnerFiles;

public final class StylesheetCSS extends InnerFiles {

    public StylesheetCSS(EpubBook epubBook) {
	setInnerPath(epubBook.innerFolderPath(2)+"stylesheet.css");
	setEpubBook(epubBook);
	setFile(new File(epubBook.tempPath() + innerPath()));
	addContent("h1, h2, h3, h4, h5, h6 {\r\n"); 
	addContent("	color:#333333;\r\n"); 
	addContent("	text-decoration:none;\r\n"); 
	addContent("	padding:0px;\r\n"); 
	addContent("	margin: 0px;\r\n"); 
	addContent("}\r\n"); 
	addContent("\r\n"); 
	addContent("h1 {\r\n"); 
	addContent("	font-family: \"Ovo\", Sans-serif;\r\n"); 
	addContent("	font-size: 56px;\r\n"); 
	addContent("}\r\n"); 
	addContent("\r\n"); 
	addContent("h2 {\r\n"); 
	addContent("	font-family: \"Andalus\", Sans-serif;\r\n");
	addContent("	font-size: 30px;\r\n");
	addContent("}\r\n"); 
	addContent("\r\n");
	addContent("h3 {\r\n"); 
	addContent("	font-family: \"Ovo\", Sans-serif;\r\n");
	addContent("	font-size: 36px;\r\n");
	addContent("}\r\n");
	addContent("\r\n");
	addContent("h4 {\r\n"); 
	addContent("	font-family: \"Ovo\", Sans-serif;\r\n"); 
	addContent("	font-size: 26px;\r\n"); 
	addContent("}\r\n");
	addContent("\r\n"); 
	addContent("h5 {\r\n");
	addContent("	font-family: \"Mage Script\", Sans-serif;\r\n");
	addContent("	font-size: 36px;\r\n"); 
	addContent("}\r\n");
	addContent("\r\n"); 
	addContent("h6 {\r\n"); 
	addContent("	font-family: \"Moon Rune\", Sans-serif;\r\n"); 
	addContent("	font-size: 36px;\r\n");
	addContent("}\r\n");
	addContent("\r\n"); 
	addContent("img.alignleft {\r\n"); 
	addContent("	float: left;\r\n"); 
	addContent("	padding-right: 30px\r\n"); 
	addContent("}\r\n");
	addContent("\r\n"); 
	addContent("body {\r\n"); 
	addContent("	font-family: \"Ovo\", Sans-serif;\r\n"); 
	addContent("	font-size: 20px;\r\n"); 
	addContent("	color: #000000;\r\n");
	addContent("	margin-left: 2%;\r\n");
	addContent("    	margin-right: 2%;\r\n"); 
	addContent("    	margin-top: 2%;\r\n"); 
	addContent("    	margin-bottom: 2%\r\n");
	addContent("    	padding: 5%\r\n");
	addContent("}\r\n");
	addContent("\r\n");
	addContent("a {\r\n"); 
	addContent("	color: #3366aa;\r\n"); 
	addContent("}\r\n");
    }
}
