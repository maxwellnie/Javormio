package io.github.maxwellnie.javormio.flexible.sql.plugin;

import io.github.maxwellnie.javormio.common.java.reflect.ObjectFactory;
import io.github.maxwellnie.javormio.flexible.sql.plugin.expression.SqlExpressionSupport;

/**
 * @author Maxwell Nie
 */
public class SqlContext {
    private ObjectFactory<SqlBuilder> sqlBuilderFactory;
    private SqlExpressionSupport sqlExpressionSupport;

    public ObjectFactory<SqlBuilder> getSqlBuilderFactory() {
        return sqlBuilderFactory;
    }

    public SqlExpressionSupport getSqlExpressionSupport() {
        return sqlExpressionSupport;
    }
}
