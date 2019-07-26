package jmo.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class FileUtils {
	
	public static void write(String content, String loc) throws IOException{
		write(content, new File(loc));
	}
	
	public static void write(String content, File location) throws IOException {
		try (FileWriter writer = new FileWriter(location)){
			writer.write(content);
		}
	}
	
	
	public static Set<File> listFileTree(File dir, boolean recursive) {
		Set<File> fileTree = new HashSet<>();
		if (dir == null || !dir.exists()) {
			return fileTree;
		}
		File[] children = dir.listFiles();
		if (children == null) {
			fileTree.add(dir);
		} else {
			for (File entry : children) {
				if (entry.isFile()) {
					fileTree.add(entry);
				} else if (recursive) {
					fileTree.addAll(listFileTree(entry, recursive));
				}
			}
		}
		return fileTree;
	}

	private FileUtils() {
		//utility class
	}
}
