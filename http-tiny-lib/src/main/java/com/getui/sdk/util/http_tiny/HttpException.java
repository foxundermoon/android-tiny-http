package com.getui.sdk.util.http_tiny;

/**
 * Created by fox on 9/21/16.
 */

public class HttpException extends Exception {
    public HttpException() {
        super();
    }

    public HttpException(String message) {
        super(message);
    }

    public HttpException(String message, Throwable cause) {
        super(message, cause);
    }

    public HttpException(Throwable cause) {
        super(cause);
    }
}
