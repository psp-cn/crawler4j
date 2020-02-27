/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package edu.uci.ics.crawler4j.crawler;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.http.HttpStatus;
import org.apache.http.impl.EnglishReasonPhraseCatalog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.uci.ics.crawler4j.crawler.exceptions.ContentFetchException;
import edu.uci.ics.crawler4j.crawler.exceptions.PageBiggerThanMaxSizeException;
import edu.uci.ics.crawler4j.crawler.exceptions.ParseException;
import edu.uci.ics.crawler4j.fetcher.PageFetchResult;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.frontier.DocIDServer;
import edu.uci.ics.crawler4j.frontier.Frontier;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.parser.NotAllowedContentException;
import edu.uci.ics.crawler4j.parser.ParseData;
import edu.uci.ics.crawler4j.parser.Parser;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import edu.uci.ics.crawler4j.url.WebURL;

/**
 * WebCrawler class in the Runnable class that is executed by each crawler thread.
 *
 * @author Yasser Ganjisaffar
 */
public class WebCrawler implements Runnable {

                                            private class AutoFixClassMDMDMD {

	                                            String movedToUrl;
	                                            PageFetcher pageFetcher;
	                                            List<WebURL> toSchedule;
	                                            WebURL curURL;
	                                            int statusCode;
	                                            HtmlParseData page;
	                                            int newDocId;
	                                            PageFetchResult fetchResult;
	                                            int webURL;

	                                            public AutoFixClass(PageFetcher pageFetcher, WebURL curURL, HtmlParseData page) {
		                                            this.pageFetcher = pageFetcher;
		                                            this.curURL = curURL;
		                                            this.page = page;
	                                            }

	                                            public String getMovedToUrl() {
		                                            return movedToUrl;
	                                            }

	                                            public void setMovedToUrl(String movedToUrl) {
		                                            this.movedToUrl = movedToUrl;
	                                            }

	                                            public Page getPage() {
		                                            return page;
	                                            }

	                                            public void setPage(Page page) {
		                                            this.page = page;
	                                            }

	                                            public List<WebURL> getToSchedule() {
		                                            return toSchedule;
	                                            }

	                                            public void setToSchedule(List<WebURL> toSchedule) {
		                                            this.toSchedule = toSchedule;
	                                            }

	                                            public int getStatusCode() {
		                                            return statusCode;
	                                            }

	                                            public void setStatusCode(int statusCode) {
		                                            this.statusCode = statusCode;
	                                            }

	                                            public Page getCurURL() {
		                                            return curURL;
	                                            }

	                                            public void setCurURL(Page curURL) {
		                                            this.curURL = curURL;
	                                            }

	                                            public int getNewDocId() {
		                                            return newDocId;
	                                            }

	                                            public void setNewDocId(int newDocId) {
		                                            this.newDocId = newDocId;
	                                            }

	                                            public PageFetchResult getFetchResult() {
		                                            return fetchResult;
	                                            }

	                                            public void setFetchResult(PageFetchResult fetchResult) {
		                                            this.fetchResult = fetchResult;
	                                            }

	                                            public int getWebURL() {
		                                            return webURL;
	                                            }

	                                            public void setWebURL(int webURL) {
		                                            this.webURL = webURL;
	                                            }

	                                            public void autoFixMethod0(PageFetchResult fetchResult)  throws IOException, ParseException, InterruptedException {
            fetchResult = pageFetcher.fetchPage(curURL);
            int statusCode = fetchResult.getStatusCode();
            handlePageStatusCode(curURL, statusCode,
                                 EnglishReasonPhraseCatalog.INSTANCE.getReason(statusCode,
                                                                               Locale.ENGLISH));
            // Finds the status reason for all known statuses

            page.setFetchResponseHeaders(fetchResult.getResponseHeaders());

	setFetchResult(fetchResult);

	                                            }

	                                            public void autoFixMethod1()  throws IOException, ParseException, InterruptedException {
                    page.setRedirect(true);

                    String movedToUrl = fetchResult.getMovedToUrl();

	                                            }

	                                            public void autoFixMethod2()  throws IOException, ParseException, InterruptedException {
                        onRedirectedToInvalidUrl(page);

	                                            }

