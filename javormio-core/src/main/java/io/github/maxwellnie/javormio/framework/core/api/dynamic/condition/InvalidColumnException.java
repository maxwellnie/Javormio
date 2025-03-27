package io.github.maxwellnie.javormio.framework.core.api.dynamic.condition;

/**
 * @author Maxwell Nie
 */
public class InvalidColumnException extends RuntimeException{
    public InvalidColumnException() {
    }

    public InvalidColumnException(String message) {
        super(message);
    }
    public InvalidColumnException(String format, String columnName) {
        super(String.format(format, columnName));
    }
    public InvalidColumnException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidColumnException(Throwable cause) {
        super(cause);
    }

    public InvalidColumnException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
