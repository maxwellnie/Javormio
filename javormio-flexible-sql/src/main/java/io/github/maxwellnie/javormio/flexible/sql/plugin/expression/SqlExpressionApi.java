package io.github.maxwellnie.javormio.flexible.sql.plugin.expression;

import io.github.maxwellnie.javormio.flexible.sql.plugin.table.ExpressionColumnInfo;

public interface SqlExpressionApi<S extends SqlExpressionSupport, E, T> {
    <E1, T1> Expression<S> eq(ExpressionColumnInfo<? extends SqlExpressionSupport, E1, T1> expressionColumnInfo);

    Expression<S> eq(T value);

    <E1, T1> Expression<S> ne(ExpressionColumnInfo<? extends SqlExpressionSupport, E1, T1> expressionColumnInfo);

    Expression<S> ne(T value);

    <E1, T1> Expression<S> greater(ExpressionColumnInfo<? extends SqlExpressionSupport, E1, T1> expressionColumnInfo);

    Expression<S> greater(T value);

    <E1, T1> Expression<S> ge(ExpressionColumnInfo<? extends SqlExpressionSupport, E1, T1> expressionColumnInfo);

    Expression<S> ge(T value);

    <E1, T1> Expression<S> less(ExpressionColumnInfo<? extends SqlExpressionSupport, E1, T1> expressionColumnInfo);

    Expression<S> less(T value);

    <E1, T1> Expression<S> le(ExpressionColumnInfo<? extends SqlExpressionSupport, E1, T1> expressionColumnInfo);

    Expression<S> le(T value);

    Expression<S> between(T value1, T value2);

    Expression<S> in(T[] values);

    Expression<S> notIn(T[] values);

    Expression<S> like(T value);

    Expression<S> leftLike(T value);

    Expression<S> rightLike(T value);

    Expression<S> notLike(T value);

    Expression<S> notLeftLike(T value);

    Expression<S> notRightLike(T value);

    Expression<S> is(T value);

    Expression<S> isNot(T value);

    Expression<S> not(T value);

    Expression<S> isNull();

    Expression<S> notNull();
}
