package io.github.maxwellnie.javormio.core.database.sql.method;

/**
 * @author Maxwell Nie
 */
public interface SqlMethod {
    Object invokeExactly(Object... args);
    Object invoke(Object[] args);
}
