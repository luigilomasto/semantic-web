package keywords;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Document;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.SAXException;

public class TESTKeywordExtractor {
	public static void main(String[] args) throws XPathExpressionException, IOException, SAXException, ParserConfigurationException {

		SAXParserFactory factory = SAXParserFactory.newInstance();
	    SAXParser saxParser = factory.newSAXParser();
	    
	    DefaultHandler handler = new SAXParserHandler("output-test.xml");
	    
	    saxParser.parse(new File("/Users/andreasolda/git/solid-memory/src/dataset-test.xml"), handler);
		
//		String text = "A schema is a collection of finite subsets of a set. One can partition the class of all schemas into tree schemas and cyclic schemas. This partitioning has been extensively studied in relational database theory. In this paper we examine the impact of tree schemas on some NP-complete problems. We show that tree schema instances can be efficiently solved (i.e., polynomially) for certain NP-complete problems.";
//		Document doc = KeywordExtractor.extractKeyword(text, "/Users/andreasolda/git/solid-memory/src/api_key_marco.txt");
//		
//		System.out.println(getStringFromDocument(doc));
	}

	private static String getStringFromDocument(Document doc) {
		try {
			DOMSource domSource = new DOMSource(doc);
			StringWriter writer = new StringWriter();
			StreamResult result = new StreamResult(writer);

			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transformer = tf.newTransformer();
			transformer.transform(domSource, result);

			return writer.toString();
		} catch (TransformerException ex) {
			ex.printStackTrace();
			return null;
		}
	}
}
