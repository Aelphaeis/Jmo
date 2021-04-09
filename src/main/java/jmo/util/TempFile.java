package jmo.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Objects;

public class TempFile implements AutoCloseable {

	private final File tempFile;

	public TempFile() throws IOException {
		tempFile = File.createTempFile("temp-", "");
	}

	public TempFile(File file) {
		this.tempFile = Objects.requireNonNull(file);
	}

	public void touch() throws IOException {
		long now = System.currentTimeMillis();
		if (!tempFile.createNewFile() && !tempFile.setLastModified(now)) {
			String err = "Unable to update modification time of " + tempFile;
			throw new IOException(err);
		}
	}

	@Override
	public void close() throws IOException {
		Files.delete(tempFile.toPath());
	}
}
