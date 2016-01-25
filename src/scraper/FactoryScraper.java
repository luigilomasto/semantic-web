package scraper;

public class FactoryScraper {

	private SuperScraper jdispla;
	private SuperScraper jkdb;
	private SuperScraper ijiem;
	private SuperScraper standard;

	public FactoryScraper(){
		jdispla = new JDisplaScraper();
		jkdb = new JkdbScraper();
		ijiem = new IJIEMScraper();
		standard = new StandardScraper();
	}

	public SuperScraper createScraper(String line){

		if(line.contains("<ee>")){
			if(line.contains("j.displa.") || line.contains("j.compind")){
				return jdispla;
			}
			else{
				if(line.contains("jkdb")){
					return jkdb;
				}else if(line.contains("IJIEM.")){
					return ijiem;
				}
				else{
					return standard;
				}
			}
		}
		return null;
	}
}
