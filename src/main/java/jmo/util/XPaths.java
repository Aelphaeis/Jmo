package jmo.util;

import java.util.ArrayList;
import java.util.List;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class XPaths {
	
	public static List<Element> query(Document d, String q) {
		NodeList list = listNodes(d, q);
		List<Element> result = new ArrayList<>();
		for (int i = 0; i < list.getLength(); i++) {
			result.add((Element) list.item(i));
		}
		return result;
	}
	
	public static Element distinct(Document doc, String query) {
		NodeList list = listNodes(doc, query);
		if (list.getLength() > 1) {
			String err = "More than one element meeting criteria";
			throw new DuplicateElementException(err);
		}
		return (Element) (list.getLength() == 0 ? null : list.item(0));
	}
	
	public static Element first(Document doc, String query) {
		NodeList list = listNodes(doc, query);
		return (Element) (list.getLength() == 0 ? null : list.item(0));
	}

	private static NodeList listNodes(Document doc, String query) {
		XPath xp = XPathFactory.newInstance().newXPath();
		try {
			return (NodeList) xp.evaluate(query, doc, XPathConstants.NODESET);
		} catch (XPathExpressionException e) {
			String err = "Illegal XPath Expression";
			throw new IllegalArgumentException(err);
		}
	}
	
	public static class DuplicateElementException extends RuntimeException {
		
		private static final long serialVersionUID = 1L;
		
		public DuplicateElementException(String message) {
			super(message);
		}
	}
	
	private XPaths() {
		// utility class
	}
}
