package io.github.maxwellnie.javormio.flexible.sql.plugin.execution.query;

import io.github.maxwellnie.javormio.common.java.type.TypeHandler;
import io.github.maxwellnie.javormio.core.execution.executor.SingleSqlExecutor;
import io.github.maxwellnie.javormio.core.execution.executor.SqlExecutor;
import io.github.maxwellnie.javormio.core.execution.executor.parameter.ExecutableSql;
import io.github.maxwellnie.javormio.core.execution.executor.parameter.ExecutorParameters;
import io.github.maxwellnie.javormio.core.execution.result.ResultSetConvertor;
import io.github.maxwellnie.javormio.core.execution.result.ValueResultSetConvertor;
import io.github.maxwellnie.javormio.core.execution.statement.PreparedStatementHelper;
import io.github.maxwellnie.javormio.core.translation.SqlType;
import io.github.maxwellnie.javormio.core.translation.table.column.ColumnInfo;
import io.github.maxwellnie.javormio.core.translation.table.column.ColumnType;
import io.github.maxwellnie.javormio.core.translation.table.primary.PrimaryInfo;
import io.github.maxwellnie.javormio.core.translation.sql.SqlBuilder;
import io.github.maxwellnie.javormio.flexible.sql.plugin.execution.result.convertor.ResultContext;
import io.github.maxwellnie.javormio.flexible.sql.plugin.execution.result.stream.EntityResultStream;
import io.github.maxwellnie.javormio.flexible.sql.plugin.execution.result.stream.ExecutionResultStream;
import io.github.maxwellnie.javormio.flexible.sql.plugin.execution.result.stream.ResultStream;
import io.github.maxwellnie.javormio.flexible.sql.plugin.function.SqlFunctionSupport;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * 查询对象
 *
 * @author Maxwell Nie
 */
public class Query<T> {
    /**
     * 查询构建器
     */
    protected QueryBuilder<T> queryBuilder;
    /**
     * 可执行SQL
     */
    protected ExecutableSql executableSql;

    public Query(QueryBuilder<T> queryBuilder, ExecutableSql executableSql) {
        this.queryBuilder = queryBuilder;
        this.executableSql = executableSql;
    }

    /**
     * 使用执行结果集流处理来处理结果
     *
     * @return ResultStream<ResultContext> 结果流
     */
    @SuppressWarnings("unchecked")
    public ResultStream<ResultContext> selectToResultStream() {
        ExecutorParameters executorParameters = new ExecutorParameters(queryBuilder.flexibleSqlContext.getContext().getConnection(), executableSql, null, new PreparedStatementHelper(), "");
        SqlExecutor sqlExecutor = queryBuilder.flexibleSqlContext.getContext().getSqlExecutor(SingleSqlExecutor.class);
        executableSql = queryBuilder.toExecutableSql();
        ResultContext resultContext = new ResultContext(new LinkedHashMap<>(), null, queryBuilder.allColumns, queryBuilder.columnAliasMap, sqlExecutor, executorParameters);
        return new ExecutionResultStream(resultContext);
    }

    /**
     * 使用实体对象结果流来处理结果
     *
     * @return ResultStream<T> 结果流
     */
    public ResultStream<T> selectToEntityStream() {
        return new EntityResultStream<>(selectToResultStream(), queryBuilder.table.instanceInvoker);
    }

    /**
     * 使用结果转换器来处理结果
     *
     * @param convertor 结果转换器
     * @return R 结果
     */
    @SuppressWarnings("unchecked")
    public <R> R selectToEntities(ResultSetConvertor<R> convertor) {
        ExecutorParameters executorParameters = new ExecutorParameters(queryBuilder.flexibleSqlContext.getContext().getConnection(), executableSql, convertor, new PreparedStatementHelper(), "");
        SqlExecutor sqlExecutor = queryBuilder.flexibleSqlContext.getContext().getSqlExecutor(SingleSqlExecutor.class);
        return (R) sqlExecutor.query(executorParameters);
    }

    /**
     * 获取结果数量
     *
     * @return long 数量
     */
    public long count() {
        SqlFunctionSupport sqlFunctionSupport = queryBuilder.flexibleSqlContext.getSqlFunctionSupport();
        SqlBuilder selectColumnSql = queryBuilder.selectColumnSql;
        SqlBuilder fromToOnSql = queryBuilder.fromToOnSql;
        SqlBuilder whereToEndSql = queryBuilder.whereToEndSql;
        StringBuilder sqlBuilder = new StringBuilder();
        selectColumnSql.append("SELECT ");
        List<PrimaryInfo> primaryInfos = new LinkedList<>();
        for (ColumnInfo columnInfo : queryBuilder.allColumns) {
            if ((columnInfo.getColumnType() & ColumnType.PRIMARY) != 0) {
                primaryInfos.add((PrimaryInfo) columnInfo);
            }
        }
        if (!primaryInfos.isEmpty()) {
            selectColumnSql.append(sqlFunctionSupport.count(Long.class, primaryInfos).toSqlFragment());
        } else
            selectColumnSql.append(sqlFunctionSupport.count(Long.class, "*").toSqlFragment());
        sqlBuilder.append(selectColumnSql.toSql());
        sqlBuilder.append(fromToOnSql.toSql());
        sqlBuilder.append(whereToEndSql.toSql());
        ExecutableSql executableSql = new ExecutableSql();
        executableSql.setSql(sqlBuilder.toString());
        queryBuilder.handleParameters(executableSql);
        executableSql.setType(SqlType.SELECT);
        SqlExecutor sqlExecutor = queryBuilder.flexibleSqlContext.getContext().getSqlExecutor(SingleSqlExecutor.class);
        TypeHandler<Long> longTypeHandler = queryBuilder.flexibleSqlContext.getContext().getTypeHandler(Long.class);
        Object result = sqlExecutor.query(new ExecutorParameters(queryBuilder.flexibleSqlContext.getContext().getConnection(),
                executableSql, new ValueResultSetConvertor<>(longTypeHandler), new PreparedStatementHelper(), ""));
        return result == null ? -1 : (long) result;
    }

    /**
     * 获取查询构建器
     *
     * @return QueryBuilder<T> 查询构建器
     */
    public QueryBuilder<T> getQueryBuilder() {
        return queryBuilder;
    }

    /**
     * 获取可执行SQL
     *
     * @return ExecutableSql 可执行SQL
     */
    public ExecutableSql getExecutableSql() {
        return executableSql;
    }
}
