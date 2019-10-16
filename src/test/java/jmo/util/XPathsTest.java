package jmo.util;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;

import org.junit.Rule;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import jmo.serialization.Serializer;
import jmo.util.XPaths.DuplicateElementException;
import test.jmo.rules.ResourceFolder;

public class XPathsTest {
	
	@Rule
	public ResourceFolder folder = new ResourceFolder();
	
	@Test(expected=DuplicateElementException.class)
	public void distinct_multiple_exception() throws IOException {
		File sample = new File(folder.getResourceFolder(), "sample.xml");
		Document doc = Serializer.deserialize(sample);
		XPaths.distinct(doc, "breakfast_menu/food");
	}
	
	@Test
	public void distinct_uniqueXPath_found() throws IOException {
		File sample = new File(folder.getResourceFolder(), "sample.xml");
		Document doc = Serializer.deserialize(sample);
		Element e = XPaths.distinct(doc, "breakfast_menu/food[1]/name");
		assertEquals("Belgian Waffles", e.getTextContent());
	}
}
