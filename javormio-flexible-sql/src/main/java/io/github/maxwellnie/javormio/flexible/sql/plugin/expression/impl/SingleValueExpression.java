package io.github.maxwellnie.javormio.flexible.sql.plugin.expression.impl;

import io.github.maxwellnie.javormio.common.java.table.column.ColumnInfo;
import io.github.maxwellnie.javormio.flexible.sql.plugin.expression.Expression;
import io.github.maxwellnie.javormio.flexible.sql.plugin.expression.SqlExpressionSupport;

/**
 * @author Maxwell Nie
 */
public abstract class SingleValueExpression<S  extends SqlExpressionSupport, E, T> implements Expression<S> {
    protected ColumnInfo<E, T> firstColumnInfo;
    protected T value;

    public SingleValueExpression(ColumnInfo<E, T> firstColumnInfo, T value) {
        this.firstColumnInfo = firstColumnInfo;
        this.value = value;
    }
}
