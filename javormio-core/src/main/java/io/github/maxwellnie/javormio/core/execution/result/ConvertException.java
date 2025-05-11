package io.github.maxwellnie.javormio.core.execution.result;

import io.github.maxwellnie.javormio.common.java.api.JavormioException;

/**
 * @author Maxwell Nie
 */
public class ConvertException extends JavormioException {
    public ConvertException() {
    }

    public ConvertException(String message) {
        super(message);
    }

    public ConvertException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConvertException(Throwable cause) {
        super(cause);
    }

    public ConvertException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
