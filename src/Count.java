import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import scraper.FactoryScraper;
import scraper.SuperScraper;

public class Count {
	public static void main(String [] args) throws FileNotFoundException, IOException {
		BufferedReader reader = new BufferedReader(new FileReader(args[1]));
		FileWriter w = new FileWriter(args[2]);

		parsing(reader,line,w);
		w.close();
		reader.close();
	}

	private static void parsing(BufferedReader reader, FileWriter w) throws IOException {
		int nArticoli=0;
		SuperScraper scraper;
		FactoryScraper f = new FactoryScraper();

		do{
			line = reader.readLine();
			if(line.contains("<article")){
				++nArticoli;
			}
			else
				if(line.contains("<ee>")){
					scraper = f.createScraper(line);
					scraper.scrape(w, line);
				}
			if(GENERATE)
				w.write(line+"\n");
		}while(line!=null && nArticoli<=5000);
		w.flush();
		System.out.println(nArticoli);
	}
}
