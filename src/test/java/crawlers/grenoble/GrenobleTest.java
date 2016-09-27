package test.java.crawlers.grenoble;

import java.io.IOException;
import main.java.crawlers.grenoble.Constants;
import main.java.crawlers.grenoble.GrenobleCrawler;

import org.junit.Test;

/**
 * @author Gabriel Gutu
 *
 */
public class GrenobleTest {

	@Test
	public void grenobleParser() {
		
		GrenobleCrawler crawler = new GrenobleCrawler();
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
