package com.getui.sdk.util.http_tiny;

import java.io.File;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by fox on 9/20/16.
 */

public abstract class HttpRequest {
    protected HttpHeaders headers;
    protected URL url;
    protected HttpMethod httpMethod = HttpMethod.GET;
    protected Body body;
    protected List<Part> multiparts;
    protected int connectTimeout = -1;
    protected int readTimeout = -1;
    protected boolean isUseCache = false;

    protected boolean isHandleResponseBody = true;

    public HttpRequest headers(Map<String, Object> httpHeaders) {
        if (this.headers == null) {
            this.headers = new HttpHeaders();
        }
        {
            for (Map.Entry<String, Object> entry :
                    httpHeaders.entrySet()) {
                header(entry.getKey(), entry.getValue());
            }
        }
        return this;
    }

    public HttpRequest delHeadByKey(String key) {
        if (this.headers != null) {
            this.headers.remove(key);
        }
        return this;
    }

    public HttpRequest header(String key, Object value) {
        if (this.headers == null) {
            headers = new HttpHeaders();
        }
        headers.put(key, value);
        return this;
    }

    public HttpRequest url(URL url) {
        this.url = url;
        return this;
    }

    public HttpRequest connectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
        return this;
    }

    public HttpRequest readTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
        return this;
    }

    public HttpRequest url(String url) throws MalformedURLException {
        this.url = new URL(url);
        return this;
    }

    public HttpRequest useCache() {
        this.isUseCache = true;
        return this;
    }

    public HttpRequest notuseCache() {
        this.isUseCache = false;
        return this;
    }


    public HttpRequest get() {
        return setMethod(HttpMethod.GET);
    }

    public HttpRequest post() {
        return setMethod(HttpMethod.POST);
    }

    public HttpRequest donotHandleResponseBody() {
        this.isHandleResponseBody = false;
        return this;
    }

    public HttpRequest body(byte[] bytes) {
        this.body = new Body(bytes);
        return this;
    }

    public HttpRequest body(File file) {
        this.body = new Body(file);
        return this;
    }

    public HttpRequest body(InputStream inputStream) {
        this.body = new Body(inputStream);
        return this;
    }

    public HttpRequest part(Part part) {
        if (multiparts == null) {
            multiparts = new LinkedList<Part>();
        }
        multiparts.add(part);
        return this;
    }

    public HttpRequest part(String name, String value) {
        part(new Part<String>(name, value));
        return this;
    }

    public HttpRequest part(String name, File file) {
        part(new Part<File>(name, file));
        return this;
    }

    /*
    @Deprecated
    public HttpRequest part(String name, byte[] content) {
        part(new Part<byte[]>(name, content));
        return this;
    }

    */

    public HttpRequest part(String name, File file, String filename, String mediaType) {
        part(new Part<File>(name, file, filename, mediaType));
        return this;
    }

    public HttpRequest setMethod(HttpMethod httpMethod) {
        this.httpMethod = httpMethod;
        return this;
    }


    public abstract HttpResponse execute() throws IllegalUsageException, HttpException;

    public static class Body {
        public Type getType() {
            return type;
        }

        public byte[] getBytesBody() {
            return bytesBody;
        }

        public File getFileBody() {
            return fileBody;
        }

        public InputStream getInputStreamBody() {
            return inputStreamBody;
        }

        public enum Type {
            Bytes,
            File,
            Stream,
            Unknown,
        }

        private Type type;
        private byte[] bytesBody;
        private File fileBody;
        private InputStream inputStreamBody;

        public Body(File file) {
            this.type = Type.File;
            this.fileBody = file;
        }

        public Body(byte[] bytes) {
            this.type = Type.Bytes;
            this.bytesBody = bytes;
        }

        public Body(InputStream inputStream) {
            this.type = Type.Stream;
            this.inputStreamBody = inputStream;
        }
    }

    protected boolean isInMethods(HttpMethod... methods) {
        boolean in = false;
        for (HttpMethod method : methods) {
            if (method == this.httpMethod) {
                in = true;
            }
        }
        return in;
    }


    protected boolean isMultipartsEmpty() {
        return multiparts == null || multiparts.size() == 0;
    }

    protected boolean isHeadersEmpty() {
        return headers == null || headers.size() == 0;
    }


    protected void checkIllegalUsage() throws IllegalUsageException {
        if (url == null) {
            throw new IllegalUsageException("must set the url before execute");
        }
        if (httpMethod == HttpMethod.POST && (body == null && isMultipartsEmpty())) {
            throw new IllegalUsageException("must set body or add some part when use post");
        }
        if (!isMultipartsEmpty() && body != null) {
            throw new IllegalUsageException("can not set the body and add part together");
        }
    }

}
