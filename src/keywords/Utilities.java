package keywords;

import java.io.IOException;
import java.io.StringReader;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.NamedEntityTagAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;
import edu.stanford.nlp.util.Pair;
import intoxicant.analytics.coreNlp.StopwordAnnotator;

public class Utilities {

	public static <T> T find(Collection<T> collection, T example) {
	  for (T element : collection) {
	    if (element.equals(example)) {
	      return element;
	    }
	  }
	  collection.add(example);
	  return example;
	}
	
	public static List<Keyword> guessFromString(String input) throws IOException {
	    // hack to keep dashed words (e.g. "non-specific" rather than "non" and "specific")
	    input = input.replaceAll("-+", "-0");
	    // replace any punctuation char but apostrophes and dashes by a space
	    input = input.replaceAll("[\\p{Punct}&&[^'-]]+", " ");
	    // replace most common english contractions
	    input = input.replaceAll("(?:'(?:[tdsm]|[vr]e|ll))+\\b", "");
	    // lowercase input
	    input = input.toLowerCase();
	    
	    List<Keyword> keywords = new LinkedList<Keyword>();
	    
	    Properties props = new Properties();
	    props.setProperty("annotators", "tokenize, ssplit, pos, stopword, lemma");
        props.setProperty("customAnnotatorClass.stopword", "intoxicant.analytics.coreNlp.StopwordAnnotator");
	    StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
	    
	    Annotation document = new Annotation(input);
	    
	    pipeline.annotate(document);
	    
	    List<CoreMap> sentences = document.get(SentencesAnnotation.class);
	    
	    for(CoreMap sentence: sentences) {
    	  // traversing the words in the current sentence
    	  // a CoreLabel is a CoreMap with additional token-specific methods
		  for (CoreLabel token: sentence.get(TokensAnnotation.class)) {
		    // this is the text of the token
			String word = token.get(TextAnnotation.class);
			Pair<Boolean, Boolean> stopword = token.get(StopwordAnnotator.class);  
			if(!stopword.first){
			    String stem = token.get(LemmaAnnotation.class);
			    Keyword keyword = find(keywords, new Keyword(stem));
			    // add its corresponding initial token
			    keyword.add(stem);
			    // this is the POS tag of the token
			    String pos = token.get(PartOfSpeechAnnotation.class);
			    // this is the NER label of the token
			    String ne = token.get(NamedEntityTagAnnotation.class);
			}
		  }
		}	
	    // reverse sort by frequency
	    Collections.sort(keywords);
	
	    return keywords;
	} 
}
