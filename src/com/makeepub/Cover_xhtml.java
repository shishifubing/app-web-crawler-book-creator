package com.makeepub;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

final class Cover_xhtml {
	private static String encoding = StandardCharsets.UTF_8.name();
	private static String path = "OEBPS/Text/Cover.xhtml";
	private static String content = 
					  "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\r\n" + 
					  "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.1//EN\"\r\n" + 
					  "  \"http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd\">\r\n" + 
					  "\r\n" + 
					  "<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\">\r\n" + 
					  "<head>\r\n" + 
					  "  <meta content=\"true\" name=\"calibre:cover\" />\r\n" + 
					  "\r\n" + 
					  "  <title>Cover</title>\r\n" + 
					  "  \r\n" + 
					  "<style type=\"text/css\">\r\n" + 
					  "@page {padding: 0pt; margin:0pt}\r\n" + 
					  "  body { text-align: center; padding:0pt; margin: 0pt; }\r\n" + 
					  "</style>\r\n" + 
					  "</head>\r\n" + 
					  "\r\n" + 
					  "<body>\r\n" + 
					  "  <div>\r\n" + 
					  "    <svg xmlns=\"http://www.w3.org/2000/svg\" height=\"100%\" preserveAspectRatio=\"xMidYMid meet\" version=\"1.1\" viewBox=\"0 0 1118 1600\" width=\"100%\" xmlns:xlink=\"http://www.w3.org/1999/xlink\"><image height=\"1600\" width=\"1118\" xlink:href=\"../Images/Cover.jpg\" /></svg>\r\n" + 
					  "  </div>\r\n" + 
					  "</body>\r\n" + 
					  "</html>";
	
	Cover_xhtml (String epubDir){
		try {
		    	PrintWriter writer = new PrintWriter((epubDir+path), encoding);
			writer.print(content);
			writer.close();
			System.out.println("[Created] Cover.xhtml");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} 
	}
	
	    /*private static void downloadImage(String strImageURL) {
	        String strImageName = 
	                strImageURL.substring( strImageURL.lastIndexOf("/") + 1 );
	        System.out.println("Saving: " + strImageName + ", from: " + strImageURL);
	        try {
	            URL urlImage = new URL(strImageURL);
	            InputStream in = urlImage.openStream();
	            byte[] buffer = new byte[4096];
	            int n = -1;
	            OutputStream os = 
	                new FileOutputStream("output/OEBPS/Images/" + "Cover.jpg");
	            while ( (n = in.read(buffer)) != -1 ){
	                os.write(buffer, 0, n);
	            }
	            os.close();
	            System.out.println("Image is saved");
	            
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        
	    }*/
}
