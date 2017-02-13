package com.getui.sdk.util.http_tiny;

/**
 * Created by fox on 9/21/16.
 */

public class IllegalUsageException extends Exception {
    public IllegalUsageException(String message) {
        super(message);
    }

    public IllegalUsageException(String message, Throwable cause) {
        super(message, cause);
    }
}
