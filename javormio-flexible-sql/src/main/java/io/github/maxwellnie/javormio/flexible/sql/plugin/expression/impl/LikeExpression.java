package io.github.maxwellnie.javormio.flexible.sql.plugin.expression.impl;

import io.github.maxwellnie.javormio.core.translation.table.column.ColumnInfo;

/**
 * @author Maxwell Nie
 */
public abstract class LikeExpression<E, T> extends SingleValueExpression<E, T>{
    protected int mode;

    public LikeExpression(ColumnInfo<E, T> firstColumnInfo, T value, int mode) {
        super(firstColumnInfo, value);
        this.mode = mode;
    }
}
