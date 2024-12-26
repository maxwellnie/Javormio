package io.github.maxwellnie.javormio.core.database.table.primary;

/**
 * @author Maxwell Nie
 */
public interface KeyGenerator {
    void beforeInsert(Object[] params);

    void afterInsert(Object[] params);
}
