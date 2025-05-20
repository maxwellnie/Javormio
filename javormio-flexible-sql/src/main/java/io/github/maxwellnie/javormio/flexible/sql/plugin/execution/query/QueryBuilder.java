package io.github.maxwellnie.javormio.flexible.sql.plugin.execution.query;

import io.github.maxwellnie.javormio.common.java.api.JavormioException;
import io.github.maxwellnie.javormio.core.execution.executor.parameter.ExecutableSql;
import io.github.maxwellnie.javormio.core.translation.SqlParameter;
import io.github.maxwellnie.javormio.core.translation.SqlType;
import io.github.maxwellnie.javormio.core.translation.table.BaseMetaTableInfo;
import io.github.maxwellnie.javormio.core.translation.table.TableException;
import io.github.maxwellnie.javormio.core.translation.table.column.ColumnInfo;
import io.github.maxwellnie.javormio.flexible.sql.plugin.AliasHelper;
import io.github.maxwellnie.javormio.flexible.sql.plugin.AliasTable;
import io.github.maxwellnie.javormio.flexible.sql.plugin.FlexibleSqlContext;
import io.github.maxwellnie.javormio.flexible.sql.plugin.SqlBuilder;
import io.github.maxwellnie.javormio.flexible.sql.plugin.execution.Conditions;
import io.github.maxwellnie.javormio.flexible.sql.plugin.expression.SqlExpressionSupport;
import io.github.maxwellnie.javormio.flexible.sql.plugin.function.SqlFunctionColumn;
import io.github.maxwellnie.javormio.flexible.sql.plugin.table.ExpressionColumnInfo;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author Maxwell Nie
 */
public class QueryBuilder<T> {
    protected BaseMetaTableInfo<T> table;
    protected SqlBuilder selectColumnSql;
    protected SqlBuilder fromToOnSql;
    protected SqlBuilder whereToEndSql;
    protected SqlExpressionSupport sqlExpressionSupport;
    protected List<ColumnInfo> allColumns = new LinkedList<>();
    protected LinkedHashMap<ColumnInfo, String> columnAliasMap = new LinkedHashMap<>();
    protected LinkedHashMap<BaseMetaTableInfo, String> tableAliasMap = new LinkedHashMap<>();
    protected FlexibleSqlContext flexibleSqlContext;

    protected QueryBuilder() {
    }

    /**
     * 从数据表table创建查询对象，查询的默认映射结果集为当前数据表所对应实体
     * <p>此方法默认将当前数据表的全部列进行查询</p>
     *
     * @param table
     * @param context
     * @param <R>
     * @return QueryBuilder<R>
     * @throws JavormioException
     */
    public static <R> QueryBuilder<R> from(BaseMetaTableInfo<R> table, FlexibleSqlContext context) throws JavormioException {
        QueryBuilder<R> queryBuilder = new QueryBuilder<>();
        queryBuilder.table = table;
        Supplier<SqlBuilder> sqlBuilderFactory = context.getSqlBuilderFactory();
        queryBuilder.flexibleSqlContext = context;
        queryBuilder.selectColumnSql = sqlBuilderFactory.get();
        queryBuilder.fromToOnSql = sqlBuilderFactory.get();
        queryBuilder.whereToEndSql = sqlBuilderFactory.get();
        queryBuilder.selectColumnSql.append(sqlBuilderFactory.get());
        queryBuilder.fromToOnSql.append(sqlBuilderFactory.get());
        queryBuilder.whereToEndSql.append(sqlBuilderFactory.get());
        queryBuilder.sqlExpressionSupport = context.getSqlExpressionSupport();
        queryBuilder.allColumns.addAll(Arrays.asList(table.getColumnInfos()));
        queryBuilder.fromToOnSql.append(" FROM ").append(table.tableName);
        return queryBuilder;
    }

    /**
     * 从数据表table创建查询对象，并设置该数据表的别名，查询的默认映射结果集为当前数据表所对应实体
     * <p>此方法默认将当前数据表的全部列进行查询</p>
     *
     * @param table
     * @param alias
     * @param context
     * @param
     * @return QueryBuilder
     * @throws JavormioException
     */
    public static <R> QueryBuilder<R> from(BaseMetaTableInfo<R> table, String alias, FlexibleSqlContext context) throws JavormioException {
        QueryBuilder<R> queryBuilder = from(table, context);
        queryBuilder.tableAliasMap.put(table, alias);
        queryBuilder.fromToOnSql.append(" AS ").append(alias);
        return queryBuilder;
    }

