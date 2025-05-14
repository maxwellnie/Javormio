package io.github.maxwellnie.javormio.flexible.sql.plugin;

import io.github.maxwellnie.javormio.common.java.api.JavormioException;
import io.github.maxwellnie.javormio.common.java.reflect.ObjectFactory;
import io.github.maxwellnie.javormio.core.execution.ExecutableSql;
import io.github.maxwellnie.javormio.core.translation.SqlParameter;
import io.github.maxwellnie.javormio.core.translation.SqlType;
import io.github.maxwellnie.javormio.core.translation.table.BaseMetaTableInfo;
import io.github.maxwellnie.javormio.core.translation.table.column.ColumnInfo;
import io.github.maxwellnie.javormio.flexible.sql.plugin.expression.Expression;
import io.github.maxwellnie.javormio.flexible.sql.plugin.expression.SqlExpressionSupport;
import io.github.maxwellnie.javormio.flexible.sql.plugin.table.ExpressionColumnInfo;

import java.util.*;
import java.util.function.Consumer;

/**
 * @author Maxwell Nie
 */
public class QueryBuilder<T> {
    protected BaseMetaTableInfo<T> table;
    protected SqlBuilder selectColumnSql = new SqlBuilder().append("SELECT ");
    protected SqlBuilder fromToOnSql = new SqlBuilder().append(" FROM");
    protected SqlBuilder whereToEndSql = new SqlBuilder();
    protected SqlExpressionSupport sqlExpressionSupport = new SqlExpressionSupport();
    protected List<ColumnInfo> allColumns = new LinkedList<>();
    protected Map<ColumnInfo, String> columnAliasMap = new LinkedHashMap<>();
    protected Map<BaseMetaTableInfo, String> tableAliasMap = new LinkedHashMap<>();
    protected FlexibleSqlContext context;

    protected QueryBuilder() {
    }

    public static <R> QueryBuilder<R> from(BaseMetaTableInfo<R> table, FlexibleSqlContext context) throws JavormioException {
        try {
            QueryBuilder<R> queryBuilder = new QueryBuilder<>();
            queryBuilder.table = table;
            queryBuilder.context = context;
            SqlContext sqlContext = context.getSqlContext();
            ObjectFactory<SqlBuilder> sqlBuilderFactory = sqlContext.getSqlBuilderFactory();
            queryBuilder.selectColumnSql.append(sqlBuilderFactory.produce());
            queryBuilder.fromToOnSql.append(sqlBuilderFactory.produce());
            queryBuilder.whereToEndSql.append(sqlBuilderFactory.produce());
            queryBuilder.sqlExpressionSupport = sqlContext.getSqlExpressionSupport();
            queryBuilder.allColumns.addAll(Arrays.asList(table.getColumnInfos()));
            queryBuilder.fromToOnSql.append(" ").append(table.tableName);
            return queryBuilder;
        } catch (Exception e) {
            throw new JavormioException(e);
        }
    }

    public static <R> QueryBuilder<R> from(BaseMetaTableInfo<R> table, String alias, FlexibleSqlContext context) throws JavormioException {
        QueryBuilder<R> queryBuilder = from(table, context);
        queryBuilder.tableAliasMap.put(table, alias);
        queryBuilder.fromToOnSql.append(" AS ").append(alias);
        return queryBuilder;
    }

    public QueryBuilder<T> excludeColumns(ExpressionColumnInfo... excludedColumns) {
        for (ExpressionColumnInfo expressionColumnInfo : excludedColumns) {
            allColumns.remove(expressionColumnInfo.getColumnInfo());
        }
        return this;
    }

    public QueryBuilder<T> aliasColumns(Consumer<AliasHelper> aliasConsumer) {
        this.columnAliasMap = new LinkedHashMap<>();
        AliasHelper aliasHelper = new AliasHelper(columnAliasMap);
        aliasConsumer.accept(aliasHelper);
        return this;
    }

    public QueryBuilder<T> and(Expression expression) {
        sqlExpressionSupport.and(whereToEndSql);
        expression.applySql(sqlExpressionSupport, whereToEndSql, columnAliasMap, tableAliasMap);
        return this;
    }

    public QueryBuilder<T> or(Expression expression) {
        sqlExpressionSupport.or(whereToEndSql);
        expression.applySql(sqlExpressionSupport, whereToEndSql, columnAliasMap, tableAliasMap);
        return this;
    }

    public QueryBuilder<T> where(Expression expression) {
        whereToEndSql.append(" WHERE");
        expression.applySql(sqlExpressionSupport, whereToEndSql, columnAliasMap, tableAliasMap);
        return this;
    }

    public QueryBuilder<T> joinOn(BaseMetaTableInfo<T> table, Expression expression) {
        fromToOnSql.append(" JOIN ").append(table.tableName).append(" ON");
        allColumns.addAll(Arrays.asList(table.getColumnInfos()));
        expression.applySql(sqlExpressionSupport, fromToOnSql, columnAliasMap, tableAliasMap);
        return this;
    }

