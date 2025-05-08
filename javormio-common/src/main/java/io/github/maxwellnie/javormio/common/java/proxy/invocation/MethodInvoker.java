package io.github.maxwellnie.javormio.common.java.proxy.invocation;

import io.github.maxwellnie.javormio.common.java.proxy.MethodInvocationException;

/**
 * The Method invoker.
 *
 * @author Maxwell Nie
 */
public interface MethodInvoker<T, R> {
    /**
     * perform the method.
     *
     * @param context The invoker context: the target object and the arguments
     * @return Object
     * @throws MethodInvocationException
     */
    default R invoke(InvokerContext context) throws MethodInvocationException {
        throw new MethodInvocationException("The invoke(InvokerContext) is not supported.");
    }

    /**
     * perform the method.
     *
     * @param invocationLine The invocation line
     * @param context        The invoker context: the target object and the arguments
     * @return Object
     * @throws MethodInvocationException
     */
    default R invoke(InvocationLine invocationLine, InvokerContext context) throws MethodInvocationException {
        throw new MethodInvocationException("The invoke(InvocationLine, InvokerContext) is not supported.");
    }

    /**
     * perform the method.
     *
     * @param target The target object
     * @param args   The arguments
     * @return Object
     * @throws MethodInvocationException
     */
    default R invoke(T target, Object[] args) throws MethodInvocationException {
        throw new MethodInvocationException("The invoke(Object, LObject) is not supported.");
    }

    /**
     * perform the method.
     *
     * @param target The target object
     * @param args   The arguments
     * @return Object
     * @throws MethodInvocationException
     */
    default R invokeExactly(T target, Object... args) throws MethodInvocationException {
        throw new MethodInvocationException("The invoke(Object, LObject) is not supported.");
    }

    /**
     * perform the method.
     *
     * @param target The target object
     * @return Object
     * @throws MethodInvocationException
     */
    default R invoke(T target) throws MethodInvocationException {
        throw new MethodInvocationException("The invoke(Object) is not supported.");
    }

    /**
     * perform the method.
     *
     * @return Object
     * @throws MethodInvocationException
     */
    default R invoke() throws MethodInvocationException {
        throw new MethodInvocationException("The invoke() is not supported.");
    }

    /**
     * perform the method.
     *
     * @param args The arguments
     * @return Object
     * @throws MethodInvocationException
     */
    default R invoke(Object[] args) throws MethodInvocationException {
        throw new MethodInvocationException("The invoke(LObject) is not supported.");
    }
}
