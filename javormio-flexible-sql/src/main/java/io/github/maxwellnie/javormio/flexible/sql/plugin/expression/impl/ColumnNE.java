package io.github.maxwellnie.javormio.flexible.sql.plugin.expression.impl;

import io.github.maxwellnie.javormio.core.translation.table.BaseMetaTableInfo;
import io.github.maxwellnie.javormio.core.translation.table.column.ColumnInfo;
import io.github.maxwellnie.javormio.flexible.sql.plugin.expression.ColumnNameHandler;
import io.github.maxwellnie.javormio.flexible.sql.plugin.SqlBuilder;
import io.github.maxwellnie.javormio.flexible.sql.plugin.expression.SqlExpressionSupport;

import java.util.Map;

/**
 * @author Maxwell Nie
 */
public class ColumnNE extends DoubleColumnExpression {
    public ColumnNE(ColumnInfo firstColumnInfo, ColumnInfo secondColumnInfo) {
        super(firstColumnInfo, secondColumnInfo);
    }

    @Override
    public void applySql(SqlExpressionSupport sqlExpressionSupport, SqlBuilder sqlBuilder, Map<ColumnInfo, String> columnAliases, Map<BaseMetaTableInfo, String> tableAliases) {
        String leftColumnName = ColumnNameHandler.getColumnName(firstColumnInfo, columnAliases, tableAliases);
        String rightColumnName = ColumnNameHandler.getColumnName(secondColumnInfo, columnAliases, tableAliases);
        sqlExpressionSupport.notEq(sqlBuilder, leftColumnName, rightColumnName, null, true);
    }
}
