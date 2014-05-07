package co.uniandes.bigdata;

import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

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

    public RSSCollector() {
        mongoAccess = MongoAccess.getInstance();
    }

    public void start() {
        try {
            System.out.println("==============================================================");
            System.out.println("Beggining feed scraping at " + Calendar.getInstance().getTime());
            this.feeds = mongoAccess.getFeeds();
            System.out.println("Feeds loaded and ready");
            int articleCounter = 0;
            for (int i = 0; i < feeds.length; i++) {
                System.out.println("Processing:\t" + feeds[i]);
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
                        mongoAccess.addNewsDocument(newsDocument);
                    }
                }
                mongoAccess.setLastUpdated(feeds[i]);
            }
            System.out.println(articleCounter + " articles added across " + feeds.length + " feeds");
            System.out.println("Feeds scraped succesfully at " + Calendar.getInstance().getTime());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        RSSCollector collector = new RSSCollector();
        while (true) {
            collector.start();
            try {
                System.out.println("Sleeping for " + SLEEP_TIME / (1000 * 60) + " minutes");
                Thread.sleep(SLEEP_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
