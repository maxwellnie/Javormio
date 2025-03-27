package io.github.maxwellnie.javormio.framework.common.java.proxy;

/**
 * All exceptions related to method invocation will be thrown in this class
 *
 * @author Maxwell Nie
 */
public class MethodInvocationException extends Exception {
    public MethodInvocationException() {
    }

    public MethodInvocationException(String message) {
        super(message);
    }

    public MethodInvocationException(String message, Throwable cause) {
        super(message, cause);
    }

    public MethodInvocationException(Throwable cause) {
        super(cause);
    }

    public MethodInvocationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
