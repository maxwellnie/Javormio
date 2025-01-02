package com.maxwellnie.velox.sql.core.natives.exception;

public class ContextInitException extends RuntimeException {
    public ContextInitException(String message) {
        super(message);
    }

    public ContextInitException(String message, Throwable cause) {
        super(message, cause);
    }

    public ContextInitException(Throwable cause) {
        super(cause);
    }

    public ContextInitException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public ContextInitException() {
    }
}
