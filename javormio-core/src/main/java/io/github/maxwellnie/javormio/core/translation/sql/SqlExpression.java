package io.github.maxwellnie.javormio.core.translation.sql;

import io.github.maxwellnie.javormio.core.translation.SqlParameter;

/**
 * @author Maxwell Nie
 */
public class SqlExpression implements SqlFragment{
    protected String sql;
    protected SqlParameter[] parameters;


    public SqlExpression(String sql, SqlParameter[] parameters) {
        this.sql = sql;
        this.parameters = parameters;
    }

    @Override
    public SqlParameter[] getParameters() {
        return parameters;
    }

    @Override
    public String toSql() {
        return sql;
    }
}
