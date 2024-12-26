package io.github.maxwellnie.javormio.core.java.reflect;

import io.github.maxwellnie.javormio.core.java.api.Factory;

/**
 * @author Maxwell Nie
 */
public interface ObjectFactory<T> extends Factory<T> {
    default T produce() throws ReflectionException {
        throw new ReflectionException("It isn't supported.");
    }

    default <T> T produce(Class<?> clazz) throws ReflectionException {
        throw new ReflectionException("It isn't supported.");
    }

    default <T> T produce(Class<?> clazz, Object[] args) throws ReflectionException {
        throw new ReflectionException("It isn't supported.");
    }

    default <T> T produce(Class<?> clazz, Class<?>[] parameters, Object[] args) throws ReflectionException {
        throw new ReflectionException("It isn't supported.");
    }

    default <T> T produce(Object object) throws ReflectionException {
        throw new ReflectionException("It isn't supported.");
    }
}
