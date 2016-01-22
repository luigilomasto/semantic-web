<?php
  
class http_API{
	
	private $endpoint;
	private $endpointQuery;
	
	function __construct(){
		$this->endpoint =  "http://localhost:3030/Final/query?";
		$this->endpointQuery = "http://localhost:3030/Final/query?";
	}

   
    
    function sparqlQuery($query,$format = 'json' ){     
        //global $endpointQuery;
        //$format = 'xml';
        
         if (!function_exists('curl_init')){ 
           die('CURL is not installed!');
        }
        
        // get curl handle
        $ch= curl_init();
        $url = $this->endpointQuery . "query=".urlencode($query)."&format=".$format;
        curl_setopt($ch, CURLOPT_URL, $url);        
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);        
     
        $response = curl_exec($ch);
        //print_r($response);
        curl_close($ch);
        
        return $response;
    }
	
	function getUrl($query){        
        //global $endpoint;
        $format = 'xml';
     
                
		/*$query ="
         prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> 
         prefix :<http://www.competenze.com/ontologia#> 
         INSERT DATA{<http://www.competenze.com/ontologia#Prova7>
		 <http://www.w3.org/1999/02/22-rdf-syntax-ns#type>
		 <http://www.w3.org/2002/07/owl#Class> }
		 ";*/
		  
	 
        $searchUrl = $this->endpoint
           .'update='.urlencode($query)
           .'&format='.$format;
     
        return $searchUrl;
    }
    
	
	function sparqlUpdate($query)
	{
	 if (!function_exists('curl_init')) 
           die('CURL is not installed!');
        // get curl handle
        $ch= curl_init();
        $url = getUrl($query);
        // set request url
        curl_setopt($ch, CURLOPT_URL, $url);     
        // return response, don't print/echo
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
        curl_setopt($ch, CURLOPT_POST, 1);      
                
        $response = curl_exec($ch);
        curl_close($ch); 
		//print_r($response);
	}	
	
	/*
	//test
	$return = sparqlQuery("PREFIX comp: <http://www.foodontology.it/ontology#> SELECT ?ob  WHERE { comp:tomatoes ?p ?ob } ");
	echo $return;
	*/
	
}
	
?>
