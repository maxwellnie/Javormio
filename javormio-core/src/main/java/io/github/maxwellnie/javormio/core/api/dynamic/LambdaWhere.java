package io.github.maxwellnie.javormio.core.api.dynamic;

import io.github.maxwellnie.javormio.common.java.reflect.method.SerializableFunction;
import io.github.maxwellnie.javormio.common.utils.ReflectionUtils;
import io.github.maxwellnie.javormio.core.api.dynamic.condition.InvalidColumnException;
import io.github.maxwellnie.javormio.core.translation.table.TableInfo;
import io.github.maxwellnie.javormio.common.java.reflect.method.Action;

/**
 * The lambda expression of the where clause of the sql.
 *
 * @author Maxwell Nie
 */
public class LambdaWhere<E, P> extends Where<E, P> {
    ConditionsLogic<E, P, LambdaWhere<E, P>> lazyConditionLogic;

    public LambdaWhere(P p, Action<P, SqlFragment, P> callback, TableInfo tableInfo) {
        super(p, callback, tableInfo);
    }

    public <R> ConditionsLogic<E, P, LambdaWhere<E, P>> eq(SerializableFunction<E, R> getter, R value) {
        String column = getMethodName(getter);
        super.eq(column, value);
        return lazyConditionLogic();
    }

    public <R> ConditionsLogic<E, P, LambdaWhere<E, P>> ne(SerializableFunction<E, R> getter, R value) {
        String column = getMethodName(getter);
        super.ne(column, value);
        return lazyConditionLogic();
    }

    public <R> ConditionsLogic<E, P, LambdaWhere<E, P>> gt(SerializableFunction<E, R> getter, R value) {
        String column = getMethodName(getter);
        super.gt(column, value);
        return lazyConditionLogic();
    }

    public <R> ConditionsLogic<E, P, LambdaWhere<E, P>> ge(SerializableFunction<E, R> getter, R value) {
        String column = getMethodName(getter);
        super.ge(column, value);
        return lazyConditionLogic();
    }

    public <R> ConditionsLogic<E, P, LambdaWhere<E, P>> lt(SerializableFunction<E, R> getter, R value) {
        String column = getMethodName(getter);
        super.lt(column, value);
        return lazyConditionLogic();
    }

    public <R> ConditionsLogic<E, P, LambdaWhere<E, P>> le(SerializableFunction<E, R> getter, R value) {
        String column = getMethodName(getter);
        super.le(column, value);
        return lazyConditionLogic();
    }

    public <R> ConditionsLogic<E, P, LambdaWhere<E, P>> like(SerializableFunction<E, R> getter, R value) {
        String column = getMethodName(getter);
        super.like(column, value);
        return lazyConditionLogic();
    }

    public <R> ConditionsLogic<E, P, LambdaWhere<E, P>> notLike(SerializableFunction<E, R> getter, R value) {
        String column = getMethodName(getter);
        super.notLike(column, value);
        return lazyConditionLogic();
    }

    public <R> ConditionsLogic<E, P, LambdaWhere<E, P>> between(SerializableFunction<E, R> getter, R value1, R value2) {
        String column = getMethodName(getter);
        super.between(column, value1, value2);
        return lazyConditionLogic();
    }

    public <R> ConditionsLogic<E, P, LambdaWhere<E, P>> notBetween(SerializableFunction<E, R> getter, R value1, R value2) {
        String column = getMethodName(getter);
        super.notBetween(column, value1, value2);
        return lazyConditionLogic();
    }

    public <R> ConditionsLogic<E, P, LambdaWhere<E, P>> isNull(SerializableFunction<E, R> getter) {
        String column = getMethodName(getter);
        super.isNull(column);
        return lazyConditionLogic();
    }

    public <R> ConditionsLogic<E, P, LambdaWhere<E, P>> isNotNull(SerializableFunction<E, R> getter) {
        String column = getMethodName(getter);
        super.isNotNull(column);
        return lazyConditionLogic();
    }

    public <R> ConditionsLogic<E, P, LambdaWhere<E, P>> notIn(SerializableFunction<E, R> getter, R[] values) {
        String column = getMethodName(getter);
        super.notIn(column, values);
        return lazyConditionLogic();
    }

    public <R> ConditionsLogic<E, P, LambdaWhere<E, P>> in(SerializableFunction<E, R> getter, R[] values) {
        String column = getMethodName(getter);
        super.in(column, values);
        return lazyConditionLogic();
    }

    private ConditionsLogic<E, P, LambdaWhere<E, P>> lazyConditionLogic() {
        return lazyConditionLogic == null ? lazyConditionLogic = new ConditionsLogic<>(this) : lazyConditionLogic;
    }

    private <R> String getMethodName(SerializableFunction<E, R> getter) {
        String name = null;
        try {
            name = ReflectionUtils.getMethodName(getter);
        } catch (ReflectiveOperationException e) {
            throw new InvalidColumnException("There is no SerializedLambda object for which the lambda expression was found. Please check your JDK version and report back to us.", e);
        }
        name = name.substring(3);
        return name;
    }
}