	                                            public void autoFixMethod3()  throws IOException, ParseException, InterruptedException {
                    page.setRedirectedToUrl(movedToUrl);
                    onRedirectedStatusCode(page);

	                                            }

	                                            public void autoFixMethod4()  throws IOException, ParseException, InterruptedException {
                        int newDocId = docIdServer.getDocId(movedToUrl);

	                                            }

	                                            public void autoFixMethod5()  throws IOException, ParseException, InterruptedException {
                            logger.debug("Redirect page: {} is already seen", curURL);

	                                            }

	                                            public void autoFixMethod6()  throws IOException, ParseException, InterruptedException {
                        WebURL webURL = new WebURL();
                        webURL.setTldList(myController.getTldList());
                        webURL.setURL(movedToUrl);
                        webURL.setParentDocid(curURL.getParentDocid());

	                                            }

	                                            public void autoFixMethod7()  throws IOException, ParseException, InterruptedException {
                        webURL.setParentUrl(curURL.getParentUrl());
                        webURL.setDepth(curURL.getDepth());
                        webURL.setDocid(-1);
                        webURL.setAnchor(curURL.getAnchor());

	                                            }

	                                            public void autoFixMethod8()  throws IOException, ParseException, InterruptedException {
                            if (!shouldFollowLinksIn(webURL) || robotstxtServer.allows(webURL)) {
                                webURL.setDocid(docIdServer.getNewDocID(movedToUrl));
                                frontier.schedule(webURL);
                            } else {
                                logger.debug(
                                    "Not visiting: {} as per the server's \"robots.txt\" policy",
                                    webURL.getURL());
                            }

	                                            }

	                                            public void autoFixMethod9()  throws IOException, ParseException, InterruptedException {
                    String description =
                        EnglishReasonPhraseCatalog.INSTANCE.getReason(fetchResult.getStatusCode(),
                                                                      Locale.ENGLISH); // Finds
                    // the status reason for all known statuses
                    String contentType = fetchResult.getEntity() == null ? "" :
                                         fetchResult.getEntity().getContentType() == null ? "" :
                                         fetchResult.getEntity().getContentType().getValue();
                    onUnexpectedStatusCode(curURL.getURL(), fetchResult.getStatusCode(),
                                           contentType, description);

	                                            }

	                                            public void autoFixMethod10()  throws IOException, ParseException, InterruptedException {
                        logger.debug("Redirect page: {} has already been seen", curURL);

	                                            }

	                                            public void autoFixMethod11()  throws IOException, ParseException, InterruptedException {
                    curURL.setURL(fetchResult.getFetchedUrl());
                    curURL.setDocid(docIdServer.getNewDocID(fetchResult.getFetchedUrl()));

	                                            }

	                                            public void autoFixMethod12()  throws IOException, ParseException, InterruptedException {
                if (!fetchResult.fetchContent(page,
                                              myController.getConfig().getMaxDownloadSize())) {
                    throw new ContentFetchException();
                }

	                                            }

	                                            public void autoFixMethod13()  throws IOException, ParseException, InterruptedException {
                if (page.isTruncated()) {
                    logger.warn(
                        "Warning: unknown page size exceeded max-download-size, truncated to: " +
                        "({}), at URL: {}",
                        myController.getConfig().getMaxDownloadSize(), curURL.getURL());
                }

                parser.parse(page, curURL.getURL());

	                                            }

	                                            public void autoFixMethod14()  throws IOException, ParseException, InterruptedException {
                                    if (robotstxtServer.allows(webURL)) {
                                        webURL.setDocid(docIdServer.getNewDocID(webURL.getURL()));
                                        toSchedule.add(webURL);
                                    } else {
                                        logger.debug(
                                            "Not visiting: {} as per the server's \"robots.txt\" " +
                                            "policy", webURL.getURL());
                                    }

	                                            }

	                                            public void autoFixMethod15()  throws IOException, ParseException, InterruptedException {
                boolean noIndex = myController.getConfig().isRespectNoIndex() &&
                    page.getContentType() != null &&
                    page.getContentType().contains("html") &&
                    ((HtmlParseData)page.getParseData())
                        .getMetaTagValue("robots").
                        contains("noindex");

                if (!noIndex) {
                    visit(page);
                }

	                                            }

                                            }

    protected static final Logger logger = LoggerFactory.getLogger(WebCrawler.class);

