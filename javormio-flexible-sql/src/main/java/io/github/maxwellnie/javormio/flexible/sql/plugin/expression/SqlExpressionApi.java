package io.github.maxwellnie.javormio.flexible.sql.plugin.expression;

import io.github.maxwellnie.javormio.flexible.sql.plugin.table.ExpressionColumnInfo;

public interface SqlExpressionApi<E, T> {
    <E1, T1> Expression eq(ExpressionColumnInfo<E1, T1> expressionColumnInfo);

    Expression eq(T value);

    <E1, T1> Expression ne(ExpressionColumnInfo<E1, T1> expressionColumnInfo);

    Expression ne(T value);

    <E1, T1> Expression greater(ExpressionColumnInfo<E1, T1> expressionColumnInfo);

    Expression greater(T value);

    <E1, T1> Expression ge(ExpressionColumnInfo<E1, T1> expressionColumnInfo);

    Expression ge(T value);

    <E1, T1> Expression less(ExpressionColumnInfo<E1, T1> expressionColumnInfo);

    Expression less(T value);

    <E1, T1> Expression le(ExpressionColumnInfo<E1, T1> expressionColumnInfo);

    Expression le(T value);

    Expression between(T value1, T value2);

    Expression in(T[] values);

    Expression notIn(T[] values);

    Expression like(T value);

    Expression leftLike(T value);

    Expression rightLike(T value);

    Expression notLike(T value);

    Expression notLeftLike(T value);

    Expression notRightLike(T value);

    Expression is(T value);

    Expression isNot(T value);

    Expression not(T value);

    Expression isNull();

    Expression notNull();
}
