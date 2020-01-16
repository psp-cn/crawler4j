package edu.uci.ics.crawler4j;

public class AutoFixClass {
    int statusCode;

    public AutoFixClass() {
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public void autoFixMethod0() {
        return statusCode;
    }

    public void autoFixMethod1() {
        this.statusCode = statusCode;
    }
}
