package io.github.maxwellnie.javormio.flexible.sql.plugin.expression.impl;

import io.github.maxwellnie.javormio.core.translation.table.column.ColumnInfo;
import io.github.maxwellnie.javormio.flexible.sql.plugin.expression.Expression;

/**
 * @author Maxwell Nie
 */
public abstract class ValuesColumnExpression<E, T> implements Expression {
    protected ColumnInfo<E, T> columnInfo;
    protected T[] values;

    public ValuesColumnExpression(ColumnInfo<E, T> columnInfo, T[] values) {
        this.columnInfo = columnInfo;
        this.values = values;
    }
}
