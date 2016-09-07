package jmo.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Streams {
	public static void copyStream(InputStream input, OutputStream output) 
			throws IOException {
		copyStream(input, output, 1024);
		
	}
	
	public static void copyStream(InputStream input, OutputStream output, int bufferSize)
		    throws IOException {
		
		int bytesRead;
	    byte[] buffer = new byte[bufferSize]; 
	    while ((bytesRead = input.read(buffer)) != -1)  {
	        output.write(buffer, 0, bytesRead);
	    }
	}
}
