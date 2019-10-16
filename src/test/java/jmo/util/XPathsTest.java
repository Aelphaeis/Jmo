package jmo.util;

import java.io.File;
import java.io.IOException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.junit.Rule;
import org.junit.Test;
import org.w3c.dom.Document;

import jmo.serialization.Serializer;
import jmo.util.XPaths.DuplicateElementException;
import test.jmo.rules.ResourceFolder;

public class XPathsTest {
	
	@Rule
	public ResourceFolder folder = new ResourceFolder();
	
	@Test(expected=DuplicateElementException.class)
	public void distinct_multiple_exception() throws IOException {
		File sample = new File(folder.getResourceFolder(), "sample.xml");
		Document document = Serializer.deserialize(sample);
		XPaths.distinct(document, "breakfast_menu/food");
	}
	

	
	private Document sample() throws ParserConfigurationException {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
		DocumentBuilder builder = dbf.newDocumentBuilder();
		Document document = builder.newDocument();
		return document;
	}
	
}
