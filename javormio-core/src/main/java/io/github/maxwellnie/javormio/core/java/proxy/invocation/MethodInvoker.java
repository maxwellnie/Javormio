package io.github.maxwellnie.javormio.core.java.proxy.invocation;

import io.github.maxwellnie.javormio.core.java.proxy.MethodInvocationException;

/**
 * The Method invoker.
 *
 * @author Maxwell Nie
 */
public interface MethodInvoker {
    /**
     * execute the method.
     *
     * @param context The invoker context: the target object and the arguments
     * @return Object
     * @throws MethodInvocationException
     */
    default Object invoke(InvokerContext context) throws MethodInvocationException {
        throw new MethodInvocationException("The invoke(InvokerContext) is not supported.");
    }

    /**
     * execute the method.
     *
     * @param invocationLine The invocation line
     * @param context        The invoker context: the target object and the arguments
     * @return Object
     * @throws MethodInvocationException
     */
    default Object invoke(InvocationLine invocationLine, InvokerContext context) throws MethodInvocationException {
        throw new MethodInvocationException("The invoke(InvocationLine, InvokerContext) is not supported.");
    }

    /**
     * execute the method.
     *
     * @param target The target object
     * @param args   The arguments
     * @return Object
     * @throws MethodInvocationException
     */
    default Object invoke(Object target, Object[] args) throws MethodInvocationException {
        throw new MethodInvocationException("The invoke(Object, LObject) is not supported.");
    }

    /**
     * execute the method.
     *
     * @param target The target object
     * @return Object
     * @throws MethodInvocationException
     */
    default Object invoke(Object target) throws MethodInvocationException {
        throw new MethodInvocationException("The invoke(Object) is not supported.");
    }
}
