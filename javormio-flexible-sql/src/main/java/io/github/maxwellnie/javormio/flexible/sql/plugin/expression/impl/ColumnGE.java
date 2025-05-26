package io.github.maxwellnie.javormio.flexible.sql.plugin.expression.impl;

import io.github.maxwellnie.javormio.common.java.table.BaseMetaTableInfo;
import io.github.maxwellnie.javormio.common.java.table.column.ColumnInfo;
import io.github.maxwellnie.javormio.core.translation.sql.SqlBuilder;
import io.github.maxwellnie.javormio.flexible.sql.plugin.expression.ColumnNameHandler;
import io.github.maxwellnie.javormio.flexible.sql.plugin.expression.SqlExpressionSupport;

import java.util.Map;

/**
 * @author Maxwell Nie
 */
public class ColumnGE<T extends SqlExpressionSupport> extends DoubleColumnExpression<T> {
    public ColumnGE(ColumnInfo firstColumnInfo, ColumnInfo secondColumnInfo) {
        super(firstColumnInfo, secondColumnInfo);
    }

    @Override
    public void applySql(T sqlExpressionSupport, SqlBuilder sqlBuilder, Map<ColumnInfo, String> columnAliases, Map<BaseMetaTableInfo, String> tableAliases) {
        String leftColumnName = ColumnNameHandler.getColumnName(firstColumnInfo, columnAliases, tableAliases);
        String rightColumnName = ColumnNameHandler.getColumnName(secondColumnInfo, columnAliases, tableAliases);
        sqlExpressionSupport.greaterEqual(sqlBuilder, leftColumnName, rightColumnName, null, true);
    }
}
