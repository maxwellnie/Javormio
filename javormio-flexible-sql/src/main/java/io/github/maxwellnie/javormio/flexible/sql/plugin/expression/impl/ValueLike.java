package io.github.maxwellnie.javormio.flexible.sql.plugin.expression.impl;

import io.github.maxwellnie.javormio.common.java.table.BaseMetaTableInfo;
import io.github.maxwellnie.javormio.common.java.table.column.ColumnInfo;
import io.github.maxwellnie.javormio.common.java.type.StringTypeHandler;
import io.github.maxwellnie.javormio.core.translation.name.ColumnNameHandler;
import io.github.maxwellnie.javormio.core.translation.sql.SqlBuilder;
import io.github.maxwellnie.javormio.flexible.sql.plugin.expression.SqlExpressionSupport;
import io.github.maxwellnie.javormio.common.java.sql.ColumnEscapeKit;
import java.util.Map;

/**
 * @author Maxwell Nie
 */
public class ValueLike<S  extends SqlExpressionSupport, E, T> extends LikeExpression<S, E, T> {

    public ValueLike(ColumnInfo<E, T> firstColumnInfo, T value, int mode) {
        super(firstColumnInfo, value, mode);
    }

    @Override
    public void applySql(S sqlExpressionSupport, SqlBuilder sqlBuilder, Map<ColumnInfo, String> columnAliases, Map<BaseMetaTableInfo, String> tableAliases, ColumnEscapeKit columnEscapeKit) {
        String columnName = ColumnNameHandler.getColumnName(firstColumnInfo, columnAliases, tableAliases, columnEscapeKit);
        sqlExpressionSupport.like(sqlBuilder, columnName, value, StringTypeHandler.INSTANCE, mode);
    }
}
