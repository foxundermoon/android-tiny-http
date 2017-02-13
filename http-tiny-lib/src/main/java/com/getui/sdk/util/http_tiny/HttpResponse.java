package com.getui.sdk.util.http_tiny;

import java.nio.charset.Charset;

/**
 * Created by fox on 9/20/16.
 */

public class HttpResponse {
    private int status;
    private HttpHeaders headers;

    private byte[] body;

    public HttpResponse(int status, byte[] body, HttpHeaders headers) {
        this.status = status;
        this.body = body;
        this.headers = headers;
    }

    public int getStatus() {
        return status;
    }


    public byte[] getBody() {
        return body;
    }

    public String getTextBody() {
        if (body == null) {
            return "";
        }
        return new String(body, Charset.defaultCharset());
    }

    public HttpHeaders getHeaders() {
        return headers;
    }

    public String getHeader(String key) {
        if (headers == null) {
            return null;
        }
        return headers.get(key).toString();
    }
}