    public QueryBuilder<T> join(BaseMetaTableInfo<T> table, Expression expression) {
        fromToOnSql.append(" JOIN ").append(table.tableName);
        allColumns.addAll(Arrays.asList(table.getColumnInfos()));
        expression.applySql(sqlExpressionSupport, whereToEndSql, columnAliasMap, tableAliasMap);
        return this;
    }

    public QueryBuilder<T> leftJoinOn(BaseMetaTableInfo<T> table, Expression expression) {
        fromToOnSql.append(" LEFT JOIN ").append(table.tableName).append(" ON");
        allColumns.addAll(Arrays.asList(table.getColumnInfos()));
        where(expression);
        return this;
    }

    public QueryBuilder<T> rightJoinOn(BaseMetaTableInfo<T> table, Expression expression) {
        fromToOnSql.append(" RIGHT JOIN ").append(table.tableName).append(" ON");
        allColumns.addAll(Arrays.asList(table.getColumnInfos()));
        expression.applySql(sqlExpressionSupport, fromToOnSql, columnAliasMap, tableAliasMap);
        return this;
    }

    public QueryBuilder<T> joinOn(BaseMetaTableInfo<T> table, String alias, Expression expression) {
        tableAliasMap.put(table, alias);
        fromToOnSql.append(" JOIN ").append(table.tableName).append(" AS ").append(alias).append(" ON");
        allColumns.addAll(Arrays.asList(table.getColumnInfos()));
        expression.applySql(sqlExpressionSupport, fromToOnSql, columnAliasMap, tableAliasMap);
        return this;
    }

    public QueryBuilder<T> join(BaseMetaTableInfo<T> table, String alias, Expression expression) {
        fromToOnSql.append(" JOIN ").append(table.tableName).append(" AS ").append(alias);
        allColumns.addAll(Arrays.asList(table.getColumnInfos()));
        expression.applySql(sqlExpressionSupport, whereToEndSql, columnAliasMap, tableAliasMap);
        return this;
    }

    public QueryBuilder<T> leftJoinOn(BaseMetaTableInfo<T> table, String alias, Expression expression) {
        fromToOnSql.append(" LEFT JOIN ").append(table.tableName).append(" AS ").append(alias).append(" ON");
        allColumns.addAll(Arrays.asList(table.getColumnInfos()));
        expression.applySql(sqlExpressionSupport, fromToOnSql, columnAliasMap, tableAliasMap);
        return this;
    }

    public QueryBuilder<T> rightJoinOn(BaseMetaTableInfo<T> table, String alias, Expression expression) {
        fromToOnSql.append(" RIGHT JOIN ").append(table.tableName).append(" AS ").append(alias).append(" ON");
        allColumns.addAll(Arrays.asList(table.getColumnInfos()));
        expression.applySql(sqlExpressionSupport, fromToOnSql, columnAliasMap, tableAliasMap);
        return this;
    }

    public QueryBuilder<T> groupBy(ColumnInfo... columnInfos) {
        whereToEndSql.append(" GROUP BY ");
        for (int i = 0; i < columnInfos.length; i++) {
            if (i != 0)
                whereToEndSql.append(",");
            String columnName = columnAliasMap.get(columnInfos[i]);
            if (columnName == null)
                columnName = columnInfos[i].getColumnName();
            whereToEndSql.append(columnName);
        }
        return this;
    }

    public Query<T> toSql() {
        StringBuilder sqlBuilder = selectColumnSql.getSqlStringBuilder();
        for (int i = 0; i < allColumns.size(); i++) {
            if (i != 0)
                sqlBuilder.append(",");
            sqlBuilder.append(allColumns.get(i).getColumnName());
            if (columnAliasMap.containsKey(allColumns.get(i))) {
                sqlBuilder.append(" AS ").append(columnAliasMap.get(allColumns.get(i)));
            }
        }
        sqlBuilder.append(fromToOnSql.toSql());
        sqlBuilder.append(whereToEndSql.toSql());
        ExecutableSql executableSql = new ExecutableSql();
        executableSql.setSqlList(new String[]{sqlBuilder.toString()});
        List<SqlParameter[]> parametersList = new LinkedList<>();
        int length = selectColumnSql.parameters.size() + fromToOnSql.parameters.size() + whereToEndSql.parameters.size();
        SqlParameter[] parameters = new SqlParameter[length];
        int i = 0;
        for (SqlParameter sqlParameter : selectColumnSql.parameters) {
            parameters[i++] = sqlParameter;
        }
        i = 0;
        for (SqlParameter sqlParameter : fromToOnSql.parameters) {
            parameters[i++] = sqlParameter;
        }
        i = 0;
        for (SqlParameter sqlParameter : whereToEndSql.parameters) {
            parameters[i++] = sqlParameter;
        }
        parametersList.add(parameters);
        executableSql.setParametersList(parametersList);
        executableSql.setType(SqlType.SELECT);
        return new Query<>(this, executableSql);
    }

    public Query<T> toSql(Consumer<ExecutableSql> consumer) {
        Query<T> query = toSql();
        consumer.accept(query.getExecutableSql());
        return query;
    }

}
