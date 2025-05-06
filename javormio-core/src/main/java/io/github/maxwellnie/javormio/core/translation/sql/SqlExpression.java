package io.github.maxwellnie.javormio.core.translation.sql;

/**
 * @author Maxwell Nie
 */
public class SqlExpression implements SqlFragment{
    protected String sql;
    protected Object[] parameters;

    public SqlExpression(String sql, Object[] parameters) {
        this.sql = sql;
        this.parameters = parameters;
    }

    @Override
    public Object[] getParameters() {
        return parameters;
    }

    @Override
    public String toSql() {
        return sql;
    }
}