    /**
     * 从数据表table创建查询对象，并设置该数据表的别名、列的别名，查询的默认映射结果集为当前数据表所对应实体
     * <p>此方法默认将当前数据表的全部列进行查询</p>
     *
     * @param table
     * @param context
     * @param
     * @return QueryBuilder
     * @throws JavormioException
     */
    public static <R> QueryBuilder<R> from(BaseMetaTableInfo<R> table, FlexibleSqlContext context, AliasTable aliasTable) {
        QueryBuilder<R> queryBuilder = from(table, context);
        queryBuilder.loadAlias(aliasTable);
        if (queryBuilder.tableAliasMap.containsValue(table)) {
            queryBuilder.selectColumnSql.append(" AS ").append(table.tableName);
        }
        return queryBuilder;
    }

    /**
     * 从子表childrenDataTable创建查询对象，并设置子表的别名，查询的默认映射结果集为当前子表所对应实体
     * <p>此方法默认将当前子表的全部列进行查询</p>
     *
     * @param subqueryTable
     * @param context
     * @param <T>
     * @param <R>
     * @return QueryBuilder<T>
     */
    public static <T, R> QueryBuilder<R> fromSubquery(SubqueryTable<R, T> subqueryTable, String alias, FlexibleSqlContext context) {
        if (alias == null)
            throw new TableException("A subquery table has an empty name.");
        QueryBuilder<R> queryBuilder = new QueryBuilder<>();
        queryBuilder.table = subqueryTable;
        queryBuilder.flexibleSqlContext = context;
        Supplier<SqlBuilder> sqlBuilderFactory = context.getSqlBuilderFactory();
        queryBuilder.flexibleSqlContext = context;
        queryBuilder.selectColumnSql.append(sqlBuilderFactory.get());
        queryBuilder.fromToOnSql.append(sqlBuilderFactory.get());
        queryBuilder.whereToEndSql.append(sqlBuilderFactory.get());
        queryBuilder.sqlExpressionSupport = context.getSqlExpressionSupport();
        queryBuilder.allColumns.addAll(Arrays.asList(subqueryTable.getColumnInfos()));
        queryBuilder.fromToOnSql.append(" FROM ").append(subqueryTable.getSqlInstance());
        queryBuilder.tableAliasMap.put(subqueryTable, alias);
        queryBuilder.fromToOnSql.append(" AS ").append(alias);
        return queryBuilder;
    }

    /**
     * 从子表childrenDataTable创建查询对象，并设置子表的别名、列的别名，查询的默认映射结果集为当前子表所对应实体
     * <p>此方法默认将当前子表全部列进行查询</p>
     *
     * @param table
     * @param context
     * @param <T>
     * @param <R>
     * @return QueryBuilder<T>
     */
    public static <T, R> QueryBuilder<R> fromSubquery(SubqueryTable<R, T> table, FlexibleSqlContext context, AliasTable aliasTable) {
        String alias = aliasTable.getTableAliasMap().get(table);
        QueryBuilder<R> queryBuilder = fromSubquery(table, alias, context);
        queryBuilder.loadAlias(aliasTable);
        return queryBuilder;
    }

    /**
     * 清除当前全部需要被查询的列
     *
     * @return QueryBuilder
     */
    public QueryBuilder<T> clearColumns() {
        allColumns.clear();
        return this;
    }

    /**
     * 添加被查询的列
     *
     * @param expressionColumnInfos
     * @return QueryBuilder
     */
    public QueryBuilder<T> addColumns(ExpressionColumnInfo... expressionColumnInfos) {
        for (ExpressionColumnInfo expressionColumnInfo : expressionColumnInfos) {
            allColumns.add(expressionColumnInfo.getColumnInfo());
        }
        return this;
    }

    /**
     * 添加被查询的列，使用函数作为列
     *
     * @param sqlFunctionColumns
     * @return QueryBuilder
     */
    public QueryBuilder<T> addColumns(SqlFunctionColumn... sqlFunctionColumns) {
        for (SqlFunctionColumn sqlFunctionColumn : sqlFunctionColumns) {
            SqlFunctionColumn.SqlFunctionColumnInfo columnInfo = sqlFunctionColumn.toColumnInfo();
            allColumns.add(columnInfo);
            selectColumnSql.append(columnInfo.getFunction().getParameters());
            columnAliasMap.put(columnInfo, sqlFunctionColumn.getAlias());
        }
        return this;
    }

