package io.github.maxwellnie.javormio.framework.common.java.reflect;

import io.github.maxwellnie.javormio.framework.common.java.proxy.invocation.MethodInvoker;

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