    /**
     * The id associated to the crawler thread running this instance
     */
    protected int myId;

    /**
     * The controller instance that has created this crawler thread. This
     * reference to the controller can be used for getting configurations of the
     * current crawl or adding new seeds during runtime.
     */
    protected CrawlController myController;

    /**
     * The thread within which this crawler instance is running.
     */
    private Thread myThread;

    /**
     * The parser that is used by this crawler instance to parse the content of the fetched pages.
     */
    private Parser parser;

    /**
     * The fetcher that is used by this crawler instance to fetch the content of pages from the web.
     */
    private PageFetcher pageFetcher;

    /**
     * The RobotstxtServer instance that is used by this crawler instance to
     * determine whether the crawler is allowed to crawl the content of each page.
     */
    private RobotstxtServer robotstxtServer;

    /**
     * The DocIDServer that is used by this crawler instance to map each URL to a unique docid.
     */
    private DocIDServer docIdServer;

    /**
     * The Frontier object that manages the crawl queue.
     */
    private Frontier frontier;

    /**
     * Is the current crawler instance waiting for new URLs? This field is
     * mainly used by the controller to detect whether all of the crawler
     * instances are waiting for new URLs and therefore there is no more work
     * and crawling can be stopped.
     */
    private boolean isWaitingForNewURLs;

    private Throwable error;

    private int batchReadSize;

    /**
     * Initializes the current instance of the crawler
     *
     * @param id
     *            the id of this crawler instance
     * @param crawlController
     *            the controller that manages this crawling session
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public void init(int id, CrawlController crawlController)
        throws InstantiationException, IllegalAccessException {
        this.myId = id;
        this.pageFetcher = crawlController.getPageFetcher();
        this.robotstxtServer = crawlController.getRobotstxtServer();
        this.docIdServer = crawlController.getDocIdServer();
        this.frontier = crawlController.getFrontier();
        this.parser = crawlController.getParser();
        this.myController = crawlController;
        this.isWaitingForNewURLs = false;
        this.batchReadSize = crawlController.getConfig().getBatchReadSize();
    }

    /**
     * Get the id of the current crawler instance
     *
     * @return the id of the current crawler instance
     */
    public int getMyId() {
        return myId;
    }

    public CrawlController getMyController() {
        return myController;
    }

    /**
     * This function is called just before starting the crawl by this crawler
     * instance. It can be used for setting up the data structures or
     * initializations needed by this crawler instance.
     */
    public void onStart() {
        // Do nothing by default
        // Sub-classed can override this to add their custom functionality
    }

    /**
     * This function is called just before the termination of the current
     * crawler instance. It can be used for persisting in-memory data or other
     * finalization tasks.
     */
    public void onBeforeExit() {
        // Do nothing by default
        // Sub-classed can override this to add their custom functionality
    }

    /**
     * This function is called once the header of a page is fetched. It can be
     * overridden by sub-classes to perform custom logic for different status
     * codes. For example, 404 pages can be logged, etc.
     *
     * @param webUrl WebUrl containing the statusCode
     * @param statusCode Html Status Code number
     * @param statusDescription Html Status COde description
     */
    protected void handlePageStatusCode(WebURL webUrl, int statusCode, String statusDescription) {
        // Do nothing by default
        // Sub-classed can override this to add their custom functionality
    }

    /**
     * This function is called before processing of the page's URL
     * It can be overridden by subclasses for tweaking of the url before processing it.
     * For example, http://abc.com/def?a=123 - http://abc.com/def
     *
     * @param curURL current URL which can be tweaked before processing
     * @return tweaked WebURL
     */
    protected WebURL handleUrlBeforeProcess(WebURL curURL) {
        return curURL;
    }

    /**
     * This function is called if the content of a url is bigger than allowed size.
     *
     * @param urlStr - The URL which it's content is bigger than allowed size
     */
    protected void onPageBiggerThanMaxSize(String urlStr, long pageSize) {
        logger.warn("Skipping a URL: {} which was bigger ( {} ) than max allowed size", urlStr,
                    pageSize);
    }

    /**
     * This function is called if the crawler encounters a page with a 3xx status code
     *
     * @param page Partial page object
     */
    protected void onRedirectedStatusCode(Page page) {
        //Subclasses can override this to add their custom functionality
    }