    /**
     * 排除被查询的列
     *
     * @param excludedColumns
     * @return QueryBuilder
     */
    public QueryBuilder<T> excludeColumns(ExpressionColumnInfo... excludedColumns) {
        for (ExpressionColumnInfo expressionColumnInfo : excludedColumns) {
            allColumns.remove(expressionColumnInfo.getColumnInfo());
        }
        return this;
    }

    /**
     * 加载别名信息
     *
     * @param aliasTable
     */
    protected void loadAlias(AliasTable aliasTable) {
        if (aliasTable.getColumnAliasMap() != null) {
            columnAliasMap.putAll(aliasTable.getColumnAliasMap());
        }
        if (aliasTable.getTableAliasMap() != null) {
            tableAliasMap.putAll(aliasTable.getTableAliasMap());
        }
    }

    /**
     * 设置列的别名
     *
     * @param aliasConsumer
     * @return QueryBuilder
     */
    public QueryBuilder<T> aliasColumns(Consumer<AliasHelper> aliasConsumer) {
        this.columnAliasMap = new LinkedHashMap<>();
        AliasHelper aliasHelper = new AliasHelper(columnAliasMap);
        aliasConsumer.accept(aliasHelper);
        return this;
    }

    /**
     * 设置查询条件
     *
     * @param conditionConsumer
     * @return QueryBuilder
     */
    public QueryBuilder<T> where(Consumer<Conditions> conditionConsumer) {
        if (whereToEndSql.getSqlStringBuilder().length() == 0)
            whereToEndSql.append(" WHERE");
        Conditions conditions = new Conditions(whereToEndSql, sqlExpressionSupport, columnAliasMap, tableAliasMap);
        conditionConsumer.accept(conditions);
        return this;
    }

    /**
     * 使用JOIN ON模式的表关联
     * <p>默认将关联表的全部列进行查询</p>
     *
     * @param table
     * @param conditionConsumer
     * @return QueryBuilder
     */
    public QueryBuilder<T> joinOn(BaseMetaTableInfo<T> table, Consumer<Conditions> conditionConsumer) {
        fromToOnSql.append(" JOIN ").append(table.tableName).append(" ON");
        allColumns.addAll(Arrays.asList(table.getColumnInfos()));
        Conditions conditions = new Conditions(fromToOnSql, sqlExpressionSupport, columnAliasMap, tableAliasMap);
        conditionConsumer.accept(conditions);
        return this;
    }

    /**
     * 使用JOIN WHERE模式的表关联
     * <p>默认将关联表的全部列进行查询</p>
     *
     * @param table
     * @param conditionConsumer
     * @return QueryBuilder
     */
    public QueryBuilder<T> join(BaseMetaTableInfo<T> table, Consumer<Conditions> conditionConsumer) {
        fromToOnSql.append(" JOIN ").append(table.tableName);
        allColumns.addAll(Arrays.asList(table.getColumnInfos()));
        where(conditionConsumer);
        return this;
    }

    /**
     * 使用LEFT JOIN ON模式的表关联
     * <p>默认将关联表的全部列进行查询</p>
     *
     * @param table
     * @param conditionConsumer
     * @return QueryBuilder
     */
    public QueryBuilder<T> leftJoinOn(BaseMetaTableInfo<T> table, Consumer<Conditions> conditionConsumer) {
        fromToOnSql.append(" LEFT JOIN ").append(table.tableName).append(" ON");
        allColumns.addAll(Arrays.asList(table.getColumnInfos()));
        Conditions conditions = new Conditions(fromToOnSql, sqlExpressionSupport, columnAliasMap, tableAliasMap);
        conditionConsumer.accept(conditions);
        return this;
    }

    /**
     * 使用RIGHT JOIN ON模式的表关联
     * <p>默认将关联表的全部列进行查询</p>
     *
     * @param table
     * @param conditionConsumer
     * @return QueryBuilder
     */
    public QueryBuilder<T> rightJoinOn(BaseMetaTableInfo<T> table, Consumer<Conditions> conditionConsumer) {
        fromToOnSql.append(" RIGHT JOIN ").append(table.tableName).append(" ON");
        allColumns.addAll(Arrays.asList(table.getColumnInfos()));
        Conditions conditions = new Conditions(fromToOnSql, sqlExpressionSupport, columnAliasMap, tableAliasMap);
        conditionConsumer.accept(conditions);
        return this;
    }

