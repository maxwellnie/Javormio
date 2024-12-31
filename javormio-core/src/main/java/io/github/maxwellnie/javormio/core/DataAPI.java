package io.github.maxwellnie.javormio.core;

import java.util.HashMap;

/**
 * @author Maxwell Nie
 */
public interface DataAPI<T> {
    T getEntity(String sql, Object ...args);
}
