package com.maxwellnie.velox.sql.core.natives.exception;

/**
 * @author Maxwell Nie
 */
public class SessionException extends RuntimeException {
    public SessionException() {
    }

    public SessionException(String message) {
        super(message);
    }

    public SessionException(String message, Throwable cause) {
        super(message, cause);
    }

    public SessionException(Throwable cause) {
        super(cause);
    }

    public SessionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
