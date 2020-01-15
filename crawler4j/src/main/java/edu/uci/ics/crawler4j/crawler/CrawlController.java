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

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;

import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.frontier.DocIDServer;
import edu.uci.ics.crawler4j.frontier.Frontier;
import edu.uci.ics.crawler4j.parser.Parser;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import edu.uci.ics.crawler4j.url.TLDList;
import edu.uci.ics.crawler4j.url.URLCanonicalizer;
import edu.uci.ics.crawler4j.url.WebURL;
import edu.uci.ics.crawler4j.util.IO;

/**
 * The controller that manages a crawling session. This class creates the
 * crawler threads and monitors their progress.
 *
 * @author Yasser Ganjisaffar
 */
public class CrawlController {

    private class AutoFixClass {
        T crawler;
        List<T> crawlers;
        CrawlController controller;
        List<Thread> threads;
        List<Object> crawlersLocalData;
        WebCrawlerFactory<T> crawlerFactory;
        boolean finished;
        Logger logger;
        int i;

        public AutoFixClass(List<T> crawlers, CrawlController controller, List<Thread> threads,
                WebCrawlerFactory<T> crawlerFactory, Logger logger, int i) {
            this.crawlers = crawlers;
            this.controller = controller;
            this.threads = threads;
            this.crawlerFactory = crawlerFactory;
            this.logger = logger;
            this.i = i;
        }

        public T getCrawler() {
            return crawler;
        }

        public void setCrawler(T crawler) {
            this.crawler = crawler;
        }

        public Throwable getCrawlers() {
            return crawlers;
        }

        public void setCrawlers(Throwable crawlers) {
            this.crawlers = crawlers;
        }

        public List<Object> getCrawlersLocalData() {
            return crawlersLocalData;
        }

        public void setCrawlersLocalData(List<Object> crawlersLocalData) {
            this.crawlersLocalData = crawlersLocalData;
        }

        public List<Thread> getThreads() {
            return threads;
        }

        public void setThreads(List<Thread> threads) {
            this.threads = threads;
        }

        public boolean getFinished() {
            return finished;
        }

        public void setFinished(boolean finished) {
            this.finished = finished;
        }

        public void autoFixMethod0(Thread thread) {
            logger.info("Thread {} was dead, I'll recreate it", i);
            T crawler = crawlerFactory.newInstance();
            thread = new Thread(crawler, "Crawler " + (i + 1));
            threads.remove(i);
            threads.add(i, thread);
            crawler.setThread(thread);
            crawler.init(i + 1, controller);
            thread.start();
            crawlers.remove(i);
        }

        public void autoFixMethod1() {
            Throwable t = crawlers.get(i).getError();
            if (t != null && config.isHaltOnError()) {
                throw new RuntimeException("error on thread [" + threads.get(i).getName() + "]", t);
            }
        }

        public void autoFixMethod2(boolean someoneIsWorking) {
            logger.info("It looks like no thread is working, waiting for " + config.getThreadShutdownDelaySeconds()
                    + " seconds to make sure...");
            sleep(config.getThreadShutdownDelaySeconds());
            someoneIsWorking = false;
        }

        public void autoFixMethod3(boolean someoneIsWorking) {
            for (int i = 0; i < threads.size(); i++) {
                Thread thread = threads.get(i);
                if (thread.isAlive() && crawlers.get(i).isNotWaitingForNewURLs()) {
                    someoneIsWorking = true;
                }
            }
        }

        public void autoFixMethod4() {
            long queueLength = frontier.getQueueLength();
        }

        public void autoFixMethod5() {
            logger.info("No thread is working and no more URLs are in " + "queue waiting for another "
                    + config.getThreadShutdownDelaySeconds() + " seconds to make sure...");
        }

        public void autoFixMethod6(long queueLength) {
            sleep(config.getThreadShutdownDelaySeconds());
            queueLength = frontier.getQueueLength();
        }

        public void autoFixMethod7() {
            logger.info("All of the crawlers are stopped. Finishing the " + "process...");
            frontier.finish();
            for (T crawler : crawlers) {
                crawler.onBeforeExit();
                crawlersLocalData.add(crawler.getMyLocalData());
            }
        }

        public void autoFixMethod8(boolean finished) {
            logger.info("Waiting for " + config.getCleanupDelaySeconds() + " seconds before final clean up...");
            sleep(config.getCleanupDelaySeconds());
            frontier.close();
            docIdServer.close();
            pageFetcher.shutDown();
            finished = true;
            waitingLock.notifyAll();
            setFinished(finished);
        }
    }

