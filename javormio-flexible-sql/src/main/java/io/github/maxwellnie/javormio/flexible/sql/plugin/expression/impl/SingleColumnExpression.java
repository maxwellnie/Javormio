package io.github.maxwellnie.javormio.flexible.sql.plugin.expression.impl;

import io.github.maxwellnie.javormio.core.translation.table.column.ColumnInfo;
import io.github.maxwellnie.javormio.flexible.sql.plugin.expression.Expression;

/**
 * @author Maxwell Nie
 */
public abstract class SingleColumnExpression implements Expression {
    protected ColumnInfo columnInfo;

    public SingleColumnExpression(ColumnInfo columnInfo) {
        this.columnInfo = columnInfo;
    }
}
