package provaJena;

public class Keyword {

	private String Text;
	private double relevance;

	public Keyword(String text, double relevance) {
		super();
		Text = text;
		this.relevance = relevance;
	}

	public String getText() {
		return Text;
	}
	public void setText(String text) {
		Text = text;
	}
	public double getRelevance() {
		return relevance;
	}
	public void setRelevance(double relevance) {
		this.relevance = relevance;
	}



}
