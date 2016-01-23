	<?php
	require_once( 'http_API.php' );
	require_once( 'query.php' );
	error_reporting(E_ERROR | E_PARSE);
	
	class Article {
	public   $id;
	public   $keyword = array();
	public   $topic = array();
	public function setTitle($title) {
	$this->title = $title;
	}
	public function setId($id) {
	$this->id = $id;
	} 
	public function setKeyword($keyword) {
	$this->keyword = $keyword;
	}
	public function setTopic($topic) {
	$this->topic = $topic;
	}
	}
	
	
	$http= new http_API();
	
	
	$year = null;
	$topics = array();
	$article1 = array();
	$article2 = array();
	$article3 = array();
	
	$control=0;
	$equalsTopics = array();
	
	
	if(isset($_REQUEST["title"])){ // Se è settato il titolo
	$title = $_REQUEST["title"];
	if(isset($_REQUEST["topics"]))
	$topics = $_REQUEST["topics"];
	if(isset($_REQUEST["keywords"]))
	$keywords = $_REQUEST["keywords"];
	
	$sparql = new query_sparql();
	$prefixs = "
	PREFIX crm: <http://www.cidoc-crm.org/cidoc-crm/>
	PREFIX cs: <http://purl.org/vocab/changeset/schema#>
	prefix owl: <http://www.w3.org/2002/07/owl#>
	Prefix sd: <http://www.semanticweb.org/francesco/ontologies/2016/docs#>
	prefix xsd: <http://www.w3.org/2001/XMLSchema#>
	
	";
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//A PARTIRE DAL TITOLO RESTITUISCE TOPIC E KEYWORD
	if(!isset($title)){
	$query=$prefixs.$sparql->getTopicByTitle($title,$tn="?topic_value");
	$response = $http->sparqlQuery($query);
	$json = json_decode($response,true);
	$temp= $json["results"]["bindings"];
	foreach($temp as $t){
		array_push($topics,$t["topic_value"]["value"]);
	}
}
	
	if($control==1){//STAMPA I TOPIC DELL ARTICOLO DATO IN INPUT
	echo"----------------------TOPIC----------------------<br>";
	foreach($topics as $t){
		echo $t;
		echo "</br>";
	}
	}
	if(sizeof($topics)>0){
	$query=$prefixs.$sparql->getArticlesByTopics($topics,$title,"?title_value","?topic_value","?count");
	
	$response = $http->sparqlQuery($query);
	$json = json_decode($response,true);
	$temp= $json["results"]["bindings"];
	
	
	foreach($temp as $t){// ARRAY ASSOCIATIVO TITOLO_ARTICOLO -> NUMERO TOPIC
	        $article1[$t["title_value"]["value"]] = $t["count"]["value"];
	}
	}
	

	
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	 //A PARTIRE DAL TITOLO RESTITUISCE ALLA FINE UNA LISTA DI ARTICOLI CON LA CORRELAZIONE OTTENUTA DALLE KEYWORD IN COMUNE
	 if(!isset($keywords)){
	$query=$prefixs.$sparql->getKeywordsByTitle($title,'?keyword_value');
	$response = $http->sparqlQuery($query);
	$json = json_decode($response,true);
	$temp= $json["results"]["bindings"];
	
	$Keywords=array();
	foreach($temp as $t){
		array_push($Keywords,$t["keyword_value"]["value"]);
	}
}
	
	
	if($control==1){//STAMPA Le KEYWORDS DELL ARTICOLO DATO IN INPUT
	echo"----------------------KEYWORD-----------------------<br>";
	foreach($Keywords as $t){
		echo $t;
		echo "</br>";
	}
	}
	
	if(sizeof($Keywords)>1){
	$query=$prefixs.$sparql->getArticlesByKeywords($Keywords, $title);
	
	$response = $http->sparqlQuery($query);
	$json = json_decode($response,true);
	$ArticlesByKeywords= $json["results"]["bindings"];
	$id=1;	
	
	// per ogni articolo estrae titolo,id, insieme di keyword in comune e insieme di topic in comune, crea un oggetto e lo inserisce in un array di articoli
	if(sizeof($ArticlesByKeywords)>1){
	foreach($ArticlesByKeywords as $t){
		$arrayKeyword = array();
	    $arrayTopic =array();
	        $art[$t["title_value"]["value"]] = new Article;
	        $art[$t["title_value"]["value"]]->setId($t["title_value"]["value"]);
	        $art[$t["title_value"]["value"]]->setTitle($t["title_value"]["value"]);
	        $id++;
	        $query=$prefixs.$sparql->getEqualsKeywordByArticle($t["title_value"]["value"],$Keywords);
	        $response = $http->sparqlQuery($query);
	        $json = json_decode($response,true);
	        $equalsKeywords= $json["results"]["bindings"];
	        if(sizeof($equalsKeywords)>0)
	        foreach($equalsKeywords as $ek)
	          $arrayKeyword[$ek["t"]["value"]]=$ek["r"]["value"];
	        
	        if(sizeof($topics)>0){
	        $query=$prefixs.$sparql->getEqualsTopicByArticle($t["title_value"]["value"],$topics);
	        
	        $response = $http->sparqlQuery($query);
	        $json = json_decode($response,true);
	        $equalsTopics= $json["results"]["bindings"];
	        foreach($equalsTopics as $top)
	             array_push($arrayTopic,$top["topicValue"]["value"]);
	          
	         
	        }
	        
	        if(sizeof($equalsTopics)>0) 
	$art[$t["title_value"]["value"]]->setTopic($arrayTopic); 
	
	$art[$t["title_value"]["value"]]->setKeyword($arrayKeyword);
	$article3[$id] = $art[$t["title_value"]["value"]];
	}
	
	
	}
	}
	
	
	
	/*foreach($article2 as $x => $x_value){
	    echo $x . " " . $x_value;
	    echo "<br>";
	}*/
	
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/*if(sizeof($topics)>0){
	$article3=$article1;
	
	foreach($article2 as $x => $x_value){
	    if(isset($article1[$x]))
	      $article3[$x]=$article1[$x]+($article2[$x]);
	   
	}}
	else
	{
	$article3=$article2;
	}
	
	arsort($article3);
	echo"<br>-------------------------ARTICLES-----------------------------<br>";
	
	foreach($article3 as $x => $x_value){
	    echo "<br>".$x . " " . $x_value; 
	}
	
	
	/*foreach($article3 as $x => $x_value){
	$art[$x] = new Article;
	$art[$x]->setTitle($x);
	$art[$x]->setRelevance($x_value);
	}*/
	
	
	$response = array();
	
	if(sizeof($topics)>0)
	$response["topics"]=$topics;
	
	if(sizeof($Keywords)>0)
	$response["keywords"]=$Keywords;
	
	$response["articles"]=$article3;
	
	$json_result = json_encode($response);
	echo $json_result;
	}
	
	
	
	?>
	
