package io.github.maxwellnie.javormio.flexible.sql.plugin;

import io.github.maxwellnie.javormio.common.java.reflect.ObjectFactory;
import io.github.maxwellnie.javormio.flexible.sql.plugin.expression.SqlExpressionSupport;

/**
 * @author Maxwell Nie
 */
public class SqlContext {
    private final ObjectFactory<SqlBuilder> sqlBuilderFactory;
    private final SqlExpressionSupport sqlExpressionSupport;

    public SqlContext(ObjectFactory<SqlBuilder> sqlBuilderFactory, SqlExpressionSupport sqlExpressionSupport) {
        this.sqlBuilderFactory = sqlBuilderFactory;
        this.sqlExpressionSupport = sqlExpressionSupport;
    }

    public ObjectFactory<SqlBuilder> getSqlBuilderFactory() {
        return sqlBuilderFactory;
    }

    public SqlExpressionSupport getSqlExpressionSupport() {
        return sqlExpressionSupport;
    }
}
