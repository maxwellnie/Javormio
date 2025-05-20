package io.github.maxwellnie.javormio.core.api.dynamic;

import io.github.maxwellnie.javormio.common.java.reflect.method.SerializableFunction;

/**
 * @author Maxwell Nie
 */
@FunctionalInterface
public interface SqlFunc<T, R> {
    String modify(SerializableFunction<T, R> getter);
}
