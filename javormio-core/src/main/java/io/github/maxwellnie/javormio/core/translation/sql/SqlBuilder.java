package io.github.maxwellnie.javormio.core.translation.sql;

import io.github.maxwellnie.javormio.core.translation.SqlParameter;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Maxwell Nie
 */
public class SqlBuilder implements SqlFragment {
    List<SqlParameter> parameters = new LinkedList<>();
    StringBuilder sql = new StringBuilder();

    public SqlBuilder append(String sqlFragment, SqlParameter... parameters) {
        if (sqlFragment != null)
            sql.append(sqlFragment);
        if (parameters != null)
            Collections.addAll(this.parameters, parameters);
        return this;
    }

    public SqlBuilder append(SqlFragment sqlFragment) {
        if (sqlFragment == null)
            return this;
        if (sqlFragment.toSql() == null)
            return this;
        sql.append(sqlFragment.toSql());
        if (sqlFragment.getParameters() == null)
            return this;
        Collections.addAll(this.parameters, sqlFragment.getParameters());
        return this;
    }

    public void setSql(String sql) {
        this.sql = new StringBuilder(sql);
    }

    @Override
    public SqlParameter[] getParameters() {
        return parameters.toArray(new SqlParameter[0]);
    }

    public SqlBuilder setParameters(List<SqlParameter> parameters) {
        this.parameters = parameters;
        return this;
    }

    @Override
    public String toSql() {
        return sql.toString();
    }
}
