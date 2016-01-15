package scraper;

import java.io.FileWriter;
import java.io.IOException;
import java.net.SocketTimeoutException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public abstract class SuperScraper {

	protected boolean GENERATE = true;
	protected boolean DEBUG = false;
	protected boolean DEBUGException = false;
	protected int failureConnect = 0;

	public static Document returnPage(String URL ) throws IOException{
		org.jsoup.nodes.Document doc=null;
		try{
			doc = Jsoup.connect(URL).timeout(50*1000).get();
		}
		catch(SocketTimeoutException e){}
		return doc;
	}

	public abstract void scrape(FileWriter w, String line) throws IOException;
}
