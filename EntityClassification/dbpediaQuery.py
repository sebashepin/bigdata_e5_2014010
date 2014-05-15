from SPARQLWrapper import SPARQLWrapper, JSON
from pymongo import Connection

def entityType(entity):
	# Conexion a Mongo
	conn = Connection() 		
	db = conn.grupo10_taller4
	colTypeQueryCache = db.typeQueryCache
	colDescribeQueryCache = db.describeQueryCache
	
	sparql = SPARQLWrapper("http://dbpedia.org/sparql")
	sparql.setQuery("""
	PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>
	PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
	SELECT DISTINCT ?s ?o ?class WHERE { 
		?someobj  ?p ?s . 
			?s  rdfs:label '%s' @en .
		?s  rdfs:label ?o . 
			?s  rdfs:comment ?comment .
			?s rdf:type ?class .
		FILTER (!regex(str(?s), '^http://dbpedia.org/resource/Category:')). 
		FILTER (!regex(str(?s), '^http://dbpedia.org/resource/List')).
		FILTER (!regex(str(?s), '^http://sw.opencyc.org/')). 
		FILTER (lang(?o) = 'en').  
		FILTER (!isLiteral(?someobj)).
		}
	Limit 20
	""" % entity)
	sparql.setReturnFormat(JSON)
	results = sparql.query().convert()
	for result in results["results"]["bindings"]:
		currentValue = result["class"]["value"]
		if "Person" in currentValue:
			mainType = "Person"
			break
		elif "Place" in currentValue:
			mainType = "Place"
			break
		elif "Organization" in currentValue:
			mainType = "Organization"
			break
			
	if mainType == None:
		mainType = "Thing"

	queryRes = [{"term":entity,"mainType":mainType,"results":results}]
	colTypeQueryCache.insert(queryRes)
	conn.disconnect()
    return mainType

def entityProperties(entity):
	# Conexion a Mongo
	conn = Connection() 		
	db = conn.grupo10_taller4
	colTypeQueryCache = db.typeQueryCache
	colDescribeQueryCache = db.describeQueryCache
	sparql = SPARQLWrapper("http://dbpedia.org/sparql")
	sparql.setQuery("""
	PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>

	DESCRIBE ?object WHERE {
	?object rdfs:label '%s'@en .
	}
	""" % entity
	)
	sparql.setReturnFormat(JSON)
	results = sparql.query().convert()
	queryRes = [{"term":entity,"results":results}]
	colDescribeQueryCache.insert(queryRes)
	conn.disconnect()
	return results

#entityType("Google")
