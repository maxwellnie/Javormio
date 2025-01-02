package com.maxwellnie.velox.sql.core.natives.exception;

/**
 * @author Maxwell Nie
 */
public class DaoRegisterException extends RuntimeException{
    public DaoRegisterException() {
    }

    public DaoRegisterException(String message) {
        super(message);
    }

    public DaoRegisterException(String message, Throwable cause) {
        super(message, cause);
    }

    public DaoRegisterException(Throwable cause) {
        super(cause);
    }

    public DaoRegisterException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
