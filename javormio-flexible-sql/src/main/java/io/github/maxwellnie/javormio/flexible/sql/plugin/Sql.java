package io.github.maxwellnie.javormio.flexible.sql.plugin;

import io.github.maxwellnie.javormio.core.translation.SqlParameter;
import io.github.maxwellnie.javormio.core.translation.sql.SqlFragment;

/**
 * @author Maxwell Nie
 */
public class Sql implements SqlFragment {
    protected String sql;
    protected SqlParameter[] parameters;


    public Sql(String sql, SqlParameter[] parameters) {
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
