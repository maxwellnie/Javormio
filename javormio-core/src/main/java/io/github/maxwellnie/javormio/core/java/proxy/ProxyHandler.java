package io.github.maxwellnie.javormio.core.java.proxy;

import io.github.maxwellnie.javormio.core.java.proxy.invocation.InvokerContext;
import io.github.maxwellnie.javormio.core.java.reflect.MetaMethod;

/**
 * The handler of the proxy's method.
 * @author Maxwell Nie
 */
public interface ProxyHandler {
    /**
     * Trigger the method.
     * @param method The method.
     * @param context The context of the method.
     * @return The result of the method.
     * @throws MethodInvocationException The exception of the method.
     */
    Object trigger(MetaMethod method, InvokerContext context) throws MethodInvocationException;
}
