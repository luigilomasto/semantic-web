<?php
		require_once( 'http_API.php' );
		require_once( 'query.php' );
		error_reporting(E_ERROR | E_PARSE);
		
		
		$http= new http_API();
		
		 if(isset($_REQUEST["title"])) {
		   $title = $_REQUEST["title"];
		}
		$prefixs = "
		
		PREFIX crm: <http://www.cidoc-crm.org/cidoc-crm/>
		PREFIX cs: <http://purl.org/vocab/changeset/schema#>
		prefix owl: <http://www.w3.org/2002/07/owl#>
		Prefix sd: <http://www.semanticweb.org/francesco/ontologies/2016/docs#>
		prefix xsd: <http://www.w3.org/2001/XMLSchema#>
		
		";
		$sparql = new query_sparql();
		//estraggo URL
		$query=$prefixs.$sparql->getUrlByTitle($title);
		$response = $http->sparqlQuery($query);
		$json = json_decode($response,true);
		$temp= $json["results"]["bindings"];
		foreach($temp as $t){
		  $url=$t["url_value"]["value"];
	}
	
	
		$json_result = json_encode($url);
		
		echo $json_result;

	
?>
