package edu.uci.ics.crawler4j.examples;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;

public class AutoFixClass {
    CrawlConfig config;

    public AutoFixClass(CrawlConfig config) {
        this.config = config;
    }

    public CrawlConfig getConfig() {
        return config;
    }

    public void setConfig(CrawlConfig config) {
        this.config = config;
    }

    public void autoFixMethod0() throws Exception {
        config.setPolitenessDelay(1000);
        // You can set the maximum crawl depth here. The default value is -1 for unlimited depth.
        config.setMaxDepthOfCrawling(2);
        // You can set the maximum number of pages to crawl. The default value is -1 for unlimited number of pages.
        config.setMaxPagesToFetch(1000);
    }
}
