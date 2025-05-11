package io.github.maxwellnie.javormio.flexible.sql.plugin.expression.impl;

import io.github.maxwellnie.javormio.core.translation.table.BaseMetaTableInfo;
import io.github.maxwellnie.javormio.core.translation.table.column.ColumnInfo;
import io.github.maxwellnie.javormio.flexible.sql.plugin.expression.ColumnNameHandler;
import io.github.maxwellnie.javormio.flexible.sql.plugin.expression.SqlExpressionSupport;
import io.github.maxwellnie.javormio.flexible.sql.plugin.SqlBuilder;

import java.util.Map;

/**
 * @author Maxwell Nie
 */
public class ValueBetween<E, T> extends DoubleValueExpression<E, T>{
    public ValueBetween(ColumnInfo<E, T> firstColumnInfo, T firstValue, T secondValue) {
        super(firstColumnInfo, firstValue, secondValue);
    }

    @Override
    public void applySql(SqlExpressionSupport sqlExpressionSupport, SqlBuilder sqlBuilder, Map<ColumnInfo, String> columnAliases, Map<BaseMetaTableInfo, String> tableAliases) {
        String columnName = ColumnNameHandler.getColumnName(firstColumnInfo, columnAliases, tableAliases);
        sqlExpressionSupport.between(sqlBuilder, columnName, firstValue, secondValue, firstColumnInfo.getTypeHandler());
    }
}
