package jmo.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Streams {
	public static void copyStream(InputStream input, OutputStream output)
		    throws IOException {
	    byte[] buffer = new byte[1024]; // Adjust if you want
	    int bytesRead;
	    while ((bytesRead = input.read(buffer)) != -1)  {
	        output.write(buffer, 0, bytesRead);
	    }
	}
}
