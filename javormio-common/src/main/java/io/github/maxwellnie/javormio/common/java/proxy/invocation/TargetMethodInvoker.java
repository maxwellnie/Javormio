package io.github.maxwellnie.javormio.common.java.proxy.invocation;

import io.github.maxwellnie.javormio.common.java.proxy.MethodInvocationException;

import java.lang.reflect.Method;

/**
 * Target method invoker
 */
public class TargetMethodInvoker implements MethodInvoker {
    final Method method;

    public TargetMethodInvoker(Method method) {
        this.method = method;
    }

    @Override
    public Object invoke(InvokerContext context) throws MethodInvocationException {
        try {
            return method.invoke(context.getTarget(), context.getArgs());
        } catch (Throwable e) {
            throw new MethodInvocationException(e);
        }
    }

    @Override
    public Object invoke(InvocationLine invocationLine, InvokerContext context) throws MethodInvocationException {
        return invoke(context);
    }
}
