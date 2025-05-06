package io.github.maxwellnie.javormio.core.translation.sql;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Maxwell Nie
 */
public class SqlBuilder implements SqlFragment{
    List<Object> parameters = new LinkedList<>();
    StringBuilder sql = new StringBuilder();

    public SqlBuilder setParameters(List<Object> parameters) {
        this.parameters = parameters;
        return this;
    }
    public SqlBuilder append(String sqlFragment, Object... parameters){
        if (sqlFragment != null)
            sql.append(sqlFragment);
        if (parameters != null)
            Collections.addAll(this.parameters, parameters);
        return this;
    }
    public SqlBuilder append(SqlFragment sqlFragment){
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
    public Object[] getParameters() {
        return parameters.toArray();
    }

    @Override
    public String toSql() {
        return sql.toString();
    }
}
