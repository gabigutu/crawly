package test.java.crawlers.grenoble;

import java.io.IOException;
import java.util.logging.Logger;
import main.java.crawlers.grenoble.Constants;
import main.java.crawlers.grenoble.GrenobleCrawler;

import org.junit.Test;

/**
 * @author Gabriel Gutu
 *
 */
public class GrenobleTest {
    
    private static final Logger LOGGER = Logger.getLogger(GrenobleCrawler.class.getName());
    
    @Test
    public void grenobleParser() {
        
        LOGGER.info("Starting Grenoble test");
        GrenobleCrawler crawler = new GrenobleCrawler();
        crawler.setSaveTxtFiles(true);
        try {
            crawler.crawl(Constants.GRENOBLE_CRAWL_START);
            crawler.parseIndexedLinks();
        } catch (IOException e) {
            System.err.println("Eroare parsare URL: ");
            e.printStackTrace();
        }
        System.out.println(crawler);
        
    }
    
}
