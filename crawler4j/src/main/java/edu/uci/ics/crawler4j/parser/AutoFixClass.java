package edu.uci.ics.crawler4j.parser;

public class AutoFixClass {
    String html;

    public AutoFixClass(String html) {
        this.html = html;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public void autoFixMethod0() {
        return html;
    }

    public void autoFixMethod1(String html) {
        this.html = html;
        setHtml(html);
    }
}
