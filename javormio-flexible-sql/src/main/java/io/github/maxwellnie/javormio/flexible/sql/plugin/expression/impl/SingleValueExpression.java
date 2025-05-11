package io.github.maxwellnie.javormio.flexible.sql.plugin.expression.impl;

import io.github.maxwellnie.javormio.core.translation.table.column.ColumnInfo;
import io.github.maxwellnie.javormio.flexible.sql.plugin.expression.Expression;

/**
 * @author Maxwell Nie
 */
public abstract class SingleValueExpression<E, T> implements Expression {
    protected ColumnInfo<E, T> firstColumnInfo;
    protected T value;

    public SingleValueExpression(ColumnInfo<E, T> firstColumnInfo, T value) {
        this.firstColumnInfo = firstColumnInfo;
        this.value = value;
    }
}
