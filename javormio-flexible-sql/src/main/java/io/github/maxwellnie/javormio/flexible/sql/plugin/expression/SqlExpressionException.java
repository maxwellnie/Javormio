package io.github.maxwellnie.javormio.flexible.sql.plugin.expression;

import io.github.maxwellnie.javormio.common.java.api.JavormioException;

/**
 * @author Maxwell Nie
 */
public class SqlExpressionException extends JavormioException {
    public SqlExpressionException() {
    }

    public SqlExpressionException(String message) {
        super(message);
    }

    public SqlExpressionException(String message, Throwable cause) {
        super(message, cause);
    }

    public SqlExpressionException(Throwable cause) {
        super(cause);
    }

    public SqlExpressionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
