package edu.uci.ics.crawler4j;

public class AutoFixClass {
    String anchor;

    public AutoFixClass() {
    }

    public String getAnchor() {
        return anchor;
    }

    public void setAnchor(String anchor) {
        this.anchor = anchor;
    }

    public void autoFixMethod0() {
        return anchor;
    }

    public void autoFixMethod1() {
        this.anchor = anchor;
    }
}
