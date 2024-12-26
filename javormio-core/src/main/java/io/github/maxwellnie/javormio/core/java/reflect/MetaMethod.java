package io.github.maxwellnie.javormio.core.java.reflect;

import io.github.maxwellnie.javormio.core.java.proxy.invocation.MethodInvoker;

/**
 * @author Maxwell Nie
 */
public interface MetaMethod {
    MethodInvoker getInvoker();

    String getName();

    Class<?> getReturnType();

    Class<?>[] getParameterTypes();

    Class<?> getDeclaringClass();

    int getModifiers();
}
