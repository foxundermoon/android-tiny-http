package com.getui.sdk.util.http_tiny;

import java.util.TreeMap;

/**
 * Created by fox on 9/20/16.
 */

public class HttpHeaders extends TreeMap<String, Object> {

    private static final String Accept = "Accept";
    private static final String Host = "Host";
    private static final String UserAgent = "User-Agent";
    private static final String AcceptCharset = "Accept-Charset";
    private static final String AcceptEncoding = "Accept-Encoding";
    private static final String Connection = "Connection";
    private static final String ContentType = "Content-Type";

    public HttpHeaders() {
        super(String.CASE_INSENSITIVE_ORDER);
    }

    public HttpHeaders setAccept(String accept) {
        return put(Accept, accept);
    }

    public HttpHeaders setHost(String host) {
        return put(Host, host);
    }

    public HttpHeaders setUserAgent(String userAgent) {
        return put(UserAgent, userAgent);
    }

    public HttpHeaders setAcceptCharset(String charset) {
        return put(AcceptCharset, charset);
    }

    public HttpHeaders setAcceptEncoding(String encoding) {
        return put(AcceptEncoding, encoding);
    }

    public HttpHeaders setConnection(String connection) {
        return put(Connection, connection);
    }

    public HttpHeaders setContentType(String contentType) {
        return put(ContentType, contentType);
    }

    @Override
    public HttpHeaders put(String key, Object value) {
        super.put(key, value);
        return this;
    }

}
