import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import scraper.FactoryScraper;
import scraper.SuperScraper;
//*[@id="col1"]/div/table/tbody/tr/td/font/text()


public class Count {
    static int count=0;
	static boolean DEBUG=false;
	static boolean DEBUGException=false;
	static boolean GENERATE=true;
	static int failureConnect=0;
	public static void main(String [] args) throws FileNotFoundException, IOException {
		//BufferedReader reader = new BufferedReader(new FileReader("/home/luigi/Scrivania/article.xml"));
	    //BufferedReader reader = new BufferedReader(new FileReader("/home/luigi/Scrivania/jdispla_dblp.xml"));
		//BufferedReader reader = new BufferedReader(new FileReader("/home/luigi/Scrivania/dblp.xml"));
		//BufferedReader reader = new BufferedReader(new FileReader("/home/luigi/Scrivania/jkdb_dblp.xml"));
		//BufferedReader reader = new BufferedReader(new FileReader("/home/luigi/Scrivania/j.combind_dblp.xml"));
		//BufferedReader reader = new BufferedReader(new FileReader("/home/luigi/Scrivania/IJIEM_dblp.xml"));
		//BufferedReader reader = new BufferedReader(new FileReader("/home/luigi/Scrivania/mio_dblp.xml"));
		BufferedReader reader = new BufferedReader(new FileReader("/home/luigi/Scrivania/mio_dblp(2).xml"));
		String line = reader.readLine();

		FileWriter w;
		w=new FileWriter("/home/luigi/Scrivania/output.xml");

		//ricopiaArticoli(reader,line,w);
 
		parsing(reader,line,w);

	}

	//Metodo che fa il parsing e genera l'xml contenente anche abstract e topic
	private static void parsing(BufferedReader reader, String line, FileWriter w) throws IOException {
		int nArticoli=0;
		SuperScraper scraper;
		FactoryScraper f = new FactoryScraper();

		while(line!=null && nArticoli<=5000) {

			// contiene l'URL dell'articolo la prendiamo e lo passiamo alla funzione che estrae abstract e parolechiave
			if(line.contains("<title>")){
				++nArticoli;
				scraper = f.createScraper(line);
				scraper.scrape(w, line);
			}
			if(GENERATE)
				w.write(line+"\n");

			line = reader.readLine();
		}


		w.flush();
		
		System.out.println(nArticoli);

	}

	//Metodo usato per creare un nuovo xml contenente solo gli articoli con URI valida "dx.doi.org"
	private static void ricopiaArticoli(BufferedReader reader, String line, FileWriter w) throws IOException {
		boolean copia=false;
		String articolo = "";

		while(line!=null) {

			if(line.contains("<ee>")&& line.contains("dx.doi.org") && (/*line.contains("s00") || line.contains("BF") ||*/ line.contains("IJIEM.") || line.contains("j.displa.") || line.contains("jkdb") || line.contains("jdkb") || line.contains("j.compind"))){
				copia=true;
				articolo+=line+"\n";
			}
			else
				if(line.contains("</article>") && !copia){
					articolo="";
					
				}
				else
					if(line.contains("</article>") && copia){
						copia=false;

						articolo+=line+"\n";
						w.write(articolo);
						articolo="";
					}

					else
						articolo+=line+"\n";
			line = reader.readLine();
		}
		w.flush();


	}

}
