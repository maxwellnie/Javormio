package io.github.maxwellnie.javormio.common.java.api;

/**
 * @author Maxwell Nie
 */
public class JavormioException extends RuntimeException{
    public JavormioException() {
    }

    public JavormioException(String message) {
        super(message);
    }

    public JavormioException(String message, Throwable cause) {
        super(message, cause);
    }

    public JavormioException(Throwable cause) {
        super(cause);
    }

    public JavormioException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
