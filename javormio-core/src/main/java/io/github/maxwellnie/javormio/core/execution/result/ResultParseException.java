package io.github.maxwellnie.javormio.core.execution.result;

import io.github.maxwellnie.javormio.common.java.api.JavormioException;

/**
 * @author Maxwell Nie
 */
public class ResultParseException extends JavormioException {

    public ResultParseException(String message) {
        super(message);
    }

    public ResultParseException(String message, Throwable cause) {
        super(message, cause);
    }

    public ResultParseException(Throwable cause) {
        super(cause);
    }

}
