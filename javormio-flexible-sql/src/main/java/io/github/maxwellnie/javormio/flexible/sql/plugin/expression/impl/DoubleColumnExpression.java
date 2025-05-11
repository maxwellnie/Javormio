package io.github.maxwellnie.javormio.flexible.sql.plugin.expression.impl;

import io.github.maxwellnie.javormio.core.translation.table.column.ColumnInfo;
import io.github.maxwellnie.javormio.flexible.sql.plugin.expression.Expression;

/**
 * @author Maxwell Nie
 */
public abstract class DoubleColumnExpression implements Expression {
    protected ColumnInfo firstColumnInfo;
    protected ColumnInfo secondColumnInfo;

    public DoubleColumnExpression(ColumnInfo firstColumnInfo, ColumnInfo secondColumnInfo) {
        this.firstColumnInfo = firstColumnInfo;
        this.secondColumnInfo = secondColumnInfo;
    }
}
