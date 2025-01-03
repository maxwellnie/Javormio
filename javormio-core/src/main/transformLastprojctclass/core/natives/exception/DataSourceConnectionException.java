package com.maxwellnie.velox.sql.core.natives.exception;

/**
 * @author Maxwell Nie
 */
public class DataSourceConnectionException extends RuntimeException {
    public DataSourceConnectionException() {
    }

    public DataSourceConnectionException(String message) {
        super(message);
    }

    public DataSourceConnectionException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataSourceConnectionException(Throwable cause) {
        super(cause);
    }

    public DataSourceConnectionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
