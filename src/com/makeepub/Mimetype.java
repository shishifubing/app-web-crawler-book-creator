package com.makeepub;

import java.io.IOException;
import java.io.PrintWriter;

final class Mimetype extends CreateEpub {
	static String path = "mimetype";
	static String content = "application/epub+zip";
	
	static void create() throws IOException {
	    PrintWriter writer = new PrintWriter(CreateEpub.tempDir+path, encoding);
	    writer.print(content);
	    writer.close();
	    System.out.println("[Created] mimetype");
	}
	
}
