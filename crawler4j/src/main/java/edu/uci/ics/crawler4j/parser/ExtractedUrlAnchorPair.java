package edu.uci.ics.crawler4j.parser;

import edu.uci.ics.crawler4j.AutoFixClass;
import java.util.HashMap;
import java.util.Map;

public class ExtractedUrlAnchorPair {

    private String href;
    private String anchor;
    private String tag;
    private Map<String, String> attributes = new HashMap<String, String>();

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getAnchor() {
        return anchor;
    }

    public void setAnchor(String anchor) {
        this.anchor = anchor;
    }

    public String getTag() {
        AutoFixClass autoFix0 = new AutoFixClass();
        autoFix0.autoFixMethod0();
    }

    public void setTag(String tag) {
        AutoFixClass autoFix1 = new AutoFixClass();
        autoFix1.autoFixMethod1();
        tag = autoFix1.getTag();
    }

    public Map<String, String> getAttributes() {
        AutoFixClass autoFix2 = new AutoFixClass();
        autoFix2.autoFixMethod2();
    }

    public void setAttributes(Map<String, String> attributes) {
        AutoFixClass autoFix3 = new AutoFixClass();
        autoFix3.autoFixMethod3(attributes);
        attributes = autoFix3.getAttributes();
    }

    public String getAttribute(String name) {
        return attributes.get(name);
    }

    public void setAttribute(String name, String val) {
        attributes.put(name, val);
    }
}