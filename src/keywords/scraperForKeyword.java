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

import org.apache.commons.logging.Log;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class scraperForKeyword {


	private static BufferedReader reader;
	private static int numeroRigheLette;
	private static int nArticoli;
	

	//restituisce la stringa contenente le keyword con rilevanza >0.5
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

				//estraggo text e relevance
				String []relevance1=split[i].split("<relevance>");
				String []relevance2=relevance1[1].split("</relevance>");
				String []text1=split[i].split("<text>");
				String []text2=text1[1].split("</text>");

				double relevance = Double.parseDouble(relevance2[0]);
				//prendo le keyword con relevance > 0.6
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
		numeroRigheLette=0;
		nArticoli = 3670; // questa variabile varia ad ogni lancio, il suo valore dipende dagli articoli già letti
		int articoliGialetti=nArticoli; //articoli già letti, viene usata come appoggio
		int articoliDaLeggere=900; // numero di articoli da leggere
		boolean abstractB=false;
		reader = new BufferedReader(new FileReader("lib/datasetWithAbstract/dataset1.xml"));


		String line = reader.readLine();
		String abstract_Text="";


		FileWriter w;
		// questa variabile varia ad ogni lancio, il suo valore dipende dagli articoli già letti
		w=new FileWriter("lib/datasetWithKeyWord/dataset2.xml"); //

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
			else// se inizia l'abstract setto il booleano a true
				if(line.contains("<abstract>")){
					abstract_Text+=line;
					abstractB=true;

				}//se l'abstract finisce incontro <ee> <topic> <keyword>
				else if(abstractB && line.contains("</abstract>")){
					abstractB=false;
					abstract_Text=abstract_Text.replace("<abstract>","");
					abstract_Text=abstract_Text.replace("</abstract>","");
					if(!abstract_Text.equals("")){

						//Document s=KeywordExtractor.extractKeyword(abstract_Text, "lib/api_key.txt");
						try{
						Document s=KeywordExtractor.extractKeyword(abstract_Text, "/home/luigi/git/solid-memory/lib/API_key/andrea");
						String keywordDocument = getStringFromDocument(s);
						w.write(keywordDocument);
						}
						catch(IOException e){
							System.out.println("errore strano:Error making API call: unsupported-text-language.");
						}

					}

					abstract_Text="";
					w.write(line+"\n");
				}//se sto nell'abstract
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
