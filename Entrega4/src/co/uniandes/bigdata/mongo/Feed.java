/**
 * 
 */
package co.uniandes.bigdata.mongo;

import java.util.Date;

import com.mongodb.DBObject;

/**
 * @author sebastian
 *
 */
/**
 * @author sebastian
 *
 */
public class Feed{

    /**
     * The feed's name
     */
    private String feedName;
    
    /**
     * The item title.
     */
    private String rssUrl;

    /**
     * The item's twitter username.
     */
    private String twitterUrl;
    
    /**
     * The feed's twitter ID
     */
    private String twitterID;

    /**
     * The feed description.
     */
    private Date rssLastUpdated;
    

    public Feed(String feedName, String rssUrl, String twitterUrl, String twitterID, Date rssLastUpdated) {
        this.feedName = feedName;
        this.rssUrl = rssUrl;
        this.twitterUrl = twitterUrl;
        this.twitterID = twitterID;
        this.rssLastUpdated = rssLastUpdated;
    }

    /**
     * @param feedObject
     */
    public Feed(DBObject feedObject){
    	this.feedName = (String)feedObject.get("feedName");
        this.rssUrl = (String) feedObject.get("rssUrl");
        this.twitterUrl = (String) feedObject.get("twitterUrl");
        this.twitterID = (String) feedObject.get("twitterID");
        this.rssLastUpdated = (Date) feedObject.get("rssLastUpdated");
    }
    
	public String getFeedName() {
		return feedName;
	}

	public String getRssUrl() {
		return rssUrl;
	}

	public String getTwitterUrl() {
		return twitterUrl;
	}
	
	public String getTwitterID() {
	    return twitterID;
	}

	public Date getRssLastUpdated() {
		return rssLastUpdated;
	}
	
	public void setRssLastUpdated(Date newDate) {
		this.rssLastUpdated = newDate;
	}

	public String toString()
	{
		return feedName + ":" + rssUrl;
	}
}