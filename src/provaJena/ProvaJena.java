package provaJena;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.ontology.OntProperty;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.shared.BadURIException;
import org.apache.jena.util.FileManager;
import org.apache.jena.vocabulary.OWL;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;



public class ProvaJena {

	/*Ontolology URI*/
	private final static String SOURCE_URI = "http://www.semanticweb.org/francesco/ontologies/2016/docs.owl";
	/*Path file on disk*/
	private final static String SOURCE_PATH = "C:/Users/Francesco/Documenti/Università/Magistrale/Web_Semantico/Progetto/docs.owl";
	/*Namespace*/
	private final static String NS = "http://www.semanticweb.org/francesco/ontologies/2016/docs#";
	/*CRM namespace*/
	private final static String CRM_NS = "http://www.cidoc-crm.org/cidoc-crm/";
	/*Dataset path*/
	private final static String DATA_PATH = "C:/Users/Francesco/Documenti/Università/Magistrale/Web_Semantico/Progetto/dataset/";

	/*Control string*/
	private final static String ARTICLE = "</article>";
	private final static String AUTHOR = "<author>";
	private final static String TITLE = "<title>";
	private final static String YEAR = "<year>";
	//private final static String URL = "<url>";
	//private final static String ABSTRACT = "<abstract>";
	private final static String TOPIC = "<topic>";

