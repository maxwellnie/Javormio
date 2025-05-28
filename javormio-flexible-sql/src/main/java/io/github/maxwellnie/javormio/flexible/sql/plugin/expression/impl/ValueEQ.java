package io.github.maxwellnie.javormio.flexible.sql.plugin.expression.impl;

import io.github.maxwellnie.javormio.common.java.sql.ColumnEscapeKit;
import io.github.maxwellnie.javormio.common.java.table.BaseMetaTableInfo;
import io.github.maxwellnie.javormio.common.java.table.column.ColumnInfo;
import io.github.maxwellnie.javormio.common.java.type.TypeHandler;
import io.github.maxwellnie.javormio.core.translation.name.ColumnNameHandler;
import io.github.maxwellnie.javormio.core.translation.sql.SqlBuilder;
import io.github.maxwellnie.javormio.flexible.sql.plugin.expression.SqlExpressionSupport;

import java.util.Map;

/**
 * @author Maxwell Nie
 */
public class ValueEQ<S  extends SqlExpressionSupport, E, T> extends SingleValueExpression<S, E, T> {
    public ValueEQ(ColumnInfo<E, T> firstColumnInfo, T value) {
        super(firstColumnInfo, value);
    }

    @Override
    public void applySql(S sqlExpressionSupport, SqlBuilder sqlBuilder, Map<ColumnInfo, String> columnAliases,
                         Map<BaseMetaTableInfo, String> tableAliases, ColumnEscapeKit columnEscapeKit) {
        String columnName = ColumnNameHandler.getColumnName(firstColumnInfo, columnAliases, tableAliases, columnEscapeKit);
        TypeHandler<T> typeHandler = firstColumnInfo.getTypeHandler();
        sqlExpressionSupport.eq(sqlBuilder, columnName, value, typeHandler, false);
    }
}
