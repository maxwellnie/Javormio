package io.github.maxwellnie.javormio.core.java.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * @author Maxwell Nie
 */
public class DefaultObjectFactory<T> implements ObjectFactory<T> {
    Class<?> clazz;
    Constructor<?> constructor;

    public DefaultObjectFactory(Class<?> clazz) throws NoSuchMethodException {
        this.clazz = clazz;
        this.constructor = clazz.getConstructor();
    }

    @Override
    public T produce() throws ReflectionException {
        try {
            return (T) constructor.newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new ReflectionException(e);
        }
    }
}
