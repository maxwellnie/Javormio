package io.github.maxwellnie.javormio.core.execution;

import io.github.maxwellnie.javormio.common.java.api.JavormioException;

/**
 * @author Maxwell Nie
 */
public class ExecutionException extends JavormioException {
    public ExecutionException() {
    }

    public ExecutionException(String message) {
        super(message);
    }

    public ExecutionException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExecutionException(Throwable cause) {
        super(cause);
    }
    public ExecutionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
