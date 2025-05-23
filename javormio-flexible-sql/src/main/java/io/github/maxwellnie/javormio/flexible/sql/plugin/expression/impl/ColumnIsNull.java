package io.github.maxwellnie.javormio.flexible.sql.plugin.expression.impl;

import io.github.maxwellnie.javormio.core.translation.table.BaseMetaTableInfo;
import io.github.maxwellnie.javormio.core.translation.table.column.ColumnInfo;
import io.github.maxwellnie.javormio.core.translation.sql.SqlBuilder;
import io.github.maxwellnie.javormio.flexible.sql.plugin.expression.ColumnNameHandler;
import io.github.maxwellnie.javormio.flexible.sql.plugin.expression.SqlExpressionSupport;

import java.util.Map;

/**
 * @author Maxwell Nie
 */
public class ColumnIsNull<T extends SqlExpressionSupport> extends SingleColumnExpression<T> {
    public ColumnIsNull(ColumnInfo columnInfo) {
        super(columnInfo);
    }

    @Override
    public void applySql(T sqlExpressionSupport, SqlBuilder sqlBuilder, Map<ColumnInfo, String> columnAliases, Map<BaseMetaTableInfo, String> tableAliases) {
        String columnName = ColumnNameHandler.getColumnName(columnInfo, columnAliases, tableAliases);
        sqlExpressionSupport.isNull(sqlBuilder, columnName);
    }
}