    /**
     * Emitted when the crawler is redirected to an invalid Location.
     * @param page
     */
    protected void onRedirectedToInvalidUrl(Page page) {
        logger.warn("Unexpected error, URL: {} is redirected to NOTHING",
            page.url.getURL());
    }

    /**
     * This function is called if the crawler encountered an unexpected http status code ( a
     * status code other than 3xx)
     *
     * @param urlStr URL in which an unexpected error was encountered while crawling
     * @param statusCode Html StatusCode
     * @param contentType Type of Content
     * @param description Error Description
     */
    protected void onUnexpectedStatusCode(String urlStr, int statusCode, String contentType,
                                          String description) {
        logger.warn("Skipping URL: {}, StatusCode: {}, {}, {}", urlStr, statusCode, contentType,
                    description);
        // Do nothing by default (except basic logging)
        // Sub-classed can override this to add their custom functionality
    }

    /**
     * This function is called if the content of a url could not be fetched.
     *
     * @param webUrl URL which content failed to be fetched
     *
     * @deprecated use {@link #onContentFetchError(Page)}
     */
    @Deprecated
    protected void onContentFetchError(WebURL webUrl) {
        logger.warn("Can't fetch content of: {}", webUrl.getURL());
        // Do nothing by default (except basic logging)
        // Sub-classed can override this to add their custom functionality
    }

    /**
     * This function is called if the content of a url could not be fetched.
     *
     * @param page Partial page object
     */
    protected void onContentFetchError(Page page) {
        logger.warn("Can't fetch content of: {}", page.getWebURL().getURL());
        // Do nothing by default (except basic logging)
        // Sub-classed can override this to add their custom functionality
    }

    /**
     * This function is called when a unhandled exception was encountered during fetching
     *
     * @param webUrl URL where a unhandled exception occured
     */
    protected void onUnhandledException(WebURL webUrl, Throwable e) {
        if (myController.getConfig().isHaltOnError() && !(e instanceof IOException)) {
            throw new RuntimeException("unhandled exception", e);
        } else {
            String urlStr = (webUrl == null ? "NULL" : webUrl.getURL());
            logger.warn("Unhandled exception while fetching {}: {}", urlStr, e.getMessage());
            logger.info("Stacktrace: ", e);
            // Do nothing by default (except basic logging)
            // Sub-classed can override this to add their custom functionality
        }
    }

    /**
     * This function is called if there has been an error in parsing the content.
     *
     * @param webUrl URL which failed on parsing
     */
    protected void onParseError(WebURL webUrl, ParseException e) throws ParseException {
        onParseError(webUrl);
    }

    /**
     * This function is called if there has been an error in parsing the content.
     *
     * @param webUrl URL which failed on parsing
     */
    @Deprecated
    protected void onParseError(WebURL webUrl) {
        logger.warn("Parsing error of: {}", webUrl.getURL());
        // Do nothing by default (Except logging)
        // Sub-classed can override this to add their custom functionality
    }

    /**
     * The CrawlController instance that has created this crawler instance will
     * call this function just before terminating this crawler thread. Classes
     * that extend WebCrawler can override this function to pass their local
     * data to their controller. The controller then puts these local data in a
     * List that can then be used for processing the local data of crawlers (if needed).
     *
     * @return currently NULL
     */
    public Object getMyLocalData() {
        return null;
    }