    static final Logger logger = LoggerFactory.getLogger(CrawlController.class);
    private final CrawlConfig config;

    /**
     * The 'customData' object can be used for passing custom crawl-related
     * configurations to different components of the crawler.
     */
    protected Object customData;

    /**
     * Once the crawling session finishes the controller collects the local data
     * of the crawler threads and stores them in this List.
     */
    protected List<Object> crawlersLocalData = new ArrayList<>();

    /**
     * Is the crawling of this session finished?
     */
    protected boolean finished;
    private Throwable error;

    /**
     * Is the crawling session set to 'shutdown'. Crawler threads monitor this
     * flag and when it is set they will no longer process new pages.
     */
    protected boolean shuttingDown;

    protected PageFetcher pageFetcher;
    protected RobotstxtServer robotstxtServer;
    protected Frontier frontier;
    protected DocIDServer docIdServer;
    protected TLDList tldList;

    protected final Object waitingLock = new Object();
    protected final Environment env;

    protected Parser parser;

    public CrawlController(CrawlConfig config, PageFetcher pageFetcher,
                           RobotstxtServer robotstxtServer) throws Exception {
        this(config, pageFetcher, null, robotstxtServer, null);
    }

    public CrawlController(CrawlConfig config, PageFetcher pageFetcher,
            RobotstxtServer robotstxtServer, TLDList tldList) throws Exception {
        this(config, pageFetcher, null, robotstxtServer, tldList);
    }

    public CrawlController(CrawlConfig config, PageFetcher pageFetcher, Parser parser,
                           RobotstxtServer robotstxtServer, TLDList tldList) throws Exception {
        config.validate();
        this.config = config;

        File folder = new File(config.getCrawlStorageFolder());
        if (!folder.exists()) {
            if (folder.mkdirs()) {
                logger.debug("Created folder: " + folder.getAbsolutePath());
            } else {
                throw new Exception(
                    "couldn't create the storage folder: " + folder.getAbsolutePath() +
                    " does it already exist ?");
            }
        }

        this.tldList = tldList == null ? new TLDList(config) : tldList;
        URLCanonicalizer.setHaltOnError(config.isHaltOnError());

        boolean resumable = config.isResumableCrawling();

        EnvironmentConfig envConfig = new EnvironmentConfig();
        envConfig.setAllowCreate(true);
        envConfig.setTransactional(resumable);
        envConfig.setLocking(resumable);
        envConfig.setLockTimeout(config.getDbLockTimeout(), TimeUnit.MILLISECONDS);

        File envHome = new File(config.getCrawlStorageFolder() + "/frontier");
        if (!envHome.exists()) {
            if (envHome.mkdir()) {
                logger.debug("Created folder: " + envHome.getAbsolutePath());
            } else {
                throw new Exception(
                    "Failed creating the frontier folder: " + envHome.getAbsolutePath());
            }
        }

        if (!resumable) {
            IO.deleteFolderContents(envHome);
            logger.info("Deleted contents of: " + envHome +
                        " ( as you have configured resumable crawling to false )");
        }

        env = new Environment(envHome, envConfig);
        docIdServer = new DocIDServer(env, config);
        frontier = new Frontier(env, config);

        this.pageFetcher = pageFetcher;
        this.parser = parser == null ? new Parser(config, tldList) : parser;
        this.robotstxtServer = robotstxtServer;

        finished = false;
        shuttingDown = false;

        robotstxtServer.setCrawlConfig(config);
    }

    public Parser getParser() {
        return parser;
    }

    public interface WebCrawlerFactory<T extends WebCrawler> {
        T newInstance() throws Exception;
    }

    private static class SingleInstanceFactory<T extends WebCrawler>
        implements WebCrawlerFactory<T> {

        final T instance;

        SingleInstanceFactory(T instance) {
            this.instance = instance;
        }

        @Override
        public T newInstance() throws Exception {
            return this.instance;
        }
    }

    private static class DefaultWebCrawlerFactory<T extends WebCrawler>
        implements WebCrawlerFactory<T> {
        final Class<T> clazz;

        DefaultWebCrawlerFactory(Class<T> clazz) {
            this.clazz = clazz;
        }

        @Override
        public T newInstance() throws Exception {
            try {
                return clazz.newInstance();
            } catch (ReflectiveOperationException e) {
                throw e;
            }
        }
    }

