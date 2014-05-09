/**
 * 
 */
package co.uniandes.bigdata;

import java.io.IOException;
import java.util.Arrays;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import co.uniandes.bigdata.mongo.Feed;
import co.uniandes.bigdata.mongo.MongoAccess;

import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoException;

/**
 * @author sebastian
 */
public class RT_SandD {

    /**
     * Mongo access object
     */
    private MongoAccess   access;

    /**
     * Application logger
     */
    private static Logger logger = LogManager.getLogger("RT_S&D");

    /**
     * Feed array
     */
    private Feed[]        feeds;

    public RT_SandD() {
    }

    public void start() throws IOException {
        logger.info("==============================================================");
        logger.info("Application started, attempting mongo connection");
        access = MongoAccess.getInstance();
        logger.info("Mongo connection succesful, loading feeds");
        feeds = access.getFeeds();
        logger.info("{} feeds loaded\n!\t{}", feeds.length, Arrays.toString(feeds));
        logger.info("Initiating retweet S&D...");
        DBCursor cursor = access.allTweetsInQueue();
        logger.info("Found {} tweets in queue",cursor.count());
        
        int retweetAmount = 0;
        int newTweets = 0;
        int orphanedTweets = 0;
        while (cursor.hasNext()) {
            
            DBObject obj = cursor.next();
            String userId = (String)((DBObject)obj.get("user")).get("id_str");
            if(userIsFeed(userId))
            {
                continue;
            }
            else if(obj.containsField("retweeted_status"))
            {
                retweetAmount++;
                DBObject retweet = (DBObject)obj.get("retweeted_status");
                String retweetUserId = (String)((DBObject)retweet.get("user")).get("id_str");
                if(userIsFeed(retweetUserId))
                {
                    try {
                        access.insertTweetOnQueue(retweet);
                        newTweets++;
                    } catch (MongoException.DuplicateKey e) {
                        //Expected behavior
                    }
                }
                access.removeTweetFromQueue(obj);
            }
            else
            {
                orphanedTweets++;
                access.removeTweetFromQueue(obj);
            }
        }
        logger.info("Found and removed {} retweets and {} orphaned tweets. Inserted {} new tweets",retweetAmount,orphanedTweets,newTweets);
        access.closeAccess();
    }

    private boolean userIsFeed(String twitterId)
    {
        for (int i = 0; i < feeds.length; i++) {
            if(feeds[i].getTwitterID().equals(twitterId))
                return true;
        }
        return false;
    }
    
    /**
     * @param args
     */
    public static void main(String[] args) {
        RT_SandD rtsandd = new RT_SandD();

        try {
            rtsandd.start();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
