package io.github.maxwellnie.javormio.core.translation.table;

import io.github.maxwellnie.javormio.common.java.api.JavormioException;

/**
 * @author Maxwell Nie
 */
public class TableException extends JavormioException {
    public TableException() {
    }

    public TableException(String message) {
        super(message);
    }

    public TableException(String message, Throwable cause) {
        super(message, cause);
    }

    public TableException(Throwable cause) {
        super(cause);
    }

    public TableException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
