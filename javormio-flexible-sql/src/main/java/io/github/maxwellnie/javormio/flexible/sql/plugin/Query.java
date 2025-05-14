package io.github.maxwellnie.javormio.flexible.sql.plugin;

import io.github.maxwellnie.javormio.core.execution.ExecutableSql;
import io.github.maxwellnie.javormio.core.execution.ExecutorContext;
import io.github.maxwellnie.javormio.core.execution.QuerySqlExecutor;
import io.github.maxwellnie.javormio.core.execution.StatementWrapper;
import io.github.maxwellnie.javormio.core.translation.method.DaoMethodFeature;
import io.github.maxwellnie.javormio.flexible.sql.plugin.result.ExecutionResults;
import io.github.maxwellnie.javormio.flexible.sql.plugin.result.stream.EntityResultStream;
import io.github.maxwellnie.javormio.flexible.sql.plugin.result.stream.ExecutionResultStream;
import io.github.maxwellnie.javormio.flexible.sql.plugin.result.stream.ResultStream;

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

    public ResultStream<ExecutionResults> selectToResultStream() {
        ExecutorContext<ResultSet> executorContext = new ExecutorContext<>(queryBuilder.context.getConnectionResource(), executableSql, new DaoMethodFeature<>(null, null, false), queryBuilder.context.getResultSetConvertor(), null, null, false);
        StatementWrapper statementWrapper = queryBuilder.context.getSqlExecutor(QuerySqlExecutor.class).run(executorContext);
        ResultSet resultSet = (ResultSet) statementWrapper.getResult();
        ExecutionResults executionResults = new ExecutionResults(resultSet, new LinkedHashMap<>(), queryBuilder.allColumns, queryBuilder.columnAliasMap, new AutoCloseable[]{statementWrapper});
        return new ExecutionResultStream(executionResults);
    }

    public ResultStream<T> selectToEntityStream() {
        return new EntityResultStream<>(selectToResultStream(), queryBuilder.table.instanceInvoker);
    }

    public QueryBuilder<T> getQueryBuilder() {
        return queryBuilder;
    }

    public ExecutableSql getExecutableSql() {
        return executableSql;
    }
}
