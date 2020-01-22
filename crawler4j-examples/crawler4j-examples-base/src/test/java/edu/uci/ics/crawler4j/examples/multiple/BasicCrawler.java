package edu.uci.ics.crawler4j.examples.multiple;

import edu.uci.ics.crawler4j.examples.AutoFixClass;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableList;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

public class BasicCrawler extends WebCrawler {
    private static final Logger logger = LoggerFactory.getLogger(BasicCrawler.class);

    private static final Pattern FILTERS = Pattern.compile(
        ".*(\\.(css|js|bmp|gif|jpe?g" + "|png|tiff?|mid|mp2|mp3|mp4" +
        "|wav|avi|mov|mpeg|ram|m4v|pdf" + "|rm|smil|wmv|swf|wma|zip|rar|gz))$");

    private final List<String> myCrawlDomains;

    public BasicCrawler(List<String> myCrawlDomains) {
        this.myCrawlDomains = ImmutableList.copyOf(myCrawlDomains);
    }

    @Override
    public boolean shouldVisit(Page referringPage, WebURL url) {
        String href = url.getURL().toLowerCase();
        if (FILTERS.matcher(href).matches()) {
            return false;
        }

        for (String crawlDomain : myCrawlDomains) {
            if (href.startsWith(crawlDomain)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void visit(Page page) {
        AutoFixClass autoFix0 = new AutoFixClass(page, logger);
        autoFix0.autoFixMethod0();
    }
}