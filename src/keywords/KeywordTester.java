package keywords;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.alchemyapi.api.AlchemyAPI;

public class KeywordTester {

	public static void main(String[] args) throws IOException, XPathExpressionException, SAXException, ParserConfigurationException {
		if(args.length == 0){
			return;
		}
		String test = args[0];
		
		AlchemyAPI alchemyObj = AlchemyAPI.GetInstanceFromFile("api_key.txt");

        // Extract topic keywords for a text string.
        Document doc = alchemyObj.TextGetRankedKeywords(test);
        System.out.println(getStringFromDocument(doc));
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
