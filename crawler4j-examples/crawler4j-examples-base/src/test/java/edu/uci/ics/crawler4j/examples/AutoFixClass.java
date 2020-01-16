package edu.uci.ics.crawler4j.examples;

import java.util.regex.Pattern;
import edu.uci.ics.crawler4j.url.WebURL;

public class AutoFixClass {
    Pattern FILTERS;
    WebURL url;

    public AutoFixClass(Pattern FILTERS, WebURL url) {
        this.FILTERS = FILTERS;
        this.url = url;
    }

    public void autoFixMethod0() {
        String href = url.getURL().toLowerCase();
        return !FILTERS.matcher(href).matches() && href.startsWith("https://www.ics.uci.edu/");
    }
}
