package io.github.maxwellnie.javormio.common.java.proxy;

import io.github.maxwellnie.javormio.common.java.proxy.invocation.InvocationLine;
import io.github.maxwellnie.javormio.common.java.proxy.invocation.InvokerContext;

/**
 * Executor of the method
 *
 * @author Maxwell Nie
 */
public interface MethodExecutor {
    /**
     * Execute the function
     *
     * @param invocationLine the invocation line
     * @param context        the invocation context: the target object and arguments
     * @return Object
     * @throws MethodInvocationException
     */
    Object execute(InvocationLine invocationLine, InvokerContext context) throws MethodInvocationException;
}
