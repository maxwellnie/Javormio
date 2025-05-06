package io.github.maxwellnie.javormio.core.translation.sql;

/**
 * @author Maxwell Nie
 */
public interface SqlFragment {
    Object[] getParameters();
    String toSql();
}