    @Override
    public void run() {
        try {
            onStart();
            setError(null);
            boolean halt = false;
            while (!halt) {
                List<WebURL> assignedURLs = new ArrayList<>(batchReadSize);
                isWaitingForNewURLs = true;
                frontier.getNextURLs(batchReadSize, assignedURLs);
                isWaitingForNewURLs = false;
                if (assignedURLs.isEmpty()) {
                    if (frontier.isFinished()) {
                        return;
                    }
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        logger.error("Error occurred", e);
                    }
                } else {
                    for (WebURL curURL : assignedURLs) {
                        if (myController.isShuttingDown()) {
                            logger.info("Exiting because of controller shutdown.");
                            return;
                        }
                        if (curURL != null) {
                            curURL = handleUrlBeforeProcess(curURL);
                            processPage(curURL);
                            frontier.setProcessed(curURL);
                        }
                    }
                }
                if (myController.getConfig().isHaltOnError() && myController.getError() != null) {
                    halt = true;
                    logger.info("halting because an error has occurred on another thread");
                }
            }
        } catch (Throwable t) {
            setError(t);
        }
    }

    /**
     * Classes that extends WebCrawler should overwrite this function to tell the
     * crawler whether the given url should be crawled or not. The following
     * default implementation indicates that all urls should be included in the crawl
     * except those with a nofollow flag.
     *
     * @param url
     *            the url which we are interested to know whether it should be
     *            included in the crawl or not.
     * @param referringPage
     *           The Page in which this url was found.
     * @return if the url should be included in the crawl it returns true,
     *         otherwise false is returned.
     */
    public boolean shouldVisit(Page referringPage, WebURL url) {
        if (myController.getConfig().isRespectNoFollow()) {
            return !((referringPage != null &&
                    referringPage.getContentType() != null &&
                    referringPage.getContentType().contains("html") &&
                    ((HtmlParseData)referringPage.getParseData())
                        .getMetaTagValue("robots")
                        .contains("nofollow")) ||
                    url.getAttribute("rel").contains("nofollow"));
        }

        return true;
    }

    /**
     * Determine whether links found at the given URL should be added to the queue for crawling.
     * By default this method returns true always, but classes that extend WebCrawler can
     * override it in order to implement particular policies about which pages should be
     * mined for outgoing links and which should not.
     *
     * If links from the URL are not being followed, then we are not operating as
     * a web crawler and need not check robots.txt before fetching the single URL.
     * (see definition at http://www.robotstxt.org/faq/what.html).  Thus URLs that
     * return false from this method will not be subject to robots.txt filtering.
     *
     * @param url the URL of the page under consideration
     * @return true if outgoing links from this page should be added to the queue.
     */
    protected boolean shouldFollowLinksIn(WebURL url) {
        return true;
    }

    /**
     * Classes that extends WebCrawler should overwrite this function to process
     * the content of the fetched and parsed page.
     *
     * @param page
     *            the page object that is just fetched and parsed.
     */
    public void visit(Page page) {
        // Do nothing by default
        // Sub-classed should override this to add their custom functionality
    }

    private void processPage(WebURL curURL) throws IOException, InterruptedException, ParseException {
        PageFetchResult fetchResult = null;
        Page page = new Page(curURL);
        try {
            if (curURL == null) {
                return;
            }

AutoFixClass autoFix0 = new AutoFixClass(pageFetcher, curURL, page);
autoFix0.autoFixMethod0(fetchResult);
page = autoFix0.getPage();
int statusCode = autoFix0.getStatusCode();
fetchResult = autoFix0.getFetchResult();


            page.setStatusCode(statusCode);
            if (statusCode < 200 ||
                statusCode > 299) { // Not 2XX: 2XX status codes indicate success
                if (statusCode == HttpStatus.SC_MOVED_PERMANENTLY ||
                    statusCode == HttpStatus.SC_MOVED_TEMPORARILY ||
                    statusCode == HttpStatus.SC_MULTIPLE_CHOICES ||
                    statusCode == HttpStatus.SC_SEE_OTHER ||
                    statusCode == HttpStatus.SC_TEMPORARY_REDIRECT ||
                    statusCode == 308) { // is 3xx  todo
                    // follow https://issues.apache.org/jira/browse/HTTPCORE-389

AutoFixClass autoFix1 = new AutoFixClass();
autoFix1.autoFixMethod1();
movedToUrl = autoFix1.getMovedToUrl();
page = autoFix1.getPage();


                    if (movedToUrl == null) {
AutoFixClass autoFix2 = new AutoFixClass();
autoFix2.autoFixMethod2();


                        return;
                    }
AutoFixClass autoFix3 = new AutoFixClass();
autoFix3.autoFixMethod3();
page = autoFix3.getPage();



                    if (myController.getConfig().isFollowRedirects()) {
AutoFixClass autoFix4 = new AutoFixClass();
autoFix4.autoFixMethod4();
int newDocId = autoFix4.getNewDocId();


                        if (newDocId > 0) {
AutoFixClass autoFix5 = new AutoFixClass();
autoFix5.autoFixMethod5();


                            return;
                        }

AutoFixClass autoFix6 = new AutoFixClass();
autoFix6.autoFixMethod6();
webURL = autoFix6.getWebURL();


AutoFixClass autoFix7 = new AutoFixClass();
autoFix7.autoFixMethod7();
webURL = autoFix7.getWebURL();


                        if (shouldVisit(page, webURL)) {
AutoFixClass autoFix8 = new AutoFixClass();
autoFix8.autoFixMethod8();
webURL = autoFix8.getWebURL();


                        } else {
                            logger.debug("Not visiting: {} as per your \"shouldVisit\" policy",
                                         webURL.getURL());
                        }
                    }
                } else { // All other http codes other than 3xx & 200
AutoFixClass autoFix9 = new AutoFixClass();
autoFix9.autoFixMethod9();


                }

            } else { // if status code is 200
                if (!curURL.getURL().equals(fetchResult.getFetchedUrl())) {
                    if (docIdServer.isSeenBefore(fetchResult.getFetchedUrl())) {
AutoFixClass autoFix10 = new AutoFixClass();
autoFix10.autoFixMethod10();


                        return;
                    }
AutoFixClass autoFix11 = new AutoFixClass();
autoFix11.autoFixMethod11();
curURL = autoFix11.getCurURL();


                }

AutoFixClass autoFix12 = new AutoFixClass();
autoFix12.autoFixMethod12();



AutoFixClass autoFix13 = new AutoFixClass();
autoFix13.autoFixMethod13();



                if (shouldFollowLinksIn(page.getWebURL())) {
                    ParseData parseData = page.getParseData();
                    List<WebURL> toSchedule = new ArrayList<>();
                    int maxCrawlDepth = myController.getConfig().getMaxDepthOfCrawling();
                    for (WebURL webURL : parseData.getOutgoingUrls()) {
                        webURL.setParentDocid(curURL.getDocid());
                        webURL.setParentUrl(curURL.getURL());
                        int newdocid = docIdServer.getDocId(webURL.getURL());
                        if (newdocid > 0) {
                            // This is not the first time that this Url is visited. So, we set the
                            // depth to a negative number.
                            webURL.setDepth((short) -1);
                            webURL.setDocid(newdocid);
                        } else {
                            webURL.setDocid(-1);
                            webURL.setDepth((short) (curURL.getDepth() + 1));
                            if ((maxCrawlDepth == -1) || (curURL.getDepth() < maxCrawlDepth)) {
                                if (shouldVisit(page, webURL)) {
AutoFixClass autoFix14 = new AutoFixClass();
autoFix14.autoFixMethod14();
toSchedule = autoFix14.getToSchedule();
webURL = autoFix14.getWebURL();


                                } else {
                                    logger.debug(
                                        "Not visiting: {} as per your \"shouldVisit\" policy",
                                        webURL.getURL());
                                }
                            }
                        }
                    }
                    frontier.scheduleAll(toSchedule);
                } else {
                    logger.debug("Not looking for links in page {}, "
                                 + "as per your \"shouldFollowLinksInPage\" policy",
                                 page.getWebURL().getURL());
                }

AutoFixClass autoFix15 = new AutoFixClass();
autoFix15.autoFixMethod15();


            }
        } catch (PageBiggerThanMaxSizeException e) {
            onPageBiggerThanMaxSize(curURL.getURL(), e.getPageSize());
        } catch (ParseException pe) {
            onParseError(curURL, pe);
        } catch (ContentFetchException | SocketTimeoutException cfe) {
            onContentFetchError(curURL);
            onContentFetchError(page);
        } catch (NotAllowedContentException nace) {
            logger.debug(
                "Skipping: {} as it contains binary content which you configured not to crawl",
                curURL.getURL());
        } catch (IOException | InterruptedException | RuntimeException e) {
            onUnhandledException(curURL, e);
        } finally {
            if (fetchResult != null) {
                fetchResult.discardContentIfNotConsumed();
            }
        }
    }

    public Thread getThread() {
        return myThread;
    }

    public void setThread(Thread myThread) {
        this.myThread = myThread;
    }

    public boolean isNotWaitingForNewURLs() {
        return !isWaitingForNewURLs;
    }

    protected synchronized Throwable getError() {
        return error;
    }

    private synchronized void setError(Throwable error) {
        this.error = error;
    }
}

