package jmo.streams;

import java.io.IOException;
import java.io.OutputStream;

/**
 * 
 * @author morain
 *
 */
public class NullOutputStream extends OutputStream {
	
	public static final NullOutputStream INSTANCE = new NullOutputStream();

	@Override
	public void write(int b) throws IOException {
		// Do nothing 
	}
}
