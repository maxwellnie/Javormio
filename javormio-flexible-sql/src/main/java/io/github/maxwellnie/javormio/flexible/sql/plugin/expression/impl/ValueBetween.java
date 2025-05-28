package io.github.maxwellnie.javormio.flexible.sql.plugin.expression.impl;

import io.github.maxwellnie.javormio.common.java.sql.ColumnEscapeKit;
import io.github.maxwellnie.javormio.common.java.table.BaseMetaTableInfo;
import io.github.maxwellnie.javormio.common.java.table.column.ColumnInfo;
import io.github.maxwellnie.javormio.core.translation.name.ColumnNameHandler;
import io.github.maxwellnie.javormio.core.translation.sql.SqlBuilder;
import io.github.maxwellnie.javormio.flexible.sql.plugin.expression.SqlExpressionSupport;

import java.util.Map;

/**
 * @author Maxwell Nie
 */
public class ValueBetween<S extends SqlExpressionSupport, E, T> extends DoubleValueExpression<S, E, T> {
    public ValueBetween(ColumnInfo<E, T> firstColumnInfo, T firstValue, T secondValue) {
        super(firstColumnInfo, firstValue, secondValue);
    }

    @Override
    public void applySql(S sqlExpressionSupport, SqlBuilder sqlBuilder, Map<ColumnInfo, String> columnAliases,
                         Map<BaseMetaTableInfo, String> tableAliases, ColumnEscapeKit columnEscapeKit) {
        String columnName = ColumnNameHandler.getColumnName(firstColumnInfo, columnAliases, tableAliases, columnEscapeKit);
        sqlExpressionSupport.between(sqlBuilder, columnName, firstValue, secondValue, firstColumnInfo.getTypeHandler());
    }
}
