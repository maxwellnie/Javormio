package io.github.maxwellnie.javormio.flexible.sql.plugin.expression.impl;

import io.github.maxwellnie.javormio.common.java.table.column.ColumnInfo;
import io.github.maxwellnie.javormio.flexible.sql.plugin.expression.Expression;
import io.github.maxwellnie.javormio.flexible.sql.plugin.expression.SqlExpressionSupport;

/**
 * @author Maxwell Nie
 */
public abstract class DoubleValueExpression<S extends SqlExpressionSupport, E, T> implements Expression<S> {
    protected ColumnInfo<E, T> firstColumnInfo;
    protected T firstValue;
    protected T secondValue;

    public DoubleValueExpression(ColumnInfo<E, T> firstColumnInfo, T firstValue, T secondValue) {
        this.firstColumnInfo = firstColumnInfo;
        this.firstValue = firstValue;
        this.secondValue = secondValue;
    }
}
