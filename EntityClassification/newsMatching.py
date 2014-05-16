from pymongo import Connection
threshold = 0.7

def entityType(entity):
	# Conexion a Mongo
	conn = Connection() 		
	db = conn.grupo10_taller4
	colNoticias = db.news_es
	colMatches = db.newsMatches
	
	noticiaResults = colNoticias.find()
	noticias = []
	for noticia in noticiaResults:
		noticias.append(noticia)
	
	i = 0
	while i < len(noticias):
		noticia1 = noticias[i]
		#if(noticia1["matched"] == 1):
			#continue
		entidadesNoticia1 = set(noticia1["entitys"].split(","))
		j = i+1
		matches=[]
		matches.append(noticia1["_id"])
		while j < len(noticias)-1:
			#if(noticia2["matched"] == 1):
				#continue
			noticia2 = noticias[j]
			entidadesNoticia2 = set(noticia2["entitys"].split(","))
			match = entidadesNoticia1 & entidadesNoticia2
			closeness = len(match) * 1.0 / (max(len(entidadesNoticia1),len(entidadesNoticia2)) *1.0)
			if( closeness > threshold ):
				matches.append(noticia2["_id"])
			j = j+1
		if(len(matches)>1):
			print "NewsMatch for news %s" % matches
			colMatches.insert({"matches":tuple(matches)},true)
		i = i+1
	
entityType("Colombia")
