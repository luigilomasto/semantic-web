package keywords;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class scraperForKeyword {


	private static BufferedReader reader;
	private static int numeroRigheLette;
	private static int nArticoli;

	public static void main(String [] args) throws IOException, XPathExpressionException, SAXException, ParserConfigurationException{
		numeroRigheLette=0;
		nArticoli = 0;
		boolean abstractB=false;
		reader = new BufferedReader(new FileReader("/home/luigi/git/solid-memory/lib/datasetWithAbstract/dataset1.xml"));


		String line = reader.readLine();
		String abstract_Text="";


		FileWriter w;
		w=new FileWriter("/home/luigi/git/solid-memory/lib/datasetWithKeyWord/dataset1.xml");


		while(line!=null) {
			++numeroRigheLette;

			if(line.contains("<article")){
				++nArticoli;
				w.write(line+"\n");
				abstractB=false;
			}
			else
				if(line.contains("<abstract>")){
					abstract_Text+=line;
					abstractB=true;

				}
				else if(abstractB && (line.contains("<ee>"))|| line.contains("<topic>")|| line.contains("<keyword>")){
					abstractB=false;
					abstract_Text=abstract_Text.replace("<abstract>","");
					abstract_Text=abstract_Text.replace("</abstract>","");
					if(!abstract_Text.equals("")){
						//System.out.println(abstract_Text);
						//strano o.o a me funzionava, provo a debuggare
						Document s=KeywordExtractor.extractKeyword(abstract_Text, "/home/luigi/git/solid-memory/lib/API_key/marco");
						System.out.println(s);
					}

					abstract_Text="";
					w.write(line+"\n");
				}
				else if(abstractB){
					//abstract_Text.concat(line);
					abstract_Text+=line;
				}

				else
					w.write(line+"\n");

			line = reader.readLine();
		}
		System.out.println(nArticoli);

	}


}
