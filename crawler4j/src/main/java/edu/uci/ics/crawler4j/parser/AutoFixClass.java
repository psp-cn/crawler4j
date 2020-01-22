package edu.uci.ics.crawler4j.parser;

import java.util.Set;
import edu.uci.ics.crawler4j.url.WebURL;

public class AutoFixClass {
    Set<WebURL> outgoingUrls;

    public AutoFixClass(Set<WebURL> outgoingUrls) {
        this.outgoingUrls = outgoingUrls;
    }

    public Set<WebURL> getOutgoingUrls() {
        return outgoingUrls;
    }

    public void setOutgoingUrls(Set<WebURL> outgoingUrls) {
        this.outgoingUrls = outgoingUrls;
    }

    public void autoFixMethod0() {
        return outgoingUrls;
    }

    public void autoFixMethod1(Set<WebURL> outgoingUrls) {
        this.outgoingUrls = outgoingUrls;
        setOutgoingUrls(outgoingUrls);
    }
}
