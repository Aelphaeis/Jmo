package jmo.util;

import java.io.File;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.junit.Rule;
import org.junit.Test;
import org.w3c.dom.Document;

import test.jmo.rules.ResourceFolder;

public class XPathsTest {
	
	@Rule
	public ResourceFolder folder = new ResourceFolder();
	
	@Test
	public void distinct_multiple_exception() {
		File sample = new File(folder.getResourceFolder(), "sample.xml");
	}
	

	
	private Document sample() throws ParserConfigurationException {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
		DocumentBuilder builder = dbf.newDocumentBuilder();
		Document document = builder.newDocument();
		return document;
	}
	
}
