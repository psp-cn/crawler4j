package edu.uci.ics.crawler4j.examples;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

public class AutoFixClass {
    CrawlController controller;
    CrawlConfig config;

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
    }
}
