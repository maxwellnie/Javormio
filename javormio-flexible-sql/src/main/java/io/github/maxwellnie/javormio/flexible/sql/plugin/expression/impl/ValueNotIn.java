package io.github.maxwellnie.javormio.flexible.sql.plugin.expression.impl;

import io.github.maxwellnie.javormio.core.translation.table.BaseMetaTableInfo;
import io.github.maxwellnie.javormio.core.translation.table.column.ColumnInfo;
import io.github.maxwellnie.javormio.flexible.sql.plugin.SqlBuilder;
import io.github.maxwellnie.javormio.flexible.sql.plugin.expression.ColumnNameHandler;
import io.github.maxwellnie.javormio.flexible.sql.plugin.expression.SqlExpressionSupport;

import java.util.Map;

/**
 * @author Maxwell Nie
 */
public class ValueNotIn<E, T> extends ValuesColumnExpression<E, T>{
    public ValueNotIn(ColumnInfo<E, T> columnInfo, T[] values) {
        super(columnInfo, values);
    }

    @Override
    public void applySql(SqlExpressionSupport sqlExpressionSupport, SqlBuilder sqlBuilder, Map<ColumnInfo, String> columnAliases, Map<BaseMetaTableInfo, String> tableAliases) {
        String columnName = ColumnNameHandler.getColumnName(columnInfo, columnAliases, tableAliases);
        sqlExpressionSupport.notIn(sqlBuilder, columnName, values,
                columnInfo.getTypeHandler());
    }
}
