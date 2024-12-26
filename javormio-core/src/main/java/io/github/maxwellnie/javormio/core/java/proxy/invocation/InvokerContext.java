package io.github.maxwellnie.javormio.core.java.proxy.invocation;

/**
 * The context of invocation: the target object and the arguments
 *
 * @author Maxwell Nie
 */
public class InvokerContext {
    final Object target;
    final Object[] args;

    public InvokerContext(Object target, Object[] args) {
        this.target = target;
        this.args = args;
    }

    public Object getTarget() {
        return target;
    }

    public Object[] getArgs() {
        return args;
    }
}
