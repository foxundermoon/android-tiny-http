package com.getui.sdk.util.http_tiny.utils;

import android.util.Base64;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.UUID;

public class StringUtils {

    private static final String EMPTY = "";

    public static String connect(String... elements) {
        return join(EMPTY, elements);
    }

    public static String join(String delimiter, String... elements) {
        if (elements == null) {
            return EMPTY;
        }
        int length = elements.length;
        if (length == 0) {
            return EMPTY;
        }
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            builder.append(elements[i]);
            if (i != length - 1) {
                builder.append(delimiter);
            }
        }
        return builder.toString();
    }

    public static boolean isEmpty(String string) {
        return string == null || string.length() == 0;
    }

    public static String random() {
        String random = UUID.randomUUID().toString();
        return random.replace("-", EMPTY).toLowerCase();
    }

    public static String convertStreamToString(InputStream inputStream) {
        if (inputStream == null) {
            return EMPTY;
        }
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = null;
        String line;
        try {
            reader = new BufferedReader(new InputStreamReader(inputStream));
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (Exception e) {
            // Ignore
        } finally {
            IOUtils.closeQuietly(reader);
        }
        return sb.toString();
    }

    public static String base64EncodeToStr(String string) {
        return base64EncodeToStr(toBytes(string));
    }

    public static String base64EncodeToStr(byte[] bytes) {
        return Base64.encodeToString(bytes, Base64.NO_WRAP);
    }

    public static byte[] base64Decode(String string) {
        return Base64.decode(string, Base64.NO_WRAP);
    }

    public static byte[] toBytes(String string) {
        return string.getBytes( Charset.forName("UTF-8"));
    }

}
