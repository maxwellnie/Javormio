package io.github.maxwellnie.javormio.common.java.reflect;

import io.github.maxwellnie.javormio.common.java.api.Factory;

/**
 * @author Maxwell Nie
 */
public interface ObjectFactory<T> extends Factory<T> {
    default T produce() throws ReflectiveOperationException {
        throw new ReflectiveOperationException("It isn't supported.");
    }

    default <T> T produce(Class<?> clazz) throws ReflectiveOperationException {
        throw new ReflectiveOperationException("It isn't supported.");
    }

    default <T> T produce(Class<?> clazz, Object[] args) throws ReflectiveOperationException {
        throw new ReflectiveOperationException("It isn't supported.");
    }

    default <T> T produce(Class<?> clazz, Class<?>[] parameters, Object[] args) throws ReflectiveOperationException {
        throw new ReflectiveOperationException("It isn't supported.");
    }

    default <T> T produce(Object object) throws ReflectiveOperationException {
        throw new ReflectiveOperationException("It isn't supported.");
    }
}
