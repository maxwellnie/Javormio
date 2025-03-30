package io.github.maxwellnie.javormio.common.java;

/**
 * @author Maxwell Nie
 */
public class NotMappedFieldException extends RuntimeException {
    public NotMappedFieldException() {
    }

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
}
