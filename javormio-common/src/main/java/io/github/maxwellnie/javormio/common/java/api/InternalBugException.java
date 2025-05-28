package io.github.maxwellnie.javormio.common.java.api;

/**
 * 框架内部错误
 * <p>对框架与扩展编程造成其内部出现异常</p>
 * @author Maxwell Nie
 */
public class InternalBugException extends JavormioException{
    public InternalBugException() {
    }

    public InternalBugException(String message) {
        super(message);
    }

    public InternalBugException(String message, Throwable cause) {
        super(message, cause);
    }

    public InternalBugException(Throwable cause) {
        super(cause);
    }

    public InternalBugException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