    /**
     * Start the crawling session and wait for it to finish.
     * This method utilizes default crawler factory that creates new crawler using Java reflection
     *
     * @param clazz
     *            the class that implements the logic for crawler threads
     * @param numberOfCrawlers
     *            the number of concurrent threads that will be contributing in
     *            this crawling session.
     * @param <T> Your class extending WebCrawler
     */
    public <T extends WebCrawler> void start(Class<T> clazz, int numberOfCrawlers) {
        this.start(new DefaultWebCrawlerFactory<>(clazz), numberOfCrawlers, true);
    }

    /**
     * Start the crawling session and wait for it to finish.
     * This method depends on a single instance of a crawler. Only that instance will be used for crawling.
     *
     * @param instance
     *            the instance of a class that implements the logic for crawler threads
     * @param <T> Your class extending WebCrawler
     */
    public <T extends WebCrawler> void start(T instance) {
        this.start(new SingleInstanceFactory<>(instance), 1, true);
    }

    /**
     * Start the crawling session and wait for it to finish.
     *
     * @param crawlerFactory
     *            factory to create crawlers on demand for each thread
     * @param numberOfCrawlers
     *            the number of concurrent threads that will be contributing in
     *            this crawling session.
     * @param <T> Your class extending WebCrawler
     */
    public <T extends WebCrawler> void start(WebCrawlerFactory<T> crawlerFactory,
                                             int numberOfCrawlers) {
        this.start(crawlerFactory, numberOfCrawlers, true);
    }

    /**
     * Start the crawling session and return immediately.
     *
     * @param crawlerFactory
     *            factory to create crawlers on demand for each thread
     * @param numberOfCrawlers
     *            the number of concurrent threads that will be contributing in
     *            this crawling session.
     * @param <T> Your class extending WebCrawler
     */
    public <T extends WebCrawler> void startNonBlocking(WebCrawlerFactory<T> crawlerFactory,
                                                        final int numberOfCrawlers) {
        this.start(crawlerFactory, numberOfCrawlers, false);
    }

    /**
     * Start the crawling session and return immediately.
     * This method utilizes default crawler factory that creates new crawler using Java reflection
     *
     * @param clazz
     *            the class that implements the logic for crawler threads
     * @param numberOfCrawlers
     *            the number of concurrent threads that will be contributing in
     *            this crawling session.
     * @param <T> Your class extending WebCrawler
     */
    public <T extends WebCrawler> void startNonBlocking(Class<T> clazz, int numberOfCrawlers) {
        start(new DefaultWebCrawlerFactory<>(clazz), numberOfCrawlers, false);
    }

