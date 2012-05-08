package gmerg.utils;

import java.io.*;


import javax.servlet.http.HttpServletResponse;

public class FileHandler {

    public FileHandler() {
        
    }
    
    public static void saveStringToDesktop(HttpServletResponse response, String textString, String header, String contentType){
        int read = 0;
        byte[] bytes = new byte[1024];
        bytes = textString.getBytes();
              
        //Now set the content type for our response, be sure to use the best suitable content type depending on your file
        //the content type presented here is ok for, lets say, text files and others (like  CSVs, PDFs)
        response.setContentType(contentType);
        

        //This is another important attribute for the header of the response
        //Here fileName, is a String with the name that you will suggest as a name to save as
        //I use the same name as it is stored in the file system of the server.
        response.setHeader("Content-Disposition", header); 

        //Streams we will use to read, write the file bytes to our response
        ByteArrayInputStream fis = null;
        OutputStream os = null;
              
        //First we load the file in our InputStream
        try {
	        fis = new ByteArrayInputStream(bytes);
	        os = response.getOutputStream();
	        //While there are still bytes in the file, read them and write them to our OutputStream
	        while((read = fis.read(bytes)) != -1){
	            os.write(bytes,0,read);
        }
                         
        //Clean resources
        os.flush();
        os.close();
        }
        catch (FileNotFoundException e){
//            System.out.println("File not found");
            e.printStackTrace();
        }
        catch(IOException e){
//            System.out.println("io exception");
            e.printStackTrace();
        }
    }
    
    public static void saveFileToDesktop(HttpServletResponse response, String filePath, String fileName, String contentType, String downloadName) {
 	
		int read = 0;
		byte[] bytes = new byte[1024];

		response.setContentType(contentType);
		response.setHeader("Pragma", "public");
		response.setHeader("Expires", "0");
		response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0"); 
		response.setHeader("Cache-Control: private","false"); 
		response.setHeader("Content-Description", "File Transfer");
		response.setHeader("Content-Disposition", "attachment;filename=\"" + downloadName + "\"");

		// Streams we will use to read, write the file bytes to our response
		FileInputStream fis = null;
		OutputStream os = null;

		try {
			// First we load the file in our InputStream
			fis = new FileInputStream(new File(filePath, fileName));
			os = response.getOutputStream();
			while ((read = fis.read(bytes)) != -1) {
				os.write(bytes, 0, read);
			}
			// Clean resources
			os.flush();
			os.close();
		} catch (FileNotFoundException e) {
			System.out.println("File not found");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("io exception");
			e.printStackTrace();
		}
	}
}