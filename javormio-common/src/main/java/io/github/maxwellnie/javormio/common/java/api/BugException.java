package io.github.maxwellnie.javormio.common.java.api;

/**
 * @author Maxwell Nie
 */
public class BugException extends JavormioException{
    public BugException() {
    }

    public BugException(String message) {
        super(message);
    }

    public BugException(String message, Throwable cause) {
        super(message, cause);
    }

    public BugException(Throwable cause) {
        super(cause);
    }

    public BugException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
