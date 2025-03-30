package io.github.maxwellnie.javormio.common.java.proxy.invocation;

import io.github.maxwellnie.javormio.common.java.proxy.MethodExecutor;
import io.github.maxwellnie.javormio.common.java.proxy.MethodInvocationException;

/**
 * Proxy method invoker that wraps the method executor
 */
public class MethodExecutorInvoker implements MethodInvoker {
    final MethodExecutor methodExecutor;

    public MethodExecutorInvoker(MethodExecutor methodExecutor) {
        this.methodExecutor = methodExecutor;
    }

    @Override
    public Object invoke(InvokerContext context) throws MethodInvocationException {
        throw new MethodInvocationException("MethodExecutorInvoker.invoke() should not be called");
    }

    @Override
    public Object invoke(InvocationLine invocationLine, InvokerContext context) throws MethodInvocationException {
        return methodExecutor.execute(invocationLine, context);
    }
}
