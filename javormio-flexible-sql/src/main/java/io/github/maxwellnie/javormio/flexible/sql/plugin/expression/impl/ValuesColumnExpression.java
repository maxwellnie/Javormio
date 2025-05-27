package io.github.maxwellnie.javormio.flexible.sql.plugin.expression.impl;

//import io.github.maxwellnie.javormio.core.translation.table.column.ColumnInfo;
import io.github.maxwellnie.javormio.flexible.sql.plugin.expression.Expression;
import io.github.maxwellnie.javormio.common.java.table.column.ColumnInfo;
import io.github.maxwellnie.javormio.common.java.table.BaseMetaTableInfo;
import io.github.maxwellnie.javormio.flexible.sql.plugin.expression.SqlExpressionSupport;

/**
 * @author Maxwell Nie
 */
public abstract class ValuesColumnExpression<S extends SqlExpressionSupport, E, T> implements Expression<S> {
    protected ColumnInfo<E, T> columnInfo;
    protected T[] values;

    public ValuesColumnExpression(ColumnInfo<E, T> columnInfo, T[] values) {
        this.columnInfo = columnInfo;
        this.values = values;
    }
}
