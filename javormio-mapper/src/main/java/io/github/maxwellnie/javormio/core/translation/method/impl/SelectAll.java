package io.github.maxwellnie.javormio.core.translation.method.impl;

import io.github.maxwellnie.javormio.common.java.table.BaseMetaTableInfo;
import io.github.maxwellnie.javormio.core.execution.executor.SqlExecutor;
import io.github.maxwellnie.javormio.core.execution.executor.parameter.ExecutableSql;
import io.github.maxwellnie.javormio.core.execution.executor.parameter.ExecutorParameters;
import io.github.maxwellnie.javormio.common.java.sql.dialect.Dialect;
import io.github.maxwellnie.javormio.common.java.sql.SqlType;
import io.github.maxwellnie.javormio.core.translation.sql.SqlBuilder;
import io.github.maxwellnie.javormio.common.java.sql.SqlFragment;
import io.github.maxwellnie.javormio.common.java.table.column.ColumnInfo;

import java.util.*;

/**
 * @author Maxwell Nie
 */
public class SelectAll<T> extends BaseMapperSqlMethod<T, List<T>> {
    public SelectAll(MapperContext<T, List<T>> context) {
        super(context);
    }

    @Override
    protected List<T> doInvoke(SqlExecutor sqlExecutor, ExecutorParameters<?, List<T>> executorParameters) {
        return sqlExecutor.query(executorParameters);
    }

    @Override
    protected ExecutorParameters<?, List<T>> getExecutorParameters(Object[] args) {
        ExecutorParameters<?, List<T>> executorParameters = new ExecutorParameters<>();
        executorParameters.setResultSetConvertor(this.mapperContext.convertor);
        executorParameters.setStatementHelper(this.mapperContext.statementHelper);
        executorParameters.setNamespace(this.mapperContext.getNamespace());
        executorParameters.setConnection(this.mapperContext.context.getConnection());
        executorParameters.setExecutableSql(getExecutableSql(args));
        return executorParameters;
    }

    @Override
    protected ExecutableSql getExecutableSql(Object[] args) {
        ExecutableSql executableSql = new ExecutableSql();
        SqlFragment sqlFragment = getSqlFragment(args);
        executableSql.setSql(sqlFragment.toSql());
        executableSql.setParametersList(Collections.singletonList(sqlFragment.getParameters()));
        executableSql.setType(SqlType.SELECT);
        return executableSql;
    }

    @Override
    protected SqlFragment getSqlFragment(Object[] args) {
        Dialect dialect = this.mapperContext.context.getDialect();
        BaseMetaTableInfo<T> tableInfo = this.mapperContext.getTableInfo();
        LinkedHashSet<String> columns = new LinkedHashSet<>();
        for (ColumnInfo<T,?> columnInfo : tableInfo.getColumnInfos()){
            columns.add(columnInfo.getColumnName());
        }
        SqlBuilder sqlBuilder = new SqlBuilder();
        selectToColumnFragment(sqlBuilder, columns);
        return sqlBuilder;
    }

    protected void selectToColumnFragment(SqlBuilder sqlBuilder, LinkedHashSet<String> columns){
        sqlBuilder.append("SELECT ");
        Iterator<String> iterator = columns.iterator();
        while (iterator.hasNext()) {
            sqlBuilder.append(iterator.next());
            if (iterator.hasNext()) sqlBuilder.append(", ");
        }
    }
}
