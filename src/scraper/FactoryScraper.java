package scraper;

public class FactoryScraper {
	
	public SuperScraper createScraper(String line){
		SuperScraper toReturn = null;
		
		if(line.contains("<ee>")){
			if(line.contains("j.displa.") || line.contains("j.compind")){
				toReturn = new JDisplaScraper();
			}
			else 
				if(line.contains("jkdb")){
					toReturn = new JkdbScraper();
				}else 
					if(line.contains("IJIEM.")){
						toReturn = new IJIEMScraper();
					}
				else{
					toReturn = new StandardScraper();
				}
		}
		return toReturn;
	}
}
