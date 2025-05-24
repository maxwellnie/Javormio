package io.github.maxwellnie.javormio.flexible.sql.plugin.table;

import io.github.maxwellnie.javormio.AbstractColumnExpression;
import io.github.maxwellnie.javormio.core.translation.table.column.ColumnInfo;
import io.github.maxwellnie.javormio.flexible.sql.plugin.expression.Expression;
import io.github.maxwellnie.javormio.flexible.sql.plugin.expression.SqlExpressionApi;
import io.github.maxwellnie.javormio.flexible.sql.plugin.expression.SqlExpressionSupport;
import io.github.maxwellnie.javormio.flexible.sql.plugin.expression.impl.*;

/**
 * @author Maxwell Nie
 */
public class ExpressionColumnInfo<S extends SqlExpressionSupport, E, T> extends AbstractColumnExpression<E, T> implements SqlExpressionApi<S, E, T> {

    public ExpressionColumnInfo(ColumnInfo<E, T> columnInfo) {
        super(columnInfo);
    }

    @Override
    public <E1, T1> Expression<S> eq(ExpressionColumnInfo<? extends SqlExpressionSupport, E1, T1> expressionColumnInfo) {
        return new ColumnEQ<>(columnInfo, expressionColumnInfo.columnInfo);
    }

    @Override
    public Expression<S> eq(T value) {
        return new ValueEQ<>(columnInfo, value);
    }

    @Override
    public <E1, T1> Expression<S> ne(ExpressionColumnInfo<? extends SqlExpressionSupport, E1, T1> expressionColumnInfo) {
        return new ColumnNE<>(columnInfo, expressionColumnInfo.columnInfo);
    }

    @Override
    public Expression<S> ne(T value) {
        return new ValueNE<>(columnInfo, value);
    }

    @Override
    public <E1, T1> Expression<S> greater(ExpressionColumnInfo<? extends SqlExpressionSupport, E1, T1> expressionColumnInfo) {
        return new ColumnGreater<>(columnInfo, expressionColumnInfo.columnInfo);
    }

    @Override
    public Expression<S> greater(T value) {
        return new ValueGreater<>(columnInfo, value);
    }

    @Override
    public <E1, T1> Expression<S> ge(ExpressionColumnInfo<? extends SqlExpressionSupport, E1, T1> expressionColumnInfo) {
        return new ColumnGE<>(columnInfo, expressionColumnInfo.columnInfo);
    }

    @Override
    public Expression<S> ge(T value) {
        return new ValueGE<>(columnInfo, value);
    }

    @Override
    public <E1, T1> Expression<S> less(ExpressionColumnInfo<? extends SqlExpressionSupport, E1, T1> expressionColumnInfo) {
        return new ColumnLess<>(columnInfo, expressionColumnInfo.columnInfo);
    }

    @Override
    public Expression<S> less(T value) {
        return new ValueLess<>(columnInfo, value);
    }

    @Override
    public <E1, T1> Expression<S> le(ExpressionColumnInfo<? extends SqlExpressionSupport, E1, T1> expressionColumnInfo) {
        return new ColumnLess<>(columnInfo, expressionColumnInfo.columnInfo);
    }

    @Override
    public Expression<S> le(T value) {
        return new ValueLE<>(columnInfo, value);
    }

    @Override
    public Expression<S> between(T value1, T value2) {
        return new ValueBetween<>(columnInfo, value1, value2);
    }

    @Override
    public Expression<S> in(T[] values) {
        return new ValueIn<>(columnInfo, values);
    }

    @Override
    public Expression<S> notIn(T[] values) {
        return new ValueNotIn<>(columnInfo, values);
    }

    @Override
    public Expression<S> like(T value) {
        return new ValueLike<>(columnInfo, value, 0);
    }

    @Override
    public Expression<S> notLike(T value) {
        return new ValueNotLike<>(columnInfo, value, 0);
    }

    @Override
    public Expression<S> is(T value) {
        return new ValueIs<>(columnInfo, value);
    }

    @Override
    public Expression<S> isNot(T value) {
        return new ValueIsNot<>(columnInfo, value);
    }

    @Override
    public Expression<S> not(T value) {
        return new ValueNot<>(columnInfo, value);
    }

    @Override
    public Expression<S> isNull() {
        return new ColumnIsNull<>(columnInfo);
    }

    @Override
    public Expression<S> notNull() {
        return new ColumnNotNull<>(columnInfo);
    }

    @Override
    public Expression<S> leftLike(T value) {
        return new ValueLike<>(columnInfo, value, 1);
    }

    @Override
    public Expression<S> rightLike(T value) {
        return new ValueLike<>(columnInfo, value, 2);
    }

    @Override
    public Expression<S> notLeftLike(T value) {
        return new ValueNotLike<>(columnInfo, value, 1);
    }

    @Override
    public Expression<S> notRightLike(T value) {
        return new ValueNotLike<>(columnInfo, value, 2);
    }
}
