package io.github.maxwellnie.javormio.flexible.sql.plugin.expression.impl;

import io.github.maxwellnie.javormio.common.java.table.BaseMetaTableInfo;
import io.github.maxwellnie.javormio.common.java.table.column.ColumnInfo;
import io.github.maxwellnie.javormio.core.translation.name.ColumnNameHandler;
import io.github.maxwellnie.javormio.core.translation.sql.SqlBuilder;
import io.github.maxwellnie.javormio.flexible.sql.plugin.expression.SqlExpressionSupport;
import io.github.maxwellnie.javormio.common.java.sql.ColumnEscapeKit;
import java.util.Map;

/**
 * @author Maxwell Nie
 */
public class ValueIn<S  extends SqlExpressionSupport, E, T> extends ValuesColumnExpression<S, E, T> {
    public ValueIn(ColumnInfo<E, T> columnInfo, T[] values) {
        super(columnInfo, values);
    }

    @Override
    public void applySql(S sqlExpressionSupport, SqlBuilder sqlBuilder, Map<ColumnInfo, String> columnAliases,
                         Map<BaseMetaTableInfo, String> tableAliases, ColumnEscapeKit columnEscapeKit) {
        String columnName = ColumnNameHandler.getColumnName(columnInfo, columnAliases, tableAliases, columnEscapeKit);
        sqlExpressionSupport.in(sqlBuilder, columnName, values,
                columnInfo.getTypeHandler());
    }
}