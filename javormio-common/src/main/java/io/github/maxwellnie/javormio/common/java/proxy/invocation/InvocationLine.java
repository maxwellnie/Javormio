package io.github.maxwellnie.javormio.common.java.proxy.invocation;

import io.github.maxwellnie.javormio.common.java.proxy.MethodInvocationException;

import java.lang.reflect.Method;

/**
 * Line of invocation
 *
 * @author Maxwell Nie
 */
public class InvocationLine {
    final MethodInvoker targetMethod;
    final InvocationLine nextInvocation;
    final Method method;

    public InvocationLine(MethodInvoker targetMethod, InvocationLine nextInvocation, Method method) {
        this.targetMethod = targetMethod;
        this.nextInvocation = nextInvocation;
        this.method = method;
    }

    public MethodInvoker getTargetMethod() {
        return targetMethod;
    }

    public Method getMethod() {
        return method;
    }

    /**
     * Proceed to next invocation line
     *
     * @param context
     * @return Object
     */
    public Object proceed(InvokerContext context) throws MethodInvocationException {
        return this.targetMethod.invoke(nextInvocation, context);
    }
}
