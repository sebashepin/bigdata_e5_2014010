/**
 * 
 */
package co.uniandes.bigdata;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import co.uniandes.bigdata.mongo.Feed;
import co.uniandes.bigdata.mongo.MongoAccess;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

/**
 * @author sebastian
 */
public class RT_SandD {

    /**
     * Mongo access object
     */
    private MongoAccess access;
    
    /**
     * Application logger
     */
    private static Logger logger = LogManager.getLogger("RT_S&D");
    
    /**
     * Feed array
     */
    private Feed[] feeds;
    
    public RT_SandD() {
        logger.info("Application started, attempting mongo connection");
        access = MongoAccess.getInstance();
        logger.info("Mongo connection succesful, loading feeds");
        feeds = access.getFeeds();
        logger.info("{} feeds loaded\n!\t{}",feeds.length,Arrays.toString(feeds));
        logger.info("Initiating retweet S&D...");   
    }
    
    public void start() throws IOException
    {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String line = "";
        DBObject query = new BasicDBObject();
        while(true)
        {
            line = br.readLine();
            if(line.equals("all"))
            {
                int i = 0;
                int j = 0;
                DBCursor cursor = access.allTweetsInQueue();
                while(cursor.hasNext())
                {
                    i++;
                    DBObject obj = cursor.next();
                    if(obj.containsField("user")){
                        j++;
                        System.out.println(((DBObject)obj.get("user")).keySet());
                    }
                }
                System.out.println(cursor+"\n\t"+i+"\t"+j);
                query = new BasicDBObject();
                continue;
            }
            if(line.equals("quit"))
            {
                System.exit(0);
            }
            if(line.equals("exec"))
            {
                int i = 0;
                int j = 0;
                DBCursor cursor = access.executeQuery(query);
                while(cursor.hasNext())
                {
                    i++;
                    DBObject obj = cursor.next();
                    if(obj.containsField("user")){
                        j++;
                        System.out.println(obj);
                    }
                }
                System.out.println(cursor+"\n\t"+i+"\t"+j);
                query = new BasicDBObject();
                continue;
            }
            String[] data = line.split(":",2);
            try{
                long candidate = Long.parseLong(data[1].trim());
                query.put(data[0].trim(), candidate);
            }
            catch(NumberFormatException e)
            {
                if(data[1].startsWith("\"") && data[1].endsWith("\""))
                    data[1] = data[1].substring(1, data[1].length()-1);
                query.put(data[0].trim(), data[1].equals("null")?null:data[1].trim());                
            }
            System.out.println(query);
        }
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
