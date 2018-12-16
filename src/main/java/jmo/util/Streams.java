package jmo.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Streams {
	private Streams() {
		throw new IllegalStateException("Utility class");
	}

	public static void copyStream(InputStream input, OutputStream output) throws IOException {
		copyStream(input, output, 1024);

	}

	public static void copyStream(InputStream input, OutputStream output, int bufferSize) throws IOException {
		int bytesRead;
		byte[] buffer = new byte[bufferSize];
		while ((bytesRead = input.read(buffer)) != -1) {
			output.write(buffer, 0, bytesRead);
		}
	}
	
	public static List<String> readLines(InputStream is, boolean closeStream)
			throws IOException {
		List<String> lines = readLines(is);
		if(closeStream) {
			is.close();
		}
		return lines;
	}
	
	public static List<String> readLines(InputStream is) throws IOException {
		CharsetDecoder decoder = StandardCharsets.UTF_8.newDecoder();
		InputStreamReader isr =  new InputStreamReader(is, decoder);
		BufferedReader reader = new BufferedReader(isr);
		List<String> lines = new ArrayList<>();
		String line;
		while((line =  reader.readLine())!= null) {
			lines.add(line);
		}
		return lines;
	}
}
