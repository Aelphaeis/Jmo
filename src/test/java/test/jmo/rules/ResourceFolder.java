package test.jmo.rules;
import java.io.File;
import java.util.Objects;

import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResourceFolder extends TestWatcher implements TestRule {
	private static final Logger logger = LoggerFactory.getLogger(ResourceFolder.class);

	public static final Presentation DEFAULT_PRESENTATION = Presentation.FLAT;
	public static final String DEFAULT_BASE = "src/test/resources";

	private final Presentation presentation;
	private final File base;

	private Description description;
	
	public ResourceFolder() {
		this(DEFAULT_BASE);
	}
	
	public ResourceFolder(String path) {
		this(new File(path));
	}
	
	public ResourceFolder(File base) {
		this(base, DEFAULT_PRESENTATION);
	}
	
	public ResourceFolder(File base, Presentation presentation) {
		this.presentation = Objects.requireNonNull(presentation);
		this.base = Objects.requireNonNull(base);
		if(!base.isDirectory()) {
			throw new IllegalArgumentException("File must be a directory");
		}
	}
	
	@Override
	protected void starting(Description description) {
		this.description = Objects.requireNonNull(description);
	}

	
	public File getResourceFolder(){
		StringBuilder pathBuilder = new StringBuilder();
		pathBuilder.append(description.getTestClass().getName());
		pathBuilder.append(".").append(description.getMethodName());
		
		String path = pathBuilder.toString();
		if(Presentation.HIERARCHICAL.equals(presentation)) {
			path = path.replace(".", "/");
		}
		return new File(base, path);
	}
	
	public File createResourceFolder() {
		File file = getResourceFolder();
		String path = file.getAbsolutePath();
		if(file.mkdirs()) {
			logger.info("created test resouce folder [{}]", path);
		}
		else {
			logger.info("Test resouce folder already exists at [{}]", path);
		}
		return file;
	}
	
	public enum Presentation{
		HIERARCHICAL,
		FLAT
	}
}
