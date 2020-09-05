package jmo.util;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Objects;
import java.util.function.Function;

import javax.imageio.ImageIO;

public class BufferedImages {

	public static BufferedImage fromClasspath(String name) {
		Objects.requireNonNull(name);

		URL url = BufferedImages.class.getClassLoader().getResource(name);
		if (url == null) {
			String fmt = "[%s] is not a resource";
			throw new IllegalArgumentException(String.format(fmt, name));
		}

		try (InputStream stream = url.openStream()) {
			return ImageIO.read(stream);
		}
		catch (IOException e) {
			String fmt = "unable to parse argument [%s] at [%s]";
			String err = String.format(fmt, name, url);
			throw new IllegalArgumentException(err, e);
		}
	}
	public static <T> T fromClasspath(String name, Function<InputStream, T> f) {
		Objects.requireNonNull(name);

		URL url = BufferedImages.class.getClassLoader().getResource(name);
		if (url == null) {
			String fmt = "[%s] is not a resource";
			throw new IllegalArgumentException(String.format(fmt, name));
		}

		try (InputStream stream = url.openStream()) {
			return f.apply(stream);
		}
		catch (IOException e) {
			String fmt = "error processing stream [%s] at [%s]";
			String err = String.format(fmt, name, url);
			throw new IllegalArgumentException(err, e);
		}
	}
}
