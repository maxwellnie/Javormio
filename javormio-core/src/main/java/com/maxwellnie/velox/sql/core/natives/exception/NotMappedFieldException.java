package com.maxwellnie.velox.sql.core.natives.exception;

/**
 * @author Maxwell Nie
 */
public class NotMappedFieldException extends RuntimeException {
    public NotMappedFieldException(String message) {
        super(message);
    }

    public NotMappedFieldException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotMappedFieldException(Throwable cause) {
        super(cause);
    }

    public NotMappedFieldException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public NotMappedFieldException() {
    }
}
