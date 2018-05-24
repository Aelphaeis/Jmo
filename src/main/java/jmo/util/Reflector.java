package jmo.util;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public final class Reflector {

	private Reflector() {
		throw new IllegalStateException("Utility class");
	}

	private static final String CLASS_SUFFIX = ".class";

	
	/**
	 * @param clazz
	 * @return
	 */
	//TODO construction with params
	public static Object initParamCtor(Class<?> clazz) {
		try {
			Constructor<?> ctor = clazz.getConstructor();
			return ctor.newInstance();
		} 
		catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * Given a package this method returns all classes contained in that package
	 * 
	 * @param pkg
	 * @return Classes within package
	 */
	public static List<Class<?>> getClassesForPackage(Package pkg) {
		return getClassesForPackage(pkg, ClassLoader.getSystemClassLoader());
	}

	
	
	/**
	 * Given a package name and class loader this method returns all classes
	 * contained in package.
	 * 
	 * @param pkg
	 * @param loader
	 * @return
	 * @throws IOException
	 */
	public static List<Class<?>> getClassesForPackage(Package pkg, ClassLoader loader) {
		ArrayList<Class<?>> classes = new ArrayList<>();

		// Get name of package and turn it to a relative path
		String pkgname = pkg.getName();
		String relPath = pkgname.replace('.', '/');

		// Get a File object for the package

		Enumeration<URL> resources;
		try {
			resources = loader.getResources(relPath);
		} catch (IOException e) {
			String err = "Unexpected error loading resources";
			throw new ReflectorException(err, e);
		}
		boolean isEmpty = !resources.hasMoreElements();

		if (isEmpty) {
			String err = "Unexpected problem: No resource for {%s}";
			throw new ReflectorException(String.format(err, relPath));
		} else {
			do {
				URL resource = resources.nextElement();
				// If the resource is a jar get all classes from jar
				if (resource.toString().startsWith("jar:")) {
					classes.addAll(processJarfile(resource, pkgname));
				} else {
					classes.addAll(processDirectory(new File(resource.getPath()), pkgname));
				}
			} while (resources.hasMoreElements());
		}

		return classes;
	}

	/**
	 * Given a package name and a directory returns all classes within that
	 * directory
	 * 
	 * @param directory
	 * @param pkgname
	 * @return Classes within Directory with package name
	 */
	public static List<Class<?>> processDirectory(File directory, String pkgname) {
		List<Class<?>> classes = new ArrayList<>();
		// Get the list of the files contained in the package
		String[] files = directory.list();
		for (int i = 0; i < files.length; i++) {
			String fileName = files[i];
			String className = null;
			// we are only interested in .class files
			if (fileName.endsWith(CLASS_SUFFIX)) {
				// removes the .class extension
				className = pkgname + '.' + fileName.substring(0, fileName.length() - 6);
			}

			if (className != null) {
				classes.add(loadClass(className));
			}

			// If the file is a directory recursively class this method.
			File subdir = new File(directory, fileName);
			if (subdir.isDirectory()) {
				classes.addAll(processDirectory(subdir, pkgname + '.' + fileName));
			}
		}
		return classes;
	}

	private static Class<?> loadClass(String className) {
		try {
			// return a class based on a strong name from current class loader
			return Class.forName(className);
		} catch (ClassNotFoundException e) {
			throw new ReflectorException("Unexpected exception loading class '" + className + "'", e);
		}
	}

	/**
	 * Given a jar file's URL and a package name returns all classes within jar
	 * file.
	 * 
	 * @param resource
	 * @param pkgname
	 */
	public static List<Class<?>> processJarfile(URL resource, String pkgname) {
		List<Class<?>> classes = new ArrayList<>();
		// Turn package name to relative path to jar file
		String relPath = pkgname.replace('.', '/');
		String resPath = resource.getPath();
		String jarPath = resPath.replaceFirst("[.]jar[!].*", ".jar").replaceFirst("file:", "");

		try (JarFile jarFile = new JarFile(jarPath)) {
			// attempt to load jar file

			// get contents of jar file and iterate through them
			Enumeration<JarEntry> entries = jarFile.entries();
			while (entries.hasMoreElements()) {
				JarEntry entry = entries.nextElement();

				// Get content name from jar file
				String entryName = entry.getName();
				String className = null;

				// If content is a class save class name.
				if (entryName.endsWith(CLASS_SUFFIX) && entryName.startsWith(relPath)
						&& entryName.length() > (relPath.length() + "/".length())) {
					className = entryName.replace('/', '.').replace('\\', '.').replace(CLASS_SUFFIX, "");
				}

				// If content is a class add class to List
				if (className != null) {
					classes.add(loadClass(className));
				}
			}
		} catch (IOException e) {
			throw new ReflectorException("Unexpected IOException reading JAR File '" + jarPath + "'", e);
		}
		return classes;
	}
	

	public static class ReflectorException extends RuntimeException {

		private static final long serialVersionUID = 909384213793458361L;

		public ReflectorException() {
			super();
		}

		public ReflectorException(String message) {
			super(message);
		}

		public ReflectorException(String message, Throwable cause) {
			super(message, cause);
		}

		public ReflectorException(Throwable cause) {
			super(cause);
		}

		protected ReflectorException(String message, Throwable cause, boolean enableSuppression,
				boolean writableStackTrace) {
			super(message, cause, enableSuppression, writableStackTrace);
		}
	}
}
