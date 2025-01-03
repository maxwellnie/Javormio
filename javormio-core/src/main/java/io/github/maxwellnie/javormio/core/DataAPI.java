package io.github.maxwellnie.javormio.core;

/**
 * @author Maxwell Nie
 */
public interface DataAPI<T> {
    T getEntity(String sql, Object... args);
}
