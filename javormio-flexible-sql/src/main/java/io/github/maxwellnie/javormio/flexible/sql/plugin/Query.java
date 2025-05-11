package io.github.maxwellnie.javormio.flexible.sql.plugin;

import io.github.maxwellnie.javormio.core.execution.ExecutableSql;
import io.github.maxwellnie.javormio.core.execution.ExecutorContext;
import io.github.maxwellnie.javormio.core.execution.QuerySqlExecutor;
import io.github.maxwellnie.javormio.core.execution.StatementWrapper;
import io.github.maxwellnie.javormio.core.translation.method.DaoMethodFeature;
import io.github.maxwellnie.javormio.flexible.sql.plugin.result.ObjectExecuteResult;
import io.github.maxwellnie.javormio.flexible.sql.plugin.result.ResultSetExecuteResult;

import java.sql.ResultSet;
import java.util.LinkedHashMap;

/**
 * @author Maxwell Nie
 */
public class Query<T> {
    protected QueryBuilder<T> queryBuilder;
    protected ExecutableSql executableSql;

    public Query(QueryBuilder<T> queryBuilder, ExecutableSql executableSql) {
        this.queryBuilder = queryBuilder;
        this.executableSql = executableSql;
    }

    public ResultSetExecuteResult selectToResultSet(){
        ExecutorContext<ResultSet> executorContext = new ExecutorContext<>(queryBuilder.context.getConnectionResource(), executableSql, new DaoMethodFeature<>(null, null, false), queryBuilder.context.getResultSetConvertor(), null, null, false);
        StatementWrapper statementWrapper = queryBuilder.context.getSqlExecutor(QuerySqlExecutor.class).run(executorContext);
        ResultSet resultSet = (ResultSet) statementWrapper.getResult();
        return new ResultSetExecuteResult(resultSet, resultSet, queryBuilder.allColumns, new LinkedHashMap<>(), queryBuilder.columnAliasMap, statementWrapper, executorContext);
    }
    public ObjectExecuteResult<T> selectToEntity() {
        ExecutorContext<ResultSet> executorContext = new ExecutorContext<>(queryBuilder.context.getConnectionResource(), executableSql, new DaoMethodFeature<>(null, null, false), queryBuilder.context.getResultSetConvertor(), null,  null, false);
        StatementWrapper statementWrapper = queryBuilder.context.getSqlExecutor(QuerySqlExecutor.class).run(executorContext);
        return new ObjectExecuteResult<>(queryBuilder.table, (ResultSet) statementWrapper.getResult(), queryBuilder.allColumns, new LinkedHashMap<>(), queryBuilder.columnAliasMap, statementWrapper, executorContext);
    }

    public QueryBuilder<T> getQuery() {
        return queryBuilder;
    }

    public ExecutableSql getExecutableSql() {
        return executableSql;
    }
}
