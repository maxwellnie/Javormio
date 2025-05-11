package io.github.maxwellnie.javormio.flexible.sql.plugin.expression.impl;

import io.github.maxwellnie.javormio.common.java.type.TypeHandler;
import io.github.maxwellnie.javormio.core.translation.table.BaseMetaTableInfo;
import io.github.maxwellnie.javormio.core.translation.table.column.ColumnInfo;
import io.github.maxwellnie.javormio.flexible.sql.plugin.expression.SqlExpressionSupport;
import io.github.maxwellnie.javormio.flexible.sql.plugin.SqlBuilder;
import io.github.maxwellnie.javormio.flexible.sql.plugin.expression.ColumnNameHandler;

import java.util.Map;

/**
 * @author Maxwell Nie
 */
public class ValueGreater<E, T> extends SingleValueExpression<E, T> {
    public ValueGreater(ColumnInfo<E, T> firstColumnInfo, T value) {
        super(firstColumnInfo, value);
    }

    @Override
    public void applySql(SqlExpressionSupport sqlExpressionSupport, SqlBuilder sqlBuilder, Map<ColumnInfo, String> columnAliases, Map<BaseMetaTableInfo, String> tableAliases) {
        String columnName = ColumnNameHandler.getColumnName(firstColumnInfo, columnAliases, tableAliases);
        TypeHandler<T> typeHandler = firstColumnInfo.getTypeHandler();
        sqlExpressionSupport.greater(sqlBuilder, columnName, value, typeHandler, false);
    }
}
