package io.github.maxwellnie.javormio.flexible.sql.plugin.expression.impl;

import io.github.maxwellnie.javormio.common.java.table.column.ColumnInfo;
//import io.github.maxwellnie.javormio.core.translation.table.column.ColumnInfo;
import io.github.maxwellnie.javormio.flexible.sql.plugin.expression.Expression;
import io.github.maxwellnie.javormio.flexible.sql.plugin.expression.SqlExpressionSupport;

/**
 * @author Maxwell Nie
 */
public abstract class DoubleColumnExpression<T extends SqlExpressionSupport> implements Expression<T> {
    protected ColumnInfo firstColumnInfo;
    protected ColumnInfo secondColumnInfo;

    public DoubleColumnExpression(ColumnInfo firstColumnInfo, ColumnInfo secondColumnInfo) {
        this.firstColumnInfo = firstColumnInfo;
        this.secondColumnInfo = secondColumnInfo;
    }
}
