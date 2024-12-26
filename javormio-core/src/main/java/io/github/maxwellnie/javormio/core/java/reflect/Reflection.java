package io.github.maxwellnie.javormio.core.java.reflect;

import io.github.maxwellnie.javormio.core.java.proxy.invocation.MethodInvoker;
import io.github.maxwellnie.javormio.core.java.reflect.property.MetaField;

import java.lang.reflect.Method;

/**
 * @author Maxwell Nie
 */
public interface Reflection<T> {
    MethodInvoker getInvoker(String name, Class<?>[] parameterTypes);

    MetaField getMetaField(String name);

    Method getMethod(String name, Class<?>[] parameterTypes, boolean deepSearch);

    ObjectFactory<T> getObjectFactory();

    Class<?> getDeclaringClass();
}
