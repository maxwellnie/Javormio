package io.github.maxwellnie.javormio.flexible.sql.plugin.execution;

import io.github.maxwellnie.javormio.core.translation.table.BaseMetaTableInfo;
import io.github.maxwellnie.javormio.core.translation.table.column.ColumnInfo;
import io.github.maxwellnie.javormio.core.translation.sql.SqlBuilder;
import io.github.maxwellnie.javormio.flexible.sql.plugin.expression.Expression;
import io.github.maxwellnie.javormio.flexible.sql.plugin.expression.SqlExpressionSupport;

import java.util.Map;

/**
 * @author Maxwell Nie
 */
public class Conditions<S extends SqlExpressionSupport> {
    protected SqlBuilder conditionSql;
    protected S sqlExpressionSupport;
    protected Map<ColumnInfo, String> columnAliasMap;
    protected Map<BaseMetaTableInfo, String> tableAliasMap;

    public Conditions(SqlBuilder conditionSql, S sqlExpressionSupport, Map<ColumnInfo, String> columnAliasMap, Map<BaseMetaTableInfo, String> tableAliasMap) {
        this.conditionSql = conditionSql;
        this.sqlExpressionSupport = sqlExpressionSupport;
        this.columnAliasMap = columnAliasMap;
        this.tableAliasMap = tableAliasMap;
    }

    public Conditions<S> condition(Expression<S> expression) {
        expression.applySql(sqlExpressionSupport, conditionSql, columnAliasMap, tableAliasMap);
        return this;
    }

    public Conditions<S> and(Expression<S> expression) {
        sqlExpressionSupport.and(conditionSql);
        expression.applySql(sqlExpressionSupport, conditionSql, columnAliasMap, tableAliasMap);
        return this;
    }

    public Conditions<S> or(Expression<S> expression) {
        sqlExpressionSupport.or(conditionSql);
        expression.applySql(sqlExpressionSupport, conditionSql, columnAliasMap, tableAliasMap);
        return this;
    }

    public SqlBuilder getConditionSql() {
        return conditionSql;
    }

    public S getSqlExpressionSupport() {
        return sqlExpressionSupport;
    }

    public Map<ColumnInfo, String> getColumnAliasMap() {
        return columnAliasMap;
    }

    public Map<BaseMetaTableInfo, String> getTableAliasMap() {
        return tableAliasMap;
    }
}
