package com.crawler.mycrawler;

import com.crawler.Constants;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by Dorinela on 11/14/2015.
 */
public class Controller {

    public static void main(String[] args) {

        try {

            int numberOfCrawlers = 10; //number of crawlers
            int startPage = 3; //start page
            int endPage = 3; //end page

            CrawlConfig config = new CrawlConfig();
            config.setCrawlStorageFolder(Constants.CRAWLER_STORAGE_FOLDER);

             /*
             * Instantiate the controller
             */
            PageFetcher pageFetcher = new PageFetcher(config);
            RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
            RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
            CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);

            for (int i = startPage; i <= endPage; i++) {
                String url = Constants.URL + i;
                controller.addSeed(url);
            }
            //controller.addSeed(Constants.URL_TEST);
            controller.start(MyCrawler.class, numberOfCrawlers);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
