package io.github.maxwellnie.javormio.core.translation.sql;

import io.github.maxwellnie.javormio.core.translation.SqlParameter;

/**
 * @author Maxwell Nie
 */
public interface SqlFragment {
    SqlParameter[] getParameters();
    String toSql();
}
