package io.github.maxwellnie.javormio.common.java.reflect.method;

import java.io.Serializable;
import java.util.function.Function;

/**
 * @author Maxwell Nie
 */
public interface SerializableFunction<T, R> extends Function<T, R>, Serializable {
}
