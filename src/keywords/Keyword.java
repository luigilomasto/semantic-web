package keywords;

public class Keyword implements Comparable<Keyword> {

	  private final String stem;
	  private int frequency = 0;

	  public Keyword(String stem) {
	    this.stem = stem;
	  }
	  
	  public String toString(){
		  return stem + " - " + frequency;
	  }

	  public void add(String term) {
	    frequency++;
	  }

	  public int compareTo(Keyword o) {
	    // descending order
	    return Integer.valueOf(o.frequency).compareTo(frequency);
	  }

	  @Override
	  public boolean equals(Object obj) {
	    if (this == obj) {
	      return true;
	    } else if (!(obj instanceof Keyword)) {
	      return false;
	    } else {
	      return stem.equals(((Keyword) obj).stem);
	    }
	  }
}