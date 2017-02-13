package com.getui.sdk.util.http_tiny;

/**
 * Created by fox on 9/21/16.
 */

public class HttpRequestFactoryImpl implements HttpRequestFactory {
    private static HttpRequestFactory instance;

    private HttpRequestFactoryImpl(){

    }

    @Override
    public HttpRequest create() {
        return new DefaultHttpRequest();
    }

    public static HttpRequestFactory getInstance() {
        if (instance == null) {
            synchronized (HttpRequestFactoryImpl.class) {
                if (instance == null) {
                    instance = new HttpRequestFactoryImpl();
                }
            }
        }
        return instance;
    }
}