    protected <T extends WebCrawler> void start(final WebCrawlerFactory<T> crawlerFactory,
                                                final int numberOfCrawlers, boolean isBlocking) {
        try {
            finished = false;
            setError(null);
            crawlersLocalData.clear();
            final List<Thread> threads = new ArrayList<>();
            final List<T> crawlers = new ArrayList<>();

            for (int i = 1; i <= numberOfCrawlers; i++) {
                T crawler = crawlerFactory.newInstance();
                Thread thread = new Thread(crawler, "Crawler " + i);
                crawler.setThread(thread);
                crawler.init(i, this);
                thread.start();
                crawlers.add(crawler);
                threads.add(thread);
                logger.info("Crawler {} started", i);
            }

            final CrawlController controller = this;
            Thread monitorThread = new Thread(new Runnable() {

                @Override
                public void run() {
                    try {
                        synchronized (waitingLock) {

                            while (true) {
                                sleep(config.getThreadMonitoringDelaySeconds());
                                boolean someoneIsWorking = false;
                                for (int i = 0; i < threads.size(); i++) {
                                    Thread thread = threads.get(i);
                                    if (!thread.isAlive()) {
                                        if (!shuttingDown && !config.isHaltOnError()) {
                                            AutoFixClass autoFix0 = new AutoFixClass(crawlers, controller, threads,
                                                    crawlerFactory, logger, i);
                                            autoFix0.autoFixMethod0(thread);
                                            T crawler = autoFix0.getCrawler();
                                            crawlers = autoFix0.getCrawlers();
                                            threads = autoFix0.getThreads();
                                            crawlers.add(i, crawler);
                                        }
                                    } else if (crawlers.get(i).isNotWaitingForNewURLs()) {
                                        someoneIsWorking = true;
                                    }
                                    AutoFixClass autoFix1 = new AutoFixClass();
                                    autoFix1.autoFixMethod1();
                                }
                                boolean shutOnEmpty = config.isShutdownOnEmptyQueue();
                                if (!someoneIsWorking && shutOnEmpty) {
                                    AutoFixClass autoFix2 = new AutoFixClass();
                                    autoFix2.autoFixMethod2(someoneIsWorking);
                                    AutoFixClass autoFix3 = new AutoFixClass();
                                    autoFix3.autoFixMethod3(someoneIsWorking);
                                    if (!someoneIsWorking) {
                                        if (!shuttingDown) {
                                            AutoFixClass autoFix4 = new AutoFixClass();
                                            autoFix4.autoFixMethod4();
                                            if (queueLength > 0) {
                                                continue;
                                            }
                                            AutoFixClass autoFix5 = new AutoFixClass();
                                            autoFix5.autoFixMethod5();
                                            AutoFixClass autoFix6 = new AutoFixClass();
                                            autoFix6.autoFixMethod6(queueLength);
                                            if (queueLength > 0) {
                                                continue;
                                            }
                                        }
                                        AutoFixClass autoFix7 = new AutoFixClass();
                                        autoFix7.autoFixMethod7();
                                        crawlersLocalData = autoFix7.getCrawlersLocalData();
                                        AutoFixClass autoFix8 = new AutoFixClass();
                                        autoFix8.autoFixMethod8(finished);
                                        finished = autoFix8.getFinished();

                                        env.close();

                                        return;
                                    }
                                }
                            }
                        }
                    } catch (Throwable e) {
                        if (config.isHaltOnError()) {
                            setError(e);
                            synchronized (waitingLock) {
                                frontier.finish();
                                frontier.close();
                                docIdServer.close();
                                pageFetcher.shutDown();
                                waitingLock.notifyAll();
                                env.close();
                            }
                        } else {
                            logger.error("Unexpected Error", e);
                        }
                    }
                }

            });

            monitorThread.start();

            if (isBlocking) {
                waitUntilFinish();
            }

        } catch (Exception e) {
            if (config.isHaltOnError()) {
                if (e instanceof RuntimeException) {
                    throw (RuntimeException)e;
                } else {
                    throw new RuntimeException("error running the monitor thread", e);
                }
            } else {
                logger.error("Error happened", e);
            }
        }
    }

    /**
     * Wait until this crawling session finishes.
     */
    public void waitUntilFinish() {
        while (!finished) {
            synchronized (waitingLock) {
                if (config.isHaltOnError()) {
                    Throwable t = getError();
                    if (t != null && config.isHaltOnError()) {
                        if (t instanceof RuntimeException) {
                            throw (RuntimeException)t;
                        } else if (t instanceof Error) {
                            throw (Error)t;
                        } else {
                            throw new RuntimeException("error on monitor thread", t);
                        }
                    }
                }
                if (finished) {
                    return;
                }
                try {
                    waitingLock.wait();
                } catch (InterruptedException e) {
                    logger.error("Error occurred", e);
                }
            }
        }
    }

    /**
     * Once the crawling session finishes the controller collects the local data of the crawler
     * threads and stores them
     * in a List.
     * This function returns the reference to this list.
     *
     * @return List of Objects which are your local data
     */
    public List<Object> getCrawlersLocalData() {
        return crawlersLocalData;
    }

