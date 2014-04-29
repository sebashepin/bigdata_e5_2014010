/**
 * 
 */
package co.uniandes.bigdata.mongo;

import java.util.Date;

import com.mongodb.BasicDBObject;

/**
 * @author sebastian
 *
 */
public class NewsDocument extends BasicDBObject {

    /**
     * 
     */
    private static final long serialVersionUID = -3786749324074562211L;

    /**
     * The feed's name
     */
    private String feedName;
    
    /**
     * The item title.
     */
    private String title;

    /**
     * The item link.
     */
    private String itemLink;

    /**
     * The feed description.
     */
    private String description;

    /**
     * The feed publication date.
     */
    private Date pubDate;
    
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
    public NewsDocument(String feedName, String title, String feedLink,
            String description, Date pubDate) {
        super("id",(feedName+title).hashCode());
        this.feedName = feedName;
        this.title = title;
        this.itemLink = feedLink;
        this.description = description;
        this.pubDate = pubDate;
        append("feedName",feedName);
        append("title", title);
        append("feedLink", feedLink);
        append("description", description);
        append("pubDate", pubDate);
    }

    public String getFeedName() {
        return feedName;
    }

    public void setFeedName(String feedName) {
        this.feedName = feedName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFeedLink() {
        return itemLink;
    }

    public void setFeedLink(String feedLink) {
        this.itemLink = feedLink;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getPubDate() {
        return pubDate;
    }

    public void setPubDate(Date pubDate) {
        this.pubDate = pubDate;
    }

}
