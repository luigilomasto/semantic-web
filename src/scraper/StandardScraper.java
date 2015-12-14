package scraper;

import java.io.FileWriter;
import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import org.jsoup.HttpStatusException;
import org.jsoup.UnsupportedMimeTypeException;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class StandardScraper extends SuperScraper {

	@Override
	public void scrape(FileWriter w, String line) throws IOException {
		if(GENERATE)
			w.write("<abstract>");

		String URL[]=line.split("<ee>");
		String URL2[]=URL[1].split("</ee>");
		try{
			Document page=returnPage(URL2[0]);

			//Estrae l'abstract
			Elements elemsAbstract = page.select("p.Para");
			for(Element elem: elemsAbstract){
				if(DEBUG)
					System.out.println(elem.text().toString());
				if(GENERATE)
					w.write(elem.text().toString());
			}
			w.write("</abstract>\n");

			//Estrae le keyword
			//Elements elemsKeyWord = page.select("ul.abstract-about-subject > li > a");
			Elements elemsKeyWord = page.select("ul.abstract-about-subject > li > a");
			for(Element elem: elemsKeyWord){
				if(DEBUG)
					System.out.println(elem.text().toString());
				if(GENERATE)
					w.write("<topic>");
				w.write(elem.text().toString());
				w.write("</topic>\n");
			}

		} 
		catch(ConnectException e){ failureConnect++;if(DEBUGException){System.out.print("Connessione rifiutata "+URL2[0]+" ");System.out.println(failureConnect);}}
		catch(HttpStatusException e){failureConnect++;if(DEBUGException){System.out.print("Status=404 "+URL2[0]+" ");System.out.println(failureConnect);}}
		catch(SocketTimeoutException e){failureConnect++;if(DEBUGException){System.out.print("Socket timeout "+URL2[0]+" ");System.out.println(failureConnect);}}
		catch(UnknownHostException e){failureConnect++;if(DEBUGException){System.out.print("dx.doi.org "+URL2[0]+" ");System.out.println(failureConnect);}}
		catch(UnsupportedMimeTypeException e){failureConnect++;if(DEBUGException){System.out.print("jsoup "+URL2[0]+" ");System.out.println(failureConnect);}}
	}

}