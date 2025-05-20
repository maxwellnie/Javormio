package io.github.maxwellnie.javormio.core.translation.table.primary;

import io.github.maxwellnie.javormio.core.translation.table.TableException;

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
