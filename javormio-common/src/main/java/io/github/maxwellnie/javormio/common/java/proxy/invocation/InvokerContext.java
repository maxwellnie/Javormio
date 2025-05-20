package io.github.maxwellnie.javormio.common.java.proxy.invocation;

/**
 * The context of invocation: the target object and the arguments
 *
 * @author Maxwell Nie
 */
public class InvokerContext<T> {
    final T target;
    final Object[] args;

    public InvokerContext(T target, Object[] args) {
        this.target = target;
        this.args = args;
    }

    public T getTarget() {
        return target;
    }

    public Object[] getArgs() {
        return args;
    }
}
