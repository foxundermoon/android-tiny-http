package com.getui.sdk.util.http_tiny;

import java.io.File;

/**
 * Created by fox on 9/20/16.
 */

public class Part<T> {
    private String name;
    private String mediaType;
    private String filename;

    public enum Type {
        File,
        //        Bytes,
//        Stream,
        String,
        Unknown
    }

    public Type getPartType() {
        if (content instanceof File) {
            return Type.File;
        }
//        if (content instanceof byte[]) {
//            return Type.Bytes;
//        }
//        if (content instanceof InputStream) {
//            return Type.Stream;
//        }
        if (content instanceof String) {
            return Type.String;
        }
        return Type.Unknown;
    }

    private T content;

    public Part(String name, T content, String filename, String mediaType) {
        this.content = content;
        this.name = name;
        this.filename = filename;
        this.mediaType = mediaType;
    }

    public T getContent() {
        return content;
    }

    public String getName() {
        return name;
    }

    public String getMediaType() {
        return mediaType;
    }

    public Part(String name, T content) {
        this.name = name;
        this.content = content;
    }

    public String getFilename() {
        return filename;
    }
}