	/*Information variables*/
	private static ArrayList<String> row_authors = new ArrayList<String>();
	private static String row_title ="";
	private static String row_time = "";
	private static ArrayList<String> row_topics = new ArrayList<String>();
	/*Global model*/
	private static OntModel model = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM );
	static Model g;
	public static void main(String[] args) throws IOException, UnsupportedEncodingException {
		
		FileWriter out = new FileWriter("C:/Users/Francesco/Desktop/docs_ontology.owl");
		readExternalModel(model);
		int count=0;
		
		if(Key.SHOW_MODEL){
			//PrintWriter writer = new PrintWriter("C:/Users/Francesco/Desktop/out.txt", "UTF-8");
			Iterator<OntClass> allClasses = model.listClasses();
			Iterator<OntProperty> allProp = model.listAllOntProperties();
			while(allClasses.hasNext()){
				System.out.println(allClasses.next().toString());
				//writer.println(allClasses.next().toString());
			}
			while(allProp.hasNext()){
				System.out.println(allProp.next().toString());
				//writer.println(allProp.next().toString());
			}
			//writer.close();
		}
		
		/*Select classes*/
		OntClass author = model.getOntClass( NS + "Author" );
		OntClass production = model.getOntClass(CRM_NS+"E12_Production");
		OntClass e_document = model.getOntClass(CRM_NS+"E31_Document");
		//OntClass place = model.getOntClass(CRM_NS+"E53_Place");
		OntClass time_span = model.getOntClass(CRM_NS+"E52_Time-Span");
		OntClass title = model.getOntClass(CRM_NS+"E35_Title");
		//OntClass appellation = model.getOntClass("E41_Appellation");
		OntClass topic = model.getOntClass(NS+"Topic");
		
		/*Select properties*/
		OntProperty carried = model.getOntProperty(CRM_NS+ "P14_carried_out_by" );
		OntProperty hasProduced = model.getOntProperty(CRM_NS + "P108_has_produced");
		//OntProperty refersTo = model.getOntProperty(CRM_NS+"P67_refers_to");
		OntProperty has_time_span = model.getOntProperty(CRM_NS+"P4_has_time-span");
		//OntProperty took_place_at = model.getOntProperty(CRM_NS+"P7_took_place_at");
		OntProperty hasTitle = model.getOntProperty(CRM_NS+"P102_has_title");
		//OntProperty isIdentifiedBy = model.getOntProperty(CRM_NS+"P1_is_identified_by");
		OntProperty name = model.getOntProperty(NS+"name");
		OntProperty bears_features =model.getOntProperty(CRM_NS+"P56_bears_feature");
		
		/*XML parser
		SAXBuilder builder = new SAXBuilder();
	

		Document document = new Document();
		try {
			document = builder.build(new File(DATA_PATH+"dataset1.xml"));
		} catch (JDOMException e) {
			System.err.println("Problems in parsing file");
			e.printStackTrace();
		}
		/*Document root
		Element rootElement = document.getRootElement();
		Element article = rootElement.getChild("article");
		Iterator art_info = article.getChildren().iterator();
		while(art_info.hasNext()){
			Element att = (Element) art_info.next();
			if(Key.PRINT_DOC)
				System.out.println(att.toString());
		}

		 */
		
		
		/*Reading XML File*/
		BufferedReader br = new BufferedReader(new FileReader(DATA_PATH+"dataset1.xml"));
		String line = br.readLine();
		while(line!=null){
			if(Key.PRINT_DOC)
				System.out.println(line);
		
			/*New aticle has to be added to the owl file*/
			if(line.startsWith(ARTICLE)){
				count++;
				Iterator<String> aut_it = row_authors.iterator();
				
				/*foreach author*/
				while(aut_it.hasNext()){
					/*Create individual*/
					String curr_aut = aut_it.next();
					Individual aut = model.createIndividual(NS+curr_aut,author);
					//Individual app = model.createIndividual(NS + curr_aut,appellation);
					Individual prod = model.createIndividual(NS+"Working at writing paper"+count,production);
					Individual doc = model.createIndividual(NS +""+count,e_document);
					Individual time = model.createIndividual(NS +row_time,time_span);
					//Individual e_place = model.createIndividual(NS+"",place);
					Individual e_title = model.createIndividual(NS+row_title,title);
					
					/*Adding comment*/
					aut.addComment("Instance of the "+count+" document","");
					
					
					/*Adding property*/
					aut.addProperty(name, URLDecoder.decode(curr_aut,"utf-8"));
					prod.addProperty(carried, aut);
					prod.addProperty(hasProduced, doc);
					prod.addProperty(has_time_span, time);
					//prod.addProperty(took_place_at, e_place);
					doc.addProperty(hasTitle, e_title);
						
					/*foreach topic*/
					Iterator<String> topic_int = row_topics.iterator();
					while(topic_int.hasNext()){
						String curr_topic = topic_int.next();
						Individual top = model.createIndividual(NS+curr_topic,topic);
						doc.addProperty(bears_features, top);
					}
					//g = model.add(model);

				}
				
				refreshVariables();
			
			}
			else if(line.startsWith(AUTHOR)){
				row_authors.add(extractInfo(line));
			}
			else if(line.startsWith(TITLE)){
				row_title = extractInfo(line);
			}
			else if(line.startsWith(YEAR)){
				row_time = extractInfo(line);
			}
			else if(line.startsWith(TOPIC)){
				row_topics.add(extractInfo(line));
			}
				
			line = br.readLine();
		}
		addIndividual(model,out);
		br.close();
		
		if(Key.SHOW_INDIVIDUALS){
			Iterator<Individual> allIndividuals;
			allIndividuals=model.listIndividuals();
			while(allIndividuals.hasNext()){
				String curr = URLDecoder.decode(allIndividuals.next().toString(),"utf-8");
				System.out.println(curr);
			}
		}
								
	}

	/**Re-initializes the variables**/
	private static void refreshVariables() {
		row_authors = new ArrayList<String>();
		row_title ="";
		row_time = "";
		row_topics = new ArrayList<String>();
	}

	/**Add a new individual in the owl file**/
	private static void addIndividual(OntModel moddel, FileWriter out) {
		if(Key.PRINT_ON_FILE){
			try{
				model.write(out,"RDF/XML");
		
			}
			catch(BadURIException e){
				e.printStackTrace();
			}
			
		}
	}

	/**Allows to import an external ontology model**/
	private static void readExternalModel(OntModel model){
		try {
			FileInputStream fin = new FileInputStream(SOURCE_PATH);
			model.read(fin, NS);
			}
			catch (Exception ex) {
			System.out.println(ex);
			}

	}

	/**Allows to import an external ontology model**/
	@SuppressWarnings("unused")
	private static void importExternalModel(OntModel model){

		/*Mapping between the resource uri and the physical path within the disk*/
		FileManager.get().getLocationMapper().addAltEntry(SOURCE_URI, SOURCE_PATH);
		/*Load external model*/
		Model myOntology = FileManager.get().loadModel( SOURCE_URI ); 
		/*Append as submodel on the global model*/
		model.addSubModel(myOntology);
		/*Set the prefix for the related Namespace*/
		model.setNsPrefix( "sd", NS );
		model.setNsPrefix("owl", OWL.NS);
	}
	
	/**Provides to extract the text between XML tags**/
	private static String extractInfo(String line){
		
		
		/*remove eventual html tags within the string*/
		line = Jsoup.clean(line, Whitelist.basic());

		/*utf-8 encoded is needed because of eventual illegal characters in the line
		 * ES: '#' is coded as '%23'*/
		try {
			line = URLEncoder.encode(line, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		return line;
	}
}
