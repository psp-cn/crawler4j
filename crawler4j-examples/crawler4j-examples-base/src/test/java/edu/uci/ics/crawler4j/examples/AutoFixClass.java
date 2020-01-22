package edu.uci.ics.crawler4j.examples;

import org.slf4j.Logger;

public class AutoFixClass {
    Logger logger;

    public AutoFixClass(Logger logger) {
        this.logger = logger;
    }

    public void autoFixMethod0() throws Exception {
        logger.info("Needed parameters: ");
        logger.info("\t rootFolder (it will contain intermediate crawl data)");
        logger.info("\t numberOfCralwers (number of concurrent threads)");
    }
}
