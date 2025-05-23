package io.github.maxwellnie.javormio.flexible.sql.plugin.expression.impl;

import io.github.maxwellnie.javormio.common.java.type.TypeHandler;
import io.github.maxwellnie.javormio.core.translation.table.BaseMetaTableInfo;
import io.github.maxwellnie.javormio.core.translation.table.column.ColumnInfo;
import io.github.maxwellnie.javormio.core.translation.sql.SqlBuilder;
import io.github.maxwellnie.javormio.flexible.sql.plugin.expression.ColumnNameHandler;
import io.github.maxwellnie.javormio.flexible.sql.plugin.expression.SqlExpressionSupport;

import java.util.Map;

/**
 * @author Maxwell Nie
 */
public class ValueNot<S  extends SqlExpressionSupport, E, T> extends SingleValueExpression<S, E, T> {
    public ValueNot(ColumnInfo<E, T> firstColumnInfo, T value) {
        super(firstColumnInfo, value);
    }

    @Override
    public void applySql(S sqlExpressionSupport, SqlBuilder sqlBuilder, Map<ColumnInfo, String> columnAliases, Map<BaseMetaTableInfo, String> tableAliases) {
        String columnName = ColumnNameHandler.getColumnName(firstColumnInfo, columnAliases, tableAliases);
        TypeHandler<T> typeHandler = firstColumnInfo.getTypeHandler();
        sqlExpressionSupport.not(sqlBuilder, columnName, value, typeHandler, false);
    }
}
