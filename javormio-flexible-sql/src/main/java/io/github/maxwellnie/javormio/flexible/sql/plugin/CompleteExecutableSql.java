package io.github.maxwellnie.javormio.flexible.sql.plugin;

import io.github.maxwellnie.javormio.core.execution.ExecutableSql;
import io.github.maxwellnie.javormio.core.execution.ExecutorContext;
import io.github.maxwellnie.javormio.core.execution.QuerySqlExecutor;
import io.github.maxwellnie.javormio.core.execution.StatementWrapper;
import io.github.maxwellnie.javormio.core.translation.method.DaoMethodFeature;
import io.github.maxwellnie.javormio.flexible.sql.plugin.result.ObjectExecuteResult;
import io.github.maxwellnie.javormio.flexible.sql.plugin.result.ResultSetExecuteResult;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;

/**
 * @author Maxwell Nie
 */
public class CompleteExecutableSql<T> {
    protected Query<T> query;
    protected ExecutableSql executableSql;

    public CompleteExecutableSql(Query<T> query, ExecutableSql executableSql) {
        this.query = query;
        this.executableSql = executableSql;
    }

    public ResultSetExecuteResult selectToResultSet(){
        ExecutorContext<ResultSet> executorContext = new ExecutorContext<>(query.context.getConnectionResource(), executableSql, new DaoMethodFeature<>(null, null, false), query.context.getResultSetConvertor(), null, null, false);
        StatementWrapper statementWrapper = query.context.getSqlExecutor(QuerySqlExecutor.class).run(executorContext);
        ResultSet resultSet = (ResultSet) statementWrapper.getResult();
        return new ResultSetExecuteResult(resultSet, resultSet, query.allColumns, new LinkedHashMap<>(), query.columnAliasMap, statementWrapper, executorContext);
    }
    public ObjectExecuteResult<T> selectToEntity() {
        ExecutorContext<ResultSet> executorContext = new ExecutorContext<>(query.context.getConnectionResource(), executableSql, new DaoMethodFeature<>(null, null, false), query.context.getResultSetConvertor(), null,  null, false);
        StatementWrapper statementWrapper = query.context.getSqlExecutor(QuerySqlExecutor.class).run(executorContext);
        return new ObjectExecuteResult<>(query.table, (ResultSet) statementWrapper.getResult(), query.allColumns, new LinkedHashMap<>(), query.columnAliasMap, statementWrapper, executorContext);
    }

    public Query<T> getQuery() {
        return query;
    }

    public ExecutableSql getExecutableSql() {
        return executableSql;
    }
}
