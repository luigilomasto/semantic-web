	<?php
		require_once( 'http_API.php' );
		require_once( 'query.php' );
		error_reporting(E_ERROR | E_PARSE);
	
	class Article {
		public   $journal;
		public   $data;
		public   $keyword = array();
		public   $topic = array();
		public   $author= array();
		public   $url;
		public function setAuthor($author) {
		$this->author = $author;
		}
		public function setJournal($journal) {
		$this->journal = $journal;
		} 
		public function setData($data) {
		$this->data = $data;
		}
		public function setTopic($topic) {
		$this->topic = $topic;
		}
		public function setKeyword($keyword) {
		$this->keyword = $keyword;
		}
		public function setUrl($url){
			$this->url = $url;
			}
		}
		
		$http= new http_API();
	    $article=new Article;
	    $topics = array();
	    $keywords= array();
	    $author=array();
	    
	    if(isset($_REQUEST["title"])) {
		   $title = $_REQUEST["title"];
		}
		$sparql = new query_sparql();
		
		$prefixs = "
		PREFIX crm: <http://www.cidoc-crm.org/cidoc-crm/>
		PREFIX cs: <http://purl.org/vocab/changeset/schema#>
		prefix owl: <http://www.w3.org/2002/07/owl#>
		Prefix sd: <http://www.semanticweb.org/francesco/ontologies/2016/docs#>
		prefix xsd: <http://www.w3.org/2001/XMLSchema#>
		
		";
		//estraggo topic
		$query=$prefixs.$sparql->getTopicByTitle($title,$tn="?topic_value");
		$response = $http->sparqlQuery($query);
		$json = json_decode($response,true);
		$temp= $json["results"]["bindings"];
		$topics=array();
		foreach($temp as $t){
			array_push($topics,$t["topic_value"]["value"]);
		}
		$article->setTopic($topics);
		
		//estraggo keyword
		$query=$prefixs.$sparql->getKeywordsByTitle($title,'?keyword_value');
		$response = $http->sparqlQuery($query);
		$json = json_decode($response,true);
		$temp= $json["results"]["bindings"];
		$Keywords=array();
		foreach($temp as $t){
			array_push($keywords,$t["keyword_value"]["value"]);
			
		}
		$article->setKeyword($keywords);
		
		//estraggo journal e anno
		$query=$prefixs.$sparql->getJournalAndAnnoByArticle($title);
		$response = $http->sparqlQuery($query);
		$json = json_decode($response,true);
		$temp= $json["results"]["bindings"];
		foreach($temp as $t){
		  $article->setJournal($t["journal"]["value"]);
		
		$article->setData($t["anno"]["value"]);
	}
		//estraggo autori
		$query=$prefixs.$sparql->getAuthorByArticle($title);
		$response = $http->sparqlQuery($query);
		$json = json_decode($response,true);
		$temp= $json["results"]["bindings"];
		foreach($temp as $t){
			array_push($author,$t["author_name"]["value"]);
		}
		$article->setAuthor($author);
		
		//estraggo URL
		$query=$prefixs.$sparql->getUrlByTitle($title);
		$response = $http->sparqlQuery($query);
		$json = json_decode($response,true);
		$temp= $json["results"]["bindings"];
		foreach($temp as $t){
		  $article->setUrl($t["url_value"]["value"]);
	}
		
		$json_result = json_encode($article);
		
		echo $json_result;
	
	?>
	
	
