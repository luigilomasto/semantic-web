package keywords;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class scraperForKeyword {
	private static String getStringFromDocument(Document doc) throws IOException {
		try {
			DOMSource domSource = new DOMSource(doc);
			StringWriter writer = new StringWriter();
			StreamResult result = new StreamResult(writer);
			String toReturn="";
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transformer = tf.newTransformer();
			transformer.transform(domSource, result);

			String [] split=writer.toString().split("<keyword>");

			for(int i=1;i<split.length;i++){
				String []relevance1=split[i].split("<relevance>");
				String []relevance2=relevance1[1].split("</relevance>");
				String []text1=split[i].split("<text>");
				String []text2=text1[1].split("</text>");

				double relevance = Double.parseDouble(relevance2[0]);
				if(relevance*10 >= 6){
					toReturn+="<keyword>\n";
					toReturn+="<relevance>"+relevance+"</relevance>\n";
					toReturn+="<text>"+text2[0]+"</text>\n";
					toReturn+="</keyword>\n";
				}
			}

			return toReturn;
		} catch (TransformerException ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public static void main(String [] args) throws IOException, XPathExpressionException, SAXException, ParserConfigurationException{
		int nArticoli = Integer.parseInt(args[0]); 
		int numeroRigheLette;
		int articoliGialetti=nArticoli; 
		int articoliDaLeggere=Integer.parseInt(args[1]);
		boolean abstractB=false;
		BufferedReader reader = new BufferedReader(new FileReader(args[2]));
		String line = reader.readLine();
		String abstract_Text="";
		FileWriter w=new FileWriter(args[3]); 
		String api_path = args[4];
		int counter=0;

		while(counter<=nArticoli){
			line = reader.readLine();
			if(line.contains("<article"))
				++counter;
		}
		while(line!=null && nArticoli<articoliDaLeggere+articoliGialetti) {
			if(line.contains("<article")){
				++nArticoli;
				if(nArticoli<articoliDaLeggere+articoliGialetti){
					w.write(line+"\n");}
				abstractB=false;
			}
			else
				if(line.contains("<abstract>")){
					abstract_Text+=line;
					abstractB=true;

				}
				else if(abstractB && line.contains("</abstract>")){
					abstractB=false;
					abstract_Text=abstract_Text.replace("<abstract>","");
					abstract_Text=abstract_Text.replace("</abstract>","");
					if(!abstract_Text.equals("")){
						try{
							Document s=KeywordExtractor.extractKeyword(abstract_Text, api_path);
							String keywordDocument = getStringFromDocument(s);
							w.write(keywordDocument);
						}
						catch(IOException e){
							System.out.println("Error making API call: unsupported-text-language.");
						}
					}
					abstract_Text="";
				}
				else if(abstractB){
					abstract_Text+=line;
				}
				else
					w.write(line+"\n");
			line = reader.readLine();
		}
		w.close();
		System.out.println("#Articoli letti: "+ --nArticoli);
	}
}
