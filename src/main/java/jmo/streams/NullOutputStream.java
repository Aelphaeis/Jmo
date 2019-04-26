package jmo.streams;

import java.io.OutputStream;

/**
 * @author morain
 */
public final class NullOutputStream extends OutputStream {

	public static final NullOutputStream INSTANCE = new NullOutputStream();

	@Override
	public void write(int b) {
		// Do nothing
	}

	@Override
	public void write(byte[] b, int offset, int length) {
		// do nothing
	}
	
	private NullOutputStream() {
		//We don't really need people creating instances of this.
	}
}
