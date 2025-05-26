package io.github.maxwellnie.javormio.flexible.sql.plugin.expression.impl;

import io.github.maxwellnie.javormio.common.java.table.column.ColumnInfo;
import io.github.maxwellnie.javormio.flexible.sql.plugin.expression.Expression;
import io.github.maxwellnie.javormio.flexible.sql.plugin.expression.SqlExpressionSupport;

/**
 * @author Maxwell Nie
 */
public abstract class SingleColumnExpression<T extends SqlExpressionSupport> implements Expression<T> {
    protected ColumnInfo columnInfo;

    public SingleColumnExpression(ColumnInfo columnInfo) {
        this.columnInfo = columnInfo;
    }
}
