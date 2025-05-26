package io.github.maxwellnie.javormio.flexible.sql.plugin.expression.impl;

import io.github.maxwellnie.javormio.common.java.table.column.ColumnInfo;
import io.github.maxwellnie.javormio.flexible.sql.plugin.expression.SqlExpressionSupport;

/**
 * @author Maxwell Nie
 */
public abstract class LikeExpression<S extends SqlExpressionSupport, E, T> extends SingleValueExpression<S, E, T> {
    protected int mode;

    public LikeExpression(ColumnInfo<E, T> firstColumnInfo, T value, int mode) {
        super(firstColumnInfo, value);
        this.mode = mode;
    }
}
