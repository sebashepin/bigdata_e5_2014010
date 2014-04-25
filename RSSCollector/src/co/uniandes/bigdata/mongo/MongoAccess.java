/**
 * 
 */
package co.uniandes.bigdata.mongo;

import java.net.UnknownHostException;

import com.mongodb.DB;
import com.mongodb.DBCollection;
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
    
    @SuppressWarnings("unused")
    private DBCollection queueTweets, queueNews;
    
    private MongoAccess()
    {
        try {
            //Connection setup
            mongo = new Mongo();
            db =  mongo.getDB("Grupo05_newsfeed");
            queueNews = db.getCollection("queueNews");
            mongo.setWriteConcern(WriteConcern.SAFE); //Exception thrown in any error
            
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        
        instance = this;
    }
    
    public void addNewsDocument(NewsDocument newsDocument)
    {
        queueNews.insert(newsDocument);
    }
    
    public long getTweetCount()
    {
        return queueNews.count();
    }
    
    public final static MongoAccess getInstance()
    {
        if(instance==null)
            instance = new MongoAccess();
        
        return instance;
    }
}
