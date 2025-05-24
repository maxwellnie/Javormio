package io.github.maxwellnie.javormio.flexible.sql.plugin;

import io.github.maxwellnie.javormio.core.Context;
import io.github.maxwellnie.javormio.core.translation.sql.SqlBuilder;
import io.github.maxwellnie.javormio.flexible.sql.plugin.expression.SqlExpressionSupport;
import io.github.maxwellnie.javormio.flexible.sql.plugin.function.SqlFunctionSupport;

import java.util.function.Supplier;

/**
 * @author Maxwell Nie
 */
public class FlexibleSqlContext<S extends SqlExpressionSupport> {
    protected Context context;
    private Supplier<SqlBuilder> sqlBuilderFactory;
    private S sqlExpressionSupport;
    private SqlFunctionSupport sqlFunctionSupport;

    public FlexibleSqlContext(Context context) {
        this.context = context;
        sqlBuilderFactory = SqlBuilder::new;
        sqlFunctionSupport = new SqlFunctionSupport(context);
    }

    public FlexibleSqlContext(Context context, Supplier<SqlBuilder> sqlBuilderFactory, S sqlExpressionSupport, SqlFunctionSupport sqlFunctionSupport) {
        this.context = context;
        this.sqlBuilderFactory = sqlBuilderFactory;
        this.sqlExpressionSupport = sqlExpressionSupport;
        this.sqlFunctionSupport = sqlFunctionSupport;
    }

    public Supplier<SqlBuilder> getSqlBuilderFactory() {
        return sqlBuilderFactory;
    }

    public void setSqlBuilderFactory(Supplier<SqlBuilder> sqlBuilderFactory) {
        this.sqlBuilderFactory = sqlBuilderFactory;
    }

    public SqlFunctionSupport getSqlFunctionSupport() {
        return sqlFunctionSupport;
    }

    public void setSqlFunctionSupport(SqlFunctionSupport sqlFunctionSupport) {
        this.sqlFunctionSupport = sqlFunctionSupport;
    }

    public SqlExpressionSupport getSqlExpressionSupport() {
        return sqlExpressionSupport;
    }

    public void setSqlExpressionSupport(SqlExpressionSupport sqlExpressionSupport) {
        this.sqlExpressionSupport = sqlExpressionSupport;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
