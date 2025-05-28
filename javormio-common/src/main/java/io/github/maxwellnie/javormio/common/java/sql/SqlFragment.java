package io.github.maxwellnie.javormio.common.java.sql;


/**
 * @author Maxwell Nie
 */
public interface SqlFragment {
    SqlParameter[] getParameters();

    String toSql();
    boolean isEmpty();
}
