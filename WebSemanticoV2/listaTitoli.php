	<?php
	require_once( 'http_API.php' );
	require_once( 'query.php' );
	error_reporting(E_ERROR | E_PARSE);
	
	$http= new http_API();
	
	$sparql = new query_sparql();
	$prefixs = "
	PREFIX crm: <http://www.cidoc-crm.org/cidoc-crm/>
	PREFIX cs: <http://purl.org/vocab/changeset/schema#>
	prefix owl: <http://www.w3.org/2002/07/owl#>
	Prefix sd: <http://www.semanticweb.org/francesco/ontologies/2016/docs#>
	prefix xsd: <http://www.w3.org/2001/XMLSchema#>
	
	";
	$title = array();
	$query=$prefixs.$sparql->getTitle();
	$response = $http->sparqlQuery($query);
	$json = json_decode($response,true);
	$temp= $json["results"]["bindings"];
	foreach($temp as $t)
		array_push($title,$t["titleValue"]["value"]);
	
	
	$json_result = json_encode($title);
	echo $json_result;
	
	?>
		
		
