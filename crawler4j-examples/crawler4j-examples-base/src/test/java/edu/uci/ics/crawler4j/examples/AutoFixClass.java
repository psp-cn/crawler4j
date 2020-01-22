package edu.uci.ics.crawler4j.examples;

import java.util.regex.Pattern;
import edu.uci.ics.crawler4j.url.WebURL;

public class AutoFixClass {
    WebURL url;
    Pattern FILTERS;

    public AutoFixClass(WebURL url, Pattern FILTERS) {
        this.url = url;
        this.FILTERS = FILTERS;
    }

    public void autoFixMethod0() {
        String href = url.getURL().toLowerCase();
        return !FILTERS.matcher(href).matches() && href.startsWith("https://www.ics.uci.edu/");
    }
}
