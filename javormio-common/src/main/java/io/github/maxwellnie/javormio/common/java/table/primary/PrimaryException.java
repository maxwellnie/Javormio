package io.github.maxwellnie.javormio.common.java.table.primary;

import io.github.maxwellnie.javormio.common.java.table.TableException;

/**
 * @author Maxwell Nie
 */
public class PrimaryException extends TableException {
    public PrimaryException() {
    }

    public PrimaryException(String message) {
        super(message);
    }

    public PrimaryException(String message, Throwable cause) {
        super(message, cause);
    }

    public PrimaryException(Throwable cause) {
        super(cause);
    }

    public PrimaryException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
