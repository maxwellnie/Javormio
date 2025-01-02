package com.maxwellnie.velox.sql.core.natives.exception;

/**
 * @author Maxwell Nie
 */
public class ProxyExtendsException extends RuntimeException{
    public ProxyExtendsException() {
    }

    public ProxyExtendsException(String message) {
        super(message);
    }

    public ProxyExtendsException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProxyExtendsException(Throwable cause) {
        super(cause);
    }

    public ProxyExtendsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
