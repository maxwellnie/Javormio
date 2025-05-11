package io.github.maxwellnie.javormio.flexible.sql.plugin.table;

import io.github.maxwellnie.javormio.core.translation.table.column.ColumnInfo;
import io.github.maxwellnie.javormio.flexible.sql.plugin.expression.Expression;
import io.github.maxwellnie.javormio.flexible.sql.plugin.expression.SqlExpressionApi;
import io.github.maxwellnie.javormio.flexible.sql.plugin.expression.impl.*;

/**
 * @author Maxwell Nie
 */
public class ExpressionColumnInfo<E, T> implements SqlExpressionApi<E, T> {
    ColumnInfo<E, T> columnInfo;

    public ExpressionColumnInfo(ColumnInfo<E, T> columnInfo) {
        this.columnInfo = columnInfo;
    }

    @Override
    public <E1, T1> Expression eq(ExpressionColumnInfo<E1, T1> expressionColumnInfo) {
        return new ColumnEQ(columnInfo, expressionColumnInfo.columnInfo);
    }

    @Override
    public Expression eq(T value) {
        return new ValueEQ<>(columnInfo, value);
    }

    @Override
    public <E1, T1> Expression ne(ExpressionColumnInfo<E1, T1> expressionColumnInfo) {
        return new ColumnNE(columnInfo, expressionColumnInfo.columnInfo);
    }

    @Override
    public Expression ne(T value) {
        return new ValueNE<>(columnInfo, value);
    }

    @Override
    public <E1, T1> Expression greater(ExpressionColumnInfo<E1, T1> expressionColumnInfo) {
        return new ColumnGreater(columnInfo, expressionColumnInfo.columnInfo);
    }

    @Override
    public Expression greater(T value) {
        return new ValueGreater<>(columnInfo, value);
    }

    @Override
    public <E1, T1> Expression ge(ExpressionColumnInfo<E1, T1> expressionColumnInfo) {
        return new ColumnGE(columnInfo, expressionColumnInfo.columnInfo);
    }

    @Override
    public Expression ge(T value) {
        return new ValueGE<>(columnInfo, value);
    }

    @Override
    public <E1, T1> Expression less(ExpressionColumnInfo<E1, T1> expressionColumnInfo) {
        return new ColumnLess(columnInfo, expressionColumnInfo.columnInfo);
    }

    @Override
    public Expression less(T value) {
        return new ValueLess<>(columnInfo, value);
    }

    @Override
    public <E1, T1> Expression le(ExpressionColumnInfo<E1, T1> expressionColumnInfo) {
        return new ColumnLess(columnInfo, expressionColumnInfo.columnInfo);
    }

    @Override
    public Expression le(T value) {
        return new ValueLE<>(columnInfo, value);
    }

    @Override
    public Expression between(T value1, T value2) {
        return new ValueBetween<>(columnInfo, value1, value2);
    }

    @Override
    public Expression in(T[] values) {
        return new ValueIn<>(columnInfo, values);
    }

    @Override
    public Expression notIn(T[] values) {
        return new ValueNotIn<>(columnInfo, values);
    }

    @Override
    public Expression like(T value) {
        return new ValueLike<>(columnInfo, value, 0);
    }

    @Override
    public Expression notLike(T value) {
        return new ValueNotLike<>(columnInfo, value, 0);
    }

    @Override
    public Expression is(T value) {
        return new ValueIs<>(columnInfo, value);
    }

    @Override
    public Expression isNot(T value) {
        return new ValueIsNot<>(columnInfo, value);
    }

    @Override
    public Expression not(T value) {
        return new ValueNot<>(columnInfo, value);
    }

    @Override
    public Expression isNull() {
        return new ColumnIsNull(columnInfo);
    }

    @Override
    public Expression notNull() {
        return new ColumnNotNull(columnInfo);
    }

    @Override
    public Expression leftLike(T value) {
        return new ValueLike<>(columnInfo, value, 1);
    }

    @Override
    public Expression rightLike(T value) {
        return new ValueLike<>(columnInfo, value, 2);
    }

    @Override
    public Expression notLeftLike(T value) {
        return new ValueNotLike<>(columnInfo, value, 1);
    }

    @Override
    public Expression notRightLike(T value) {
        return new ValueNotLike<>(columnInfo, value, 2);
    }

    public ColumnInfo<E, T> getColumnInfo() {
        return columnInfo;
    }
}
