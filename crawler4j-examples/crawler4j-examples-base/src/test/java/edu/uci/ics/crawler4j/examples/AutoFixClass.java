package edu.uci.ics.crawler4j.examples;

import java.util.Set;
import org.slf4j.Logger;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

public class AutoFixClass {
    Page page;
    Logger logger;

    public AutoFixClass(Page page, Logger logger) {
        this.page = page;
        this.logger = logger;
    }

    public void autoFixMethod0() {
        int docid = page.getWebURL().getDocid();
        String url = page.getWebURL().getURL();
        int parentDocid = page.getWebURL().getParentDocid();
        logger.debug("Docid: {}", docid);
        logger.info("URL: {}", url);
        logger.debug("Docid of parent page: {}", parentDocid);
        if (page.getParseData() instanceof HtmlParseData) {
            HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
            String text = htmlParseData.getText();
            String html = htmlParseData.getHtml();
            Set<WebURL> links = htmlParseData.getOutgoingUrls();
            logger.debug("Text length: {}", text.length());
            logger.debug("Html length: {}", html.length());
            logger.debug("Number of outgoing links: {}", links.size());
        }
        logger.debug("=============");
    }
}
