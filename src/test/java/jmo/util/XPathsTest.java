package jmo.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import jmo.serialization.Serializer;
import jmo.util.XPaths.DuplicateElementException;
import test.jmo.rules.ResourceFolder;

public class XPathsTest {
	
	private static final String SAMPLE_NAME = "sample.xml";
	
	@Rule
	public ResourceFolder folder = new ResourceFolder();

	@Test
	public void query_multi_allReturned() throws IOException {
		String [] expected = new String[5];
		expected[0] = "Belgian Waffles";
		expected[1] = "Strawberry Belgian Waffles";
		expected[2] = "Berry-Berry Belgian Waffles";
		expected[3] = "French Toast";
		expected[4] = "Homestyle Breakfast";
		
		String query = "breakfast_menu/food/name";
		List<Element> e = XPaths.query(getSample(), query);
		List<String> results = e.stream()
				.map(Element::getTextContent)
				.collect(Collectors.toList());
		
		assertThat(results, Matchers.contains(expected));
	}
	
	@Test
	public void first_xpath_found() throws IOException {
		String query = "breakfast_menu/food[1]/name";
		Element e = XPaths.first(getSample(), query);
		assertEquals("Belgian Waffles", e.getTextContent());
	}
	
	@Test
	public void first_xpath_notFound() throws IOException {
		String query = "breakfast_menu/food[7]/name";
		Element e = XPaths.first(getSample(), query);
		assertNull(e);
	}
	
	@Test
	public void distinct_uniqueXPath_found() throws IOException {
		String query = "breakfast_menu/food[1]/name";
		Element e = XPaths.distinct(getSample(), query);
		assertEquals("Belgian Waffles", e.getTextContent());
	}
	
	@Test
	public void distinct_uniqueXPath_notFound() throws IOException {
		String query = "breakfast_menu/food[7]/name";
		Element e = XPaths.distinct(getSample(), query);
		assertNull(e);
	}
	
	@Test(expected = DuplicateElementException.class)
	public void distinct_multiple_exception() throws IOException {
		XPaths.distinct(getSample(), "breakfast_menu/food");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void distinct_illegalPath_iae() {
		XPaths.distinct(null, "//%#");
	}
	
	private Document getSample() throws IOException {
		File sample = new File(folder.getResourceFolder(), SAMPLE_NAME);
		return Serializer.deserialize(sample);
	}
	
}
