package keywords;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.alchemyapi.api.AlchemyAPI;

public class KeywordExtractor {

	public static Document extractKeyword(String text, String api_path) throws IOException, XPathExpressionException, SAXException, ParserConfigurationException {
		AlchemyAPI alchemyObj = AlchemyAPI.GetInstanceFromFile(api_path);
		return alchemyObj.TextGetRankedKeywords(text);
	}

}
