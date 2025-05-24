package io.github.maxwellnie.javormio.core.translation.method.impl;

import io.github.maxwellnie.javormio.core.execution.executor.SqlExecutor;
import io.github.maxwellnie.javormio.core.execution.executor.parameter.ExecutableSql;
import io.github.maxwellnie.javormio.core.execution.executor.parameter.ExecutorParameters;
import io.github.maxwellnie.javormio.core.translation.Dialect;
import io.github.maxwellnie.javormio.core.translation.SqlType;
import io.github.maxwellnie.javormio.core.translation.sql.SqlBuilder;
import io.github.maxwellnie.javormio.core.translation.sql.SqlFragment;
import io.github.maxwellnie.javormio.core.translation.table.TableInfo;
import io.github.maxwellnie.javormio.core.translation.table.column.ColumnInfo;
import io.github.maxwellnie.javormio.core.translation.table.primary.JoinInfo;

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
        TableInfo<T> tableInfo = this.mapperContext.getTableInfo();
        JoinInfo<T,?>[] joinInfos = tableInfo.getJoinInfo();
        LinkedHashSet<String> columns = new LinkedHashSet<>();
        for (ColumnInfo<T,?> columnInfo : tableInfo.getColumnInfos()){
            columns.add(columnInfo.getColumnName());
        }
        if (joinInfos != null){
            for (JoinInfo<T,?> joinInfo : joinInfos){
                for (ColumnInfo<?,?> joinColumnInfo : joinInfo.getColumnInfos()){
                    columns.add(joinColumnInfo.getColumnName());
                }
            }
        }
        SqlBuilder sqlBuilder = new SqlBuilder();
        SqlFragment beforeSqlFragment = dialect.beforeSqlBuild(null, Dialect.SELECT_FRAGMENT);
        sqlBuilder.append(beforeSqlFragment);
        selectToColumnFragment(sqlBuilder, columns);
        fromToEnd(sqlBuilder, tableInfo, joinInfos);
        SqlFragment afterSqlFragment = dialect.afterSqlBuild(sqlBuilder, Dialect.SELECT_FRAGMENT);
        if (afterSqlFragment != null)
            return afterSqlFragment;
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
    protected void fromToEnd(SqlBuilder sqlBuilder, TableInfo<T> tableInfo, JoinInfo<T,?>[] joinInfos){
        sqlBuilder.append(" FROM ")
                .append(tableInfo.tableName);
        if (joinInfos != null){
            for (JoinInfo<T,?> joinInfo : joinInfos){
                sqlBuilder.append(" ")
                        .append(joinInfo.getJoinType())
                        .append(joinInfo.tableName)
                        .append(" ON ")
                        .append(tableInfo.tableName)
                        .append(".")
                        .append(tableInfo.getColumnInfoMapping().get(joinInfo.getMasterKey()).getColumnName())
                        .append(" = ")
                        .append(joinInfo.tableName)
                        .append(".")
                        .append(joinInfo.getColumnInfoMapping().get(joinInfo.getSlaveKey()).getColumnName());
            }
        }
    }
}
