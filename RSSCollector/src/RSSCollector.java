import java.net.URL;
import java.util.Iterator;
import java.util.List;

import com.sun.syndication.feed.synd.SyndEntryImpl;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;

/**
 * @author sebastian
 *
 */
/**
 * @author sebastian
 *
 */
/**
 * @author sebastian
 *
 */
public class RSSCollector {

        /**
         * @param args
         * @throws Exception
         */
        /**
         * @param args
         * @throws Exception
         */
        /**
         * @param args
         * @throws Exception
         */
        public static void main(String[] args) throws Exception {
            
            //URL url = new URL("http://feeds.elespectador.com/c/33813/f/607844/index.rss");
//            URL url = new URL("http://www.ntn24.com/rss/ntn24");
//            
//            Feed feed = FeedParser.parse(url);
//            
//            System.out.println("** HEADER **");
//            FeedHeader header = feed.getHeader();
//            System.out.println("Title: " + header.getTitle());
//            System.out.println("Link: " + header.getLink());
//            System.out.println("Description: " + header.getDescription());
//            System.out.println("Language: " + header.getLanguage());
//            System.out.println("PubDate: " + header.getPubDate());
//            
//            System.out.println("** ITEMS **");
//            int items = feed.getItemCount();
//            for (int i = 0; i < items; i++) {
//                FeedItem item = feed.getItem(i);
//                System.out.println("Title: " + item.getTitle());
//                System.out.println("Link: " + item.getLink());
//                System.out.println("Plain text description: " + item.getDescriptionAsText());
//                System.out.println("HTML description: " + item.getDescriptionAsHTML());
//                System.out.println("PubDate: " + item.getPubDate());
//                RawNode raw = item.getNode(3);
//                if(raw instanceof RawElement)
//                {
//                    RawElement rawE = (RawElement) raw;
//                    System.out.println(rawE.getValue());
//                }
//            }
         
                boolean ok = false;
                    try {
                        URL feedUrl = new URL("http://feeds.elespectador.com/c/33813/f/607844/index.rss");

                        SyndFeedInput input = new SyndFeedInput();
                        SyndFeed feed = input.build(new XmlReader(feedUrl));
                        
                        List<SyndEntryImpl> entries = feed.getEntries();
                        for (Iterator iterator = entries.iterator(); iterator.hasNext();) {
                            SyndEntryImpl syndEntryImpl = (SyndEntryImpl) iterator.next();
                            System.out.println(syndEntryImpl.toString());
                            System.out.println(syndEntryImpl.getDescription().toString());
                        }


                        ok = true;
                    }
                    catch (Exception ex) {
                        ex.printStackTrace();
                        System.out.println("ERROR: "+ex.getMessage());
                    }
                
            }
        }


