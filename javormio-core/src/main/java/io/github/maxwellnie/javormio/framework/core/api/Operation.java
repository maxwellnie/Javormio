package io.github.maxwellnie.javormio.framework.core.api;

/**
 * @author Maxwell Nie
 */
public interface Operation<T> {
    T getEntity(String sql, Object... args);
}
