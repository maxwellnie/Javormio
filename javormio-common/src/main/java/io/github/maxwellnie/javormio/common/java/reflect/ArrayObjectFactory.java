package io.github.maxwellnie.javormio.common.java.reflect;

import java.lang.reflect.Array;

/**
 * @author Maxwell Nie
 */
public class ArrayObjectFactory<T> implements ObjectFactory<T[]> {
    private final Class<T> clazz;
    private final int length;

    public ArrayObjectFactory(Class<T> clazz, int length) {
        this.clazz = clazz;
        this.length = length;
    }

    @Override
    public T[] produce() throws ReflectiveOperationException {
        return (T[]) Array.newInstance(clazz, length);
    }
}
