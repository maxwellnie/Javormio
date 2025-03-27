package io.github.maxwellnie.javormio.framework.core.api.dynamic;

import java.io.Serializable;
import java.util.function.Function;

/**
 * @author Maxwell Nie
 */
@FunctionalInterface
public interface SerializableFunction<T, R> extends Function<T, R>, Serializable {
}
