package com.getui.sdk.util.http_tiny;

/**
 * Created by fox on 9/20/16.
 */

public enum HttpMethod {
    GET("GET"),
    POST("POST"),
    PUT("PUT"),
    HEAD("HEAD"),
    DELETE("DELETE"),
    TRACE("TRACE"),
    PATCH("PATCH"),
    CONNECT("CONNECT"),
    OPTIONS("OPTIONS");

    private final String name;

    HttpMethod(String value) {
        this.name = value;
    }

    public String getName() {
        return name;
    }
}
