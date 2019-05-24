package com.makeepub;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

final class Cover_xhtml {
	private static String encoding = StandardCharsets.UTF_8.name();
	private static String path = "OEBPS/Text/cover.xhtml";
	private static String[] cover = {
					  "<?xml version=\"1.0\" encoding=\"utf-8\"?>\r\n" , 
					  "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.1//EN\"\r\n" , 
					  "  \"http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd\">\r\n" , 
					  "\r\n" , 
					  "<html xmlns=\"http://www.w3.org/1999/xhtml\">\r\n" , 
					  "<head>\r\n" , 
					  "  <title></title>\r\n" , 
					  "  <link href=\"../Styles/stylesheet.css\" rel=\"stylesheet\" type=\"text/css\"/>\r\n" , 
					  "</head>\r\n" , 
					  "\r\n" , 
					  "<body>\r\n" , 
					  "  <h1 style=\"text-align: center;\">Martial God Asura<br/></h1>\r\n" , 
					  "\r\n" , 
					  "  <div>\r\n" , 
					  "    <h5 style=\"text-align: center;\"><span style=\"font-size: 30px;\">(修罗武神)</span><br/></h5>\r\n" , 
					  "  </div>\r\n" , 
					  "  <h4 style=\"text-align: center;\">Kindhearted Bee</h4>\r\n" , 
					  "\r\n" , 
					  "  <h4 style=\"text-align: center;\">(善良的蜜蜂)<br/><br/><br/></h4>\r\n" , 
					  "\r\n" , 
					  "  <div>\r\n" , 
					  "    <span style=\"font-weight: normal;\"><br/></span>\r\n" , 
					  "  </div>\r\n" , 
					  "\r\n" , 
					  "  <div>\r\n" , 
					  "<em>Story Description:</em>\r\n" , 
					  "  </div>\r\n" , 
					  "\r\n" , 
					  "  <blockquote style=\"margin: 0 0 0 40px; border: none; padding: 0px;\">\r\n" , 
					  "    <div style=\"text-align: justify;\">\r\n" , 
					  "      <div>\r\n" , 
					  "        <br/>In terms of potential: Even if you are not a genius, you can learn Mysterious Techniques and martial skills. You can also learn without a teacher.\r\n" , 
					  "      </div>\r\n" , 
					  "\r\n" , 
					  "      <div>\r\n" , 
					  "        In terms of strength: Even if you have tens of thousands of treasures, you may not be able to defeat my grand World Spirit army.\r\n" , 
					  "      </div>\r\n" , 
					  "\r\n" , 
					  "      <div>\r\n" , 
					  "        Who am I? Every single living thing in the world views me as Asura. However, I did not know about that. Thus, as Asura, I became the Martial God.\r\n" , 
					  "      </div>\r\n" , 
					  "    </div>\r\n" , 
					  "  </blockquote>\r\n" , 
					  "\r\n" , 
					  "  <div>\r\n" , 
					  "    <br/>\r\n" , 
					  "  </div>\r\n" , 
					  "\r\n" , 
					  "  <div>\r\n" , 
					  "    Original Story can be found here:                                         <a href=\"http://www.17k.com/list/493239.html\" target=\"_blank\">Link</a>\r\n" , 
					  "  </div>\r\n" , 
					  "</body>\r\n" , 
					  "</html>"};
	
	Cover_xhtml (String epubDir){
		try {
		    	PrintWriter writer = new PrintWriter(epubDir+path, encoding);
		    	for (String string : cover) {
		    	    writer.print(string);
		    	}
			writer.close();
			/*try(InputStream in = new URL(
				"https://cdn.wuxiaworld.com/images/covers/mga.jpg?ver=39e11140d3b24d8646736032f6298c34f0cc8ba5")
				.openStream()){
			    Files.copy(in, Paths.get(epubDir+"OEBPS/Images/Cover.jpg"));
			} catch (IOException e) {
			    System.out.println("[!] Problems with getting cover image");
			    //e.printStackTrace();
			}*/
			System.out.println("[Created] Cover.xhtml");
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
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
