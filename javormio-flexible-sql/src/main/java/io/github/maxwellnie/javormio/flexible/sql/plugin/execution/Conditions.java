package io.github.maxwellnie.javormio.flexible.sql.plugin.execution;

import io.github.maxwellnie.javormio.core.translation.table.BaseMetaTableInfo;
import io.github.maxwellnie.javormio.core.translation.table.column.ColumnInfo;
import io.github.maxwellnie.javormio.flexible.sql.plugin.SqlBuilder;
import io.github.maxwellnie.javormio.flexible.sql.plugin.expression.Expression;
import io.github.maxwellnie.javormio.flexible.sql.plugin.expression.SqlExpressionSupport;

import java.util.Map;

/**
 * @author Maxwell Nie
 */
public class Conditions {
    protected SqlBuilder conditionSql;
    protected SqlExpressionSupport sqlExpressionSupport;
    protected Map<ColumnInfo, String> columnAliasMap;
    protected Map<BaseMetaTableInfo, String> tableAliasMap;

    public Conditions(SqlBuilder conditionSql, SqlExpressionSupport sqlExpressionSupport, Map<ColumnInfo, String> columnAliasMap, Map<BaseMetaTableInfo, String> tableAliasMap) {
        this.conditionSql = conditionSql;
        this.sqlExpressionSupport = sqlExpressionSupport;
        this.columnAliasMap = columnAliasMap;
        this.tableAliasMap = tableAliasMap;
    }

    public Conditions condition(Expression expression) {
        expression.applySql(sqlExpressionSupport, conditionSql, columnAliasMap, tableAliasMap);
        return this;
    }

    public Conditions and(Expression expression) {
        sqlExpressionSupport.and(conditionSql);
        expression.applySql(sqlExpressionSupport, conditionSql, columnAliasMap, tableAliasMap);
        return this;
    }

    public Conditions or(Expression expression) {
        sqlExpressionSupport.or(conditionSql);
        expression.applySql(sqlExpressionSupport, conditionSql, columnAliasMap, tableAliasMap);
        return this;
    }

    public SqlBuilder getConditionSql() {
        return conditionSql;
    }

    public SqlExpressionSupport getSqlExpressionSupport() {
        return sqlExpressionSupport;
    }

    public Map<ColumnInfo, String> getColumnAliasMap() {
        return columnAliasMap;
    }

    public Map<BaseMetaTableInfo, String> getTableAliasMap() {
        return tableAliasMap;
    }
}
