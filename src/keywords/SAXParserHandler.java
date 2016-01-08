package keywords;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

public class SAXParserHandler extends DefaultHandler {

	private  Document doc;
	private String filePath;
	private boolean abstractFound;

	public SAXParserHandler(String filePath) {
		this.filePath = filePath;
		this.abstractFound = false;
	}

	public void setDocumentLocator(Locator locator) {
		// TODO Auto-generated method stub

	}

	public void startDocument() throws SAXException {
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder;
		try {
			docBuilder = docFactory.newDocumentBuilder();
			doc = docBuilder.newDocument();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} finally {

		}
	}

	public void endDocument() throws SAXException {
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer;
		try {
			transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(filePath));

			// Output to console for testing
			// StreamResult result = new StreamResult(System.out);

			transformer.transform(source, result);
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		} finally {
			System.err.println("You fucked up bro");
		}

	}

	public void startPrefixMapping(String prefix, String uri)
			throws SAXException {
		// TODO Auto-generated method stub

	}

	public void endPrefixMapping(String prefix) throws SAXException {
		// TODO Auto-generated method stub

	}

	public void startElement(String uri, String localName, String qName,
			Attributes atts) throws SAXException {
		if(qName.equalsIgnoreCase("abstract")) {
			this.abstractFound = true;
		} else {
			doc.createElement(qName);
		}
	}

	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		// TODO Auto-generated method stub

	}

	public void characters(char[] ch, int start, int length)
			throws SAXException {
		Document out = null;
		if(abstractFound) {
			try {
				out = KeywordExtractor.extractKeyword(new String(ch), "/Users/andreasolda/git/solid-memory/src/api_key_marco.txt");
			} catch (XPathExpressionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				abstractFound = false;
			}
			Node newNode = out.getElementsByTagName("keywords").item(0).cloneNode(true);
			// Transfer ownership of the new node into the destination document
			doc.adoptNode(newNode);
			// Make the new node an actual item in the target document
			doc.getDocumentElement().appendChild(newNode);
		} else {
			String text = new String(ch);
			doc.createTextNode(text);
		}

	}

	public void ignorableWhitespace(char[] ch, int start, int length)
			throws SAXException {
		// TODO Auto-generated method stub

	}

	public void processingInstruction(String target, String data)
			throws SAXException {
		// TODO Auto-generated method stub

	}

	public void skippedEntity(String name) throws SAXException {
		// TODO Auto-generated method stub

	}

}
