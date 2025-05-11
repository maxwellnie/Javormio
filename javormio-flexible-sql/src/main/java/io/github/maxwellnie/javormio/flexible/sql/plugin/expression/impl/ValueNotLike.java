package io.github.maxwellnie.javormio.flexible.sql.plugin.expression.impl;

import io.github.maxwellnie.javormio.common.java.type.StringTypeHandler;
import io.github.maxwellnie.javormio.core.translation.table.BaseMetaTableInfo;
import io.github.maxwellnie.javormio.core.translation.table.column.ColumnInfo;
import io.github.maxwellnie.javormio.flexible.sql.plugin.expression.ColumnNameHandler;
import io.github.maxwellnie.javormio.flexible.sql.plugin.expression.SqlExpressionSupport;
import io.github.maxwellnie.javormio.flexible.sql.plugin.SqlBuilder;

import java.util.Map;

/**
 * @author Maxwell Nie
 */
public class ValueNotLike<E, T> extends LikeExpression<E, T> {

    public ValueNotLike(ColumnInfo<E, T> firstColumnInfo, T value, int mode) {
        super(firstColumnInfo, value, mode);
    }

    @Override
    public void applySql(SqlExpressionSupport sqlExpressionSupport, SqlBuilder sqlBuilder, Map<ColumnInfo, String> columnAliases, Map<BaseMetaTableInfo, String> tableAliases) {
        String columnName = ColumnNameHandler.getColumnName(firstColumnInfo, columnAliases, tableAliases);
        sqlExpressionSupport.notLike(sqlBuilder, columnName, value, StringTypeHandler.INSTANCE, mode);
    }
}
