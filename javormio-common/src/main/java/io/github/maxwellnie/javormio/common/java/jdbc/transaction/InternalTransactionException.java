package io.github.maxwellnie.javormio.common.java.jdbc.transaction;

/**
 * @author Maxwell Nie
 */
public class InternalTransactionException extends Exception {
    public InternalTransactionException() {
    }

    public InternalTransactionException(String message) {
        super(message);
    }

    public InternalTransactionException(String message, Throwable cause) {
        super(message, cause);
    }

    public InternalTransactionException(Throwable cause) {
        super(cause);
    }

    public InternalTransactionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
