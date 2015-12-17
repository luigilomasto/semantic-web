import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.UnsupportedMimeTypeException;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import scraper.JDisplaScraper;
import scraper.JkdbScraper;
import scraper.StandardScraper;
import scraper.SuperScraper;



public class Count {

	static boolean DEBUG=false;
	static boolean DEBUGException=false;
	static boolean GENERATE=true;
	static int failureConnect=0;
	public static void main(String [] args) throws FileNotFoundException, IOException {
		//BufferedReader reader = new BufferedReader(new FileReader("/home/luigi/Scrivania/article.xml"));
		//BufferedReader reader = new BufferedReader(new FileReader("/home/luigi/Scrivania/mio_dblp.xml"));
		//BufferedReader reader = new BufferedReader(new FileReader("/home/luigi/Scrivania/dblp.xml"));
		BufferedReader reader = new BufferedReader(new FileReader("/home/luigi/Scrivania/jkdb_dblp.xml"));
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

		while(line!=null && nArticoli<=5000) {

			// contiene l'URL dell'articolo la prendiamo e lo passiamo alla funzione che estrae abstract e parolechiave
			if(line.contains("<title>"))
				++nArticoli;
			if(line.contains("<ee>")){
				if(line.contains("j.displa.")){
					scraper = new JDisplaScraper();
				}
				else 
					if(line.contains("jkdb")){
						scraper = new JkdbScraper();
					}
					else{
						scraper = new StandardScraper();
					}
				scraper.scrape(w, line);
			}
			if(GENERATE)
				w.write(line+"\n");

			line = reader.readLine();
		}


		w.flush();

	}

	//Metodo usato per creare un nuovo xml contenente solo gli articoli con URI valida "dx.doi.org"
	private static void ricopiaArticoli(BufferedReader reader, String line, FileWriter w) throws IOException {
		boolean copia=false;
		String articolo = "";

		while(line!=null) {

			if(line.contains("<ee>")&& line.contains("dx.doi.org") && line.contains("jkdb")  /*(line.contains("j.displa.") || line.contains("jkdb") || line.contains("j.compind") || line.contains("IJIEM."))*/){
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