    protected static void sleep(int seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException ignored) {
            // Do nothing
        }
    }

    /**
     * Adds a new seed URL. A seed URL is a URL that is fetched by the crawler
     * to extract new URLs in it and follow them for crawling.
     *
     * @param pageUrl
     *            the URL of the seed
     *
     * @throws InterruptedException
     * @throws IOException
     */
    public void addSeed(String pageUrl) throws IOException, InterruptedException {
        addSeed(pageUrl, -1);
    }

    /**
     * Adds a new seed URL. A seed URL is a URL that is fetched by the crawler
     * to extract new URLs in it and follow them for crawling. You can also
     * specify a specific document id to be assigned to this seed URL. This
     * document id needs to be unique. Also, note that if you add three seeds
     * with document ids 1,2, and 7. Then the next URL that is found during the
     * crawl will get a doc id of 8. Also you need to ensure to add seeds in
     * increasing order of document ids.
     *
     * Specifying doc ids is mainly useful when you have had a previous crawl
     * and have stored the results and want to start a new crawl with seeds
     * which get the same document ids as the previous crawl.
     *
     * @param pageUrl
     *            the URL of the seed
     * @param docId
     *            the document id that you want to be assigned to this seed URL.
     *
     * @throws InterruptedException
     * @throws IOException
     */
    public void addSeed(String pageUrl, int docId) throws IOException, InterruptedException {
        String canonicalUrl = URLCanonicalizer.getCanonicalURL(pageUrl);
        if (canonicalUrl == null) {
            logger.error("Invalid seed URL: {}", pageUrl);
        } else {
            if (docId < 0) {
                docId = docIdServer.getDocId(canonicalUrl);
                if (docId > 0) {
                    logger.trace("This URL is already seen.");
                    return;
                }
                docId = docIdServer.getNewDocID(canonicalUrl);
            } else {
                try {
                    docIdServer.addUrlAndDocId(canonicalUrl, docId);
                } catch (RuntimeException e) {
                    if (config.isHaltOnError()) {
                        throw e;
                    } else {
                        logger.error("Could not add seed: {}", e.getMessage());
                    }
                }
            }

            WebURL webUrl = new WebURL();
            webUrl.setTldList(tldList);
            webUrl.setURL(canonicalUrl);
            webUrl.setDocid(docId);
            webUrl.setDepth((short) 0);
            if (robotstxtServer.allows(webUrl)) {
                frontier.schedule(webUrl);
            } else {
                // using the WARN level here, as the user specifically asked to add this seed
                logger.warn("Robots.txt does not allow this seed: {}", pageUrl);
            }
        }
    }

    /**
     * This function can called to assign a specific document id to a url. This
     * feature is useful when you have had a previous crawl and have stored the
     * Urls and their associated document ids and want to have a new crawl which
     * is aware of the previously seen Urls and won't re-crawl them.
     *
     * Note that if you add three seen Urls with document ids 1,2, and 7. Then
     * the next URL that is found during the crawl will get a doc id of 8. Also
     * you need to ensure to add seen Urls in increasing order of document ids.
     *
     * @param url
     *            the URL of the page
     * @param docId
     *            the document id that you want to be assigned to this URL.
     * @throws UnsupportedEncodingException
     *
     */
    public void addSeenUrl(String url, int docId) throws UnsupportedEncodingException {
        String canonicalUrl = URLCanonicalizer.getCanonicalURL(url);
        if (canonicalUrl == null) {
            logger.error("Invalid Url: {} (can't cannonicalize it!)", url);
        } else {
            try {
                docIdServer.addUrlAndDocId(canonicalUrl, docId);
            } catch (RuntimeException e) {
                if (config.isHaltOnError()) {
                    throw e;
                } else {
                    logger.error("Could not add seen url: {}", e.getMessage());
                }
            }
        }
    }

    public PageFetcher getPageFetcher() {
        return pageFetcher;
    }

    public void setPageFetcher(PageFetcher pageFetcher) {
        this.pageFetcher = pageFetcher;
    }

    public RobotstxtServer getRobotstxtServer() {
        return robotstxtServer;
    }

    public void setRobotstxtServer(RobotstxtServer robotstxtServer) {
        this.robotstxtServer = robotstxtServer;
    }

    public Frontier getFrontier() {
        return frontier;
    }

    public void setFrontier(Frontier frontier) {
        this.frontier = frontier;
    }

    public DocIDServer getDocIdServer() {
        return docIdServer;
    }

    public void setDocIdServer(DocIDServer docIdServer) {
        this.docIdServer = docIdServer;
    }

    /**
     * @deprecated implements a factory {@link WebCrawlerFactory} and inject your cutom data as
     * shown <a href="https://github.com/yasserg/crawler4j#using-a-factory">here</a> .
     */
    @Deprecated
    public Object getCustomData() {
        return customData;
    }

    /**
     * @deprecated implements a factory {@link WebCrawlerFactory} and inject your cutom data as
     * shown <a href="https://github.com/yasserg/crawler4j#using-a-factory">here</a> .
     */

    @Deprecated
    public void setCustomData(Object customData) {
        this.customData = customData;
    }

    public boolean isFinished() {
        return this.finished;
    }

    public boolean isShuttingDown() {
        return shuttingDown;
    }

    /**
     * Set the current crawling session set to 'shutdown'. Crawler threads
     * monitor the shutdown flag and when it is set to true, they will no longer
     * process new pages.
     */
    public void shutdown() {
        logger.info("Shutting down...");
        this.shuttingDown = true;
        pageFetcher.shutDown();
        frontier.finish();
    }

    public CrawlConfig getConfig() {
        return config;
    }

    protected synchronized Throwable getError() {
        return error;
    }

    private synchronized void setError(Throwable e) {
        this.error = e;
    }

    public TLDList getTldList() {
        return tldList;
    }
}
