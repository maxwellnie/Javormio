package io.github.maxwellnie.javormio.core.database.sql.dynamic;

import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;

/**
 * The lambda expression of the where clause of the sql.
 *
 * @author Maxwell Nie
 */
public class LambdaWhere<E> extends Where<E> {
    ConditionsLogic<E, LambdaWhere<E>> lazyConditionLogic;


    public <R> ConditionsLogic<E, LambdaWhere<E>> eq(SerializableFunction<E, R> getter, R value) {
        String column = getMethodName(getter);
        super.eq(column, value);
        return lazyConditionLogic();
    }

    public <R> ConditionsLogic<E, LambdaWhere<E>> ne(SerializableFunction<E, R> getter, R value) {
        String column = getMethodName(getter);
        super.ne(column, value);
        return lazyConditionLogic();
    }

    public <R> ConditionsLogic<E, LambdaWhere<E>> gt(SerializableFunction<E, R> getter, R value) {
        String column = getMethodName(getter);
        super.gt(column, value);
        return lazyConditionLogic();
    }

    public <R> ConditionsLogic<E, LambdaWhere<E>> ge(SerializableFunction<E, R> getter, R value) {
        String column = getMethodName(getter);
        super.ge(column, value);
        return lazyConditionLogic();
    }

    public <R> ConditionsLogic<E, LambdaWhere<E>> lt(SerializableFunction<E, R> getter, R value) {
        String column = getMethodName(getter);
        super.lt(column, value);
        return lazyConditionLogic();
    }

    public <R> ConditionsLogic<E, LambdaWhere<E>> le(SerializableFunction<E, R> getter, R value) {
        String column = getMethodName(getter);
        super.le(column, value);
        return lazyConditionLogic();
    }

    public <R> ConditionsLogic<E, LambdaWhere<E>> like(SerializableFunction<E, R> getter, R value) {
        String column = getMethodName(getter);
        super.like(column, value);
        return lazyConditionLogic();
    }

    public <R> ConditionsLogic<E, LambdaWhere<E>> notLike(SerializableFunction<E, R> getter, R value) {
        String column = getMethodName(getter);
        super.notLike(column, value);
        return lazyConditionLogic();
    }

    public <R> ConditionsLogic<E, LambdaWhere<E>> between(SerializableFunction<E, R> getter, R value1, R value2) {
        String column = getMethodName(getter);
        super.between(column, value1, value2);
        return lazyConditionLogic();
    }

    public <R> ConditionsLogic<E, LambdaWhere<E>> notBetween(SerializableFunction<E, R> getter, R value1, R value2) {
        String column = getMethodName(getter);
        super.notBetween(column, value1, value2);
        return lazyConditionLogic();
    }

    public <R> ConditionsLogic<E, LambdaWhere<E>> isNull(SerializableFunction<E, R> getter) {
        String column = getMethodName(getter);
        super.isNull(column);
        return lazyConditionLogic();
    }

    public <R> ConditionsLogic<E, LambdaWhere<E>> isNotNull(SerializableFunction<E, R> getter) {
        String column = getMethodName(getter);
        super.isNotNull(column);
        return lazyConditionLogic();
    }

    public <R> ConditionsLogic<E, LambdaWhere<E>> notIn(SerializableFunction<E, R> getter, R[] values) {
        String column = getMethodName(getter);
        super.notIn(column, values);
        return lazyConditionLogic();
    }

    public <R> ConditionsLogic<E, LambdaWhere<E>> in(SerializableFunction<E, R> getter, R[] values) {
        String column = getMethodName(getter);
        super.in(column, values);
        return lazyConditionLogic();
    }

    private ConditionsLogic<E, LambdaWhere<E>> lazyConditionLogic() {
        return lazyConditionLogic == null ? lazyConditionLogic = new ConditionsLogic<>(this) : lazyConditionLogic;
    }

    private <R> String getMethodName(SerializableFunction<E, R> getter) {
        try {
            Method writeReplace = getter.getClass().getDeclaredMethod("writeReplace");
            writeReplace.setAccessible(true);
            SerializedLambda serializedLambda = (SerializedLambda) writeReplace.invoke(getter);
            String name = serializedLambda.getImplMethodName();
            return name.substring(3);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}