    /**
     * 使用JOIN ON模式的表关联，并指定关联表的别名
     * <p>默认将关联表的全部列进行查询</p>
     *
     * @param table
     * @param alias
     * @param conditionConsumer
     * @return QueryBuilder
     */
    public QueryBuilder<T> joinOn(BaseMetaTableInfo<T> table, String alias, Consumer<Conditions> conditionConsumer) {
        tableAliasMap.put(table, alias);
        fromToOnSql.append(" JOIN ").append(table.tableName).append(" AS ").append(alias).append(" ON");
        allColumns.addAll(Arrays.asList(table.getColumnInfos()));
        Conditions conditions = new Conditions(fromToOnSql, sqlExpressionSupport, columnAliasMap, tableAliasMap);
        conditionConsumer.accept(conditions);
        return this;
    }

    /**
     * 使用JOIN WHERE模式的表关联，并指定关联表的别名
     * <p>默认将关联表全部列进行查询</p>
     *
     * @param table
     * @param alias
     * @param conditionConsumer
     * @return QueryBuilder
     */
    public QueryBuilder<T> join(BaseMetaTableInfo<T> table, String alias, Consumer<Conditions> conditionConsumer) {
        fromToOnSql.append(" JOIN ").append(table.tableName).append(" AS ").append(alias);
        allColumns.addAll(Arrays.asList(table.getColumnInfos()));
        where(conditionConsumer);
        return this;
    }

    /**
     * 使用LEFT JOIN ON模式的表关联，并指定关联表的别名
     * <p>默认将关联表全部列进行查询</p>
     *
     * @param table
     * @param alias
     * @param conditionConsumer
     * @return QueryBuilder
     */
    public QueryBuilder<T> leftJoinOn(BaseMetaTableInfo<T> table, String alias, Consumer<Conditions> conditionConsumer) {
        fromToOnSql.append(" LEFT JOIN ").append(table.tableName).append(" AS ").append(alias).append(" ON");
        allColumns.addAll(Arrays.asList(table.getColumnInfos()));
        Conditions conditions = new Conditions(fromToOnSql, sqlExpressionSupport, columnAliasMap, tableAliasMap);
        conditionConsumer.accept(conditions);
        return this;
    }

    /**
     * 使用RIGHT JOIN ON模式的表关联，并指定关联表的别名
     * <p>默认将关联表全部列进行查询</p>
     *
     * @param table
     * @param alias
     * @param conditionConsumer
     * @return QueryBuilder
     */
    public QueryBuilder<T> rightJoinOn(BaseMetaTableInfo<T> table, String alias, Consumer<Conditions> conditionConsumer) {
        fromToOnSql.append(" RIGHT JOIN ").append(table.tableName).append(" AS ").append(alias).append(" ON");
        allColumns.addAll(Arrays.asList(table.getColumnInfos()));
        Conditions conditions = new Conditions(fromToOnSql, sqlExpressionSupport, columnAliasMap, tableAliasMap);
        conditionConsumer.accept(conditions);
        return this;
    }

    /**
     * 使用GROUP BY进行分组
     *
     * @param columnInfos
     * @return QueryBuilder
     */
    public QueryBuilder<T> groupBy(ExpressionColumnInfo... columnInfos) {
        whereToEndSql.append(" GROUP BY ");
        for (int i = 0; i < columnInfos.length; i++) {
            if (i != 0)
                whereToEndSql.append(",");
            String columnName = columnAliasMap.get(columnInfos[i].getColumnInfo());
            if (columnName == null)
                columnName = columnInfos[i].getColumnInfo().getColumnName();
            whereToEndSql.append(columnName);
        }
        return this;
    }

    /**
     * 使用GROUP BY进行分组，并指定HAVING条件
     *
     * @param havingConditions
     * @param columnInfos
     * @return QueryBuilder<T>
     */
    public QueryBuilder<T> groupBy(Consumer<Conditions> havingConditions, ExpressionColumnInfo... columnInfos) {
        groupBy(columnInfos);
        whereToEndSql.append(" HAVING");
        Conditions conditions = new Conditions(whereToEndSql, sqlExpressionSupport, columnAliasMap, tableAliasMap);
        havingConditions.accept(conditions);
        return this;
    }

