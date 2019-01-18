package jmo.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileUtils {
	
	public static void write(String content, File location) throws IOException {
		try (FileWriter writer = new FileWriter(location)){
			writer.write(content);
		}
	}

	private FileUtils() {
		//utility class
	}
}
