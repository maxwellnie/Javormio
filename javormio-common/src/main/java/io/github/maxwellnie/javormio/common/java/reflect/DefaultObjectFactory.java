package io.github.maxwellnie.javormio.common.java.reflect;

import io.github.maxwellnie.javormio.common.java.proxy.MethodInvocationException;
import io.github.maxwellnie.javormio.common.java.proxy.invocation.MethodInvoker;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * @author Maxwell Nie
 */
public class DefaultObjectFactory<T> implements ObjectFactory<T> {
    Class<?> clazz;
    MethodInvoker constructor;

    public DefaultObjectFactory(Class<?> clazz) throws NoSuchMethodException {
        this.clazz = clazz;
        this.constructor = new MethodInvoker() {
            @Override
            public Object invoke() throws MethodInvocationException {
                Constructor<?> constructor;
                {
                    try {
                        constructor = clazz.getConstructor();
                    } catch (NoSuchMethodException e) {
                        throw new MethodInvocationException(e);
                    }
                }
                try {
                    return constructor.newInstance();
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                    throw new MethodInvocationException(e);
                }
            }
        };
    }

    public DefaultObjectFactory(Class<?> clazz, MethodInvoker constructor) {
        this.clazz = clazz;
        this.constructor = constructor;
    }

    @Override
    public T produce() throws ReflectiveOperationException {
        try {
            return (T) constructor.invoke();
        } catch (MethodInvocationException e) {
            throw new ReflectiveOperationException(e);
        }
    }
}
