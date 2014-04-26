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
     * The item link.
     */
    private String twitterUrl;

    /**
     * The feed description.
     */
    private Date rssLastUpdated;
    
    /**
     * Builds a MongoDB object with basic RSS fields
     * 
     * @param feedName
     * @param feedUrl
     * @param title
     * @param feedLink
     * @param description
     * @param pubDate
     * @param imageUrl
     */
    public Feed(String feedName, String rssUrl, String twitterUrl, Date rssLastUpdated) {
        this.feedName = feedName;
        this.rssUrl = rssUrl;
        this.twitterUrl = twitterUrl;
        this.rssLastUpdated = rssLastUpdated;
    }

    public Feed(DBObject feedObject){
    	this.feedName = (String)feedObject.get("feedName");
        this.rssUrl = (String) feedObject.get("rssUrl");
        this.twitterUrl = (String) feedObject.get("twitterUrl");
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