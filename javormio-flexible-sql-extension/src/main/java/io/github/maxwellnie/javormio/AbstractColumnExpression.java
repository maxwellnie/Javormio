package io.github.maxwellnie.javormio;

import io.github.maxwellnie.javormio.common.java.table.column.ColumnInfo;

/**
 * @author Maxwell Nie
 */
public abstract class AbstractColumnExpression <E, T>{
    protected final ColumnInfo<E, T> columnInfo;

    public AbstractColumnExpression(ColumnInfo<E, T> columnInfo) {
        this.columnInfo = columnInfo;
    }

    public ColumnInfo<E, T> getColumnInfo() {
        return columnInfo;
    }
}
