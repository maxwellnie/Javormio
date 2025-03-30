package io.github.maxwellnie.javormio.common.java.reflect;

import io.github.maxwellnie.javormio.common.java.proxy.invocation.MethodInvoker;

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