    /**
     * 使用ORDER BY进行排序
     *
     * @param columnInfos
     * @return QueryBuilder
     */
    public QueryBuilder<T> orderBy(ExpressionColumnInfo... columnInfos) {
        whereToEndSql.append(" ORDER BY ");
        for (int i = 0; i < columnInfos.length; i++) {
            if (i != 0)
                whereToEndSql.append(",");
            String columnName = columnAliasMap.get(columnInfos[i].getColumnInfo());
            if (columnName == null)
                columnName = columnInfos[i].getColumnInfo().getColumnName();
            whereToEndSql.append(columnName);
        }
        return this;
    }

    /**
     * 设置排序方式
     *
     * @param asc
     * @return QueryBuilder
     */
    public QueryBuilder<T> sort(boolean... asc) {
        for (int i = 0; i < asc.length; i++) {
            if (i != 0)
                whereToEndSql.append(",");
            if (asc[i])
                whereToEndSql.append(" ASC");
            else
                whereToEndSql.append(" DESC");
        }
        return this;
    }

    /**
     * 分页，从limit开始，offset条记录
     *
     * @param limit
     * @param offset
     * @return QueryBuilder
     */
    public QueryBuilder<T> limit(long limit, long offset) {
        whereToEndSql.append(" LIMIT ?", new SqlParameter(limit, flexibleSqlContext.getContext().getTypeHandler(Long.class)));
        if (offset > 0) {
            whereToEndSql.append(" OFFSET ?", new SqlParameter(offset, flexibleSqlContext.getContext().getTypeHandler(Long.class)));
        }
        return this;
    }

    /**
     * 转换成Query对象
     *
     * @return Query
     */
    public Query<T> toQuery() {
        return new Query<>(this, null);
    }

    /**
     * 转换成可执行的SQL对象
     *
     * @return ExecutableSql
     */
    public ExecutableSql toExecutableSql() {
        StringBuilder sqlBuilder = new StringBuilder();
        handleSelectColumn();
        sqlBuilder.append(selectColumnSql.toSql());
        sqlBuilder.append(fromToOnSql.toSql());
        sqlBuilder.append(whereToEndSql.toSql());
        ExecutableSql executableSql = new ExecutableSql();
        executableSql.setSql(sqlBuilder.toString());
        handleParameters(executableSql);
        executableSql.setType(SqlType.SELECT);
        return executableSql;
    }

    void handleParameters(ExecutableSql executableSql) {
        List<SqlParameter[]> parametersList = new LinkedList<>();
        int length = selectColumnSql.getParameterList().size() + fromToOnSql.getParameterList().size() + whereToEndSql.getParameterList().size();
        SqlParameter[] parameters = new SqlParameter[length];
        int i = 0;
        for (SqlParameter sqlParameter : selectColumnSql.getParameterList()) {
            parameters[i++] = sqlParameter;
        }
        i = 0;
        for (SqlParameter sqlParameter : fromToOnSql.getParameterList()) {
            parameters[i++] = sqlParameter;
        }
        i = 0;
        for (SqlParameter sqlParameter : whereToEndSql.getParameterList()) {
            parameters[i++] = sqlParameter;
        }
        parametersList.add(parameters);
        executableSql.setParametersList(parametersList);
    }

    void handleSelectColumn() {
        selectColumnSql.append("SELECT ");
        for (int i = 0; i < allColumns.size(); i++) {
            if (i != 0)
                selectColumnSql.append(",");
            selectColumnSql.append(allColumns.get(i).getColumnName());
            if (columnAliasMap.containsKey(allColumns.get(i))) {
                selectColumnSql.append(" AS ").append(columnAliasMap.get(allColumns.get(i)));
            }
        }
    }

    /**
     * 转换成子查询表
     *
     * @return SubqueryTable
     */
    public SubqueryTable<T, T> toSubqueryTable() {
        ExecutableSql executableSql = toExecutableSql();
        SqlBuilder sqlInstance = new SqlBuilder();
        sqlInstance.append(executableSql.getSql(), executableSql.getParametersList().get(0));
        return SubqueryTable.from(table, sqlInstance);
    }

    /**
     * 转换成Query对象，并执行consumer
     *
     * @param consumer
     * @return Query
     */
    public Query<T> toQuery(Consumer<ExecutableSql> consumer) {
        Query<T> query = toQuery();
        consumer.accept(query.getExecutableSql());
        return query;
    }

}
