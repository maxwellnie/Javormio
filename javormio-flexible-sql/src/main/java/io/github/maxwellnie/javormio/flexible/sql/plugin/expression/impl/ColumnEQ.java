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
public class ColumnEQ<T extends SqlExpressionSupport> extends DoubleColumnExpression<T> {
    public ColumnEQ(ColumnInfo firstColumnInfo, ColumnInfo secondColumnInfo) {
        super(firstColumnInfo, secondColumnInfo);
    }

    @Override
    public void applySql(T sqlExpressionSupport, SqlBuilder sqlBuilder, Map<ColumnInfo, String> columnAliases, Map<BaseMetaTableInfo, String> tableAliases, ColumnEscapeKit columnEscapeKit) {
        String leftColumnName = ColumnNameHandler.getColumnName(firstColumnInfo, columnAliases, tableAliases, columnEscapeKit);
        String rightColumnName = ColumnNameHandler.getColumnName(secondColumnInfo, columnAliases, tableAliases,  columnEscapeKit);
        sqlExpressionSupport.eq(sqlBuilder, leftColumnName, rightColumnName, null, true);
    }
}
