package jmo.util;

import java.util.Optional;

import javax.xml.namespace.QName;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public final class XmlUtils {

	public static Optional<Element> findFirst(String x, Document d) {
		return findFirst(x, d, XPathConstants.NODESET);
	}
	
	static Optional<Element> findFirst(String x, Document d, QName q){
		XPath xp = XPathFactory.newInstance().newXPath();
		NodeList nodes;
		try {
			nodes = (NodeList) xp.evaluate(x, d, q);
		}
		catch(XPathExpressionException e) {
			String err = "Illegal XPath Expression";
			throw new IllegalArgumentException(err);
		}

		Node n = nodes.getLength() == 0 ? null : nodes.item(0);
		return Optional.ofNullable((Element)n);
	}
	
	private XmlUtils () {
		// this is a utility class.
	}
}
