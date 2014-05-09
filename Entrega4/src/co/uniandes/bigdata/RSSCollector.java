package co.uniandes.bigdata;

import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import co.uniandes.bigdata.mongo.Feed;
import co.uniandes.bigdata.mongo.MongoAccess;
import co.uniandes.bigdata.mongo.NewsDocument;

import com.sun.syndication.feed.synd.SyndEntryImpl;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;

/**
 * @author sebastian
 * 
 */
public class RSSCollector {

    // 1 hour
    public static final int SLEEP_TIME = 3600 * 1000;
    private MongoAccess     mongoAccess;
    private Feed[]          feeds;
    /**
     * Application logger
     */
    private static Logger logger = LogManager.getLogger("RSSCollector");

    public RSSCollector() {
    }

    public void start() {
        try {
            mongoAccess = MongoAccess.getInstance();
            logger.info("==============================================================");
            logger.info("Beggining feed scraping at " + Calendar.getInstance().getTime());
            this.feeds = mongoAccess.getFeeds();
            logger.info("Feeds loaded and ready");
            int articleCounter = 0;
            for (int i = 0; i < feeds.length; i++) {
                logger.info("Processing:\t" + feeds[i]);
                URL feedUrl = new URL(feeds[i].getRssUrl());
                String feedName = feeds[i].getFeedName();
                Date lastUpdated = feeds[i].getRssLastUpdated();
                SyndFeedInput input = new SyndFeedInput();
                SyndFeed feed = input.build(new XmlReader(feedUrl));
                @SuppressWarnings("unchecked")
                List<SyndEntryImpl> entries = feed.getEntries();
                for (Iterator<SyndEntryImpl> iterator = entries.iterator(); iterator.hasNext();) {
                    SyndEntryImpl newsItem = iterator.next();
                    if (lastUpdated == null || lastUpdated.compareTo(newsItem.getPublishedDate()) < 0) {
                        articleCounter++;
                        NewsDocument newsDocument = new NewsDocument(feedName, newsItem.getTitle(), newsItem.getLink(), newsItem.getDescription().getValue(), newsItem.getPublishedDate());
                        try {
                            mongoAccess.addNewsDocument(newsDocument);
                        } catch (Exception e) {
                            logger.info(newsItem.getTitle());
                        }
                    }
                }
                feedUrl = null;
                mongoAccess.setLastUpdated(feeds[i]);
            }
            mongoAccess.closeAccess();
            System.gc();
            logger.info(articleCounter + " articles added across " + feeds.length + " feeds");
            logger.info("Feeds scraped succesfully at " + Calendar.getInstance().getTime());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        RSSCollector collector = new RSSCollector();
        while (true) {
            collector.start();
            try {
                logger.info("Sleeping for " + SLEEP_TIME / (1000 * 60) + " minutes");
                Thread.sleep(SLEEP_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
