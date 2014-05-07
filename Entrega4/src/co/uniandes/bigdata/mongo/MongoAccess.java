/**
 * 
 */
package co.uniandes.bigdata.mongo;

import java.net.UnknownHostException;
import java.util.Calendar;
import java.util.Date;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.WriteConcern;

/**
 * @author sebastian
 *
 */
public class MongoAccess {

    private static MongoAccess instance;
    private Mongo mongo;
    private DB db;
    
    private DBCollection colaTuits, colaNoticias;
    private DBCollection feeds;
    
    private MongoAccess()
    {
        try {
            //Connection setup
            mongo = new Mongo();
            db =  mongo.getDB("grupo10_taller4");
            colaNoticias = db.getCollection("colaNoticias");
            colaTuits = db.getCollection("colaTuits");
            feeds = db.getCollection("feeds");
            mongo.setWriteConcern(WriteConcern.SAFE); //Exception thrown in any error
            
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        
        instance = this;
    }
    
    public Feed[] getFeeds()
    {
    	int amount = (int)feeds.count();
    	Feed[] result = new Feed[amount];
    	DBCursor cursor = feeds.find();
    	int i = 0;
    	try {
    	   while(cursor.hasNext()) {
    	       Feed newFeed = new Feed(cursor.next());
    	       result[i] = newFeed;
    	       i++;
    	   }
    	} finally {
    	   cursor.close();
    	}    	
    	return result;
    }
    
    public DBCursor executeQuery(DBObject query)
    {
        return colaTuits.find(query);
    }
    
    public DBCursor allTweetsInQueue()
    {
        return colaTuits.find();
    }
    
    public void setLastUpdated(Feed feed)
    {
    	Date now = Calendar.getInstance().getTime();
    	
    	BasicDBObject newDocument = new BasicDBObject();
    	newDocument.append("$set", new BasicDBObject().append("rssLastUpdated", now));
    	BasicDBObject searchQuery = new BasicDBObject().append("feedName", feed.getFeedName());
    	feeds.update(searchQuery, newDocument);
    	feed.setRssLastUpdated(now);
    }
    
    public void addNewsDocument(NewsDocument newsDocument)
    {
        colaNoticias.insert(newsDocument);
    }
    
    public final static MongoAccess getInstance()
    {
        if(instance==null)
            instance = new MongoAccess();
        
        return instance;
    }
}
