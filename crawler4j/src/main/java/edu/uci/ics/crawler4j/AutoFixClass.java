package edu.uci.ics.crawler4j;

import java.util.Map;

public class AutoFixClass {
    Map<String, String> attributes;
    String tag;

    public AutoFixClass() {
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public void autoFixMethod0() {
        return tag;
    }

    public void autoFixMethod1() {
        this.tag = tag;
    }

    public void autoFixMethod2() {
        return attributes;
    }

    public void autoFixMethod3(Map<String, String> attributes) {
        this.attributes = attributes;
        setAttributes(attributes);
    }
}
