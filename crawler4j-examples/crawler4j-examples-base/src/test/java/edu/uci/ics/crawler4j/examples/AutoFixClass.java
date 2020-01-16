package edu.uci.ics.crawler4j.examples;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

public class AutoFixClass {
    CrawlConfig config;
    CrawlController controller;

    public AutoFixClass(CrawlConfig config) {
        this.config = config;
    }

    public CrawlController getController() {
        return controller;
    }

    public void setController(CrawlController controller) {
        this.controller = controller;
    }

    public void autoFixMethod0() throws Exception {
        PageFetcher pageFetcher = new PageFetcher(config);
        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
        RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
        CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);
        /*
         * For each crawl, you need to add some seed urls. These are the first
         * URLs that are fetched and then the crawler starts following links
         * which are found in these pages
         */
        controller.addSeed("https://www.ics.uci.edu/~welling/");
        controller.addSeed("https://www.ics.uci.edu/~lopes/");
        controller.addSeed("https://www.ics.uci.edu/");
    }
}
