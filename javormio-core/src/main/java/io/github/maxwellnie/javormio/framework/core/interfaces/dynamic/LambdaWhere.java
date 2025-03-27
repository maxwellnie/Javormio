package io.github.maxwellnie.javormio.framework.core.interfaces.dynamic;

import io.github.maxwellnie.javormio.framework.common.utils.ReflectionUtils;

/**
 * The lambda expression of the where clause of the sql.
 *
 * @author Maxwell Nie
 */
public class LambdaWhere<E> extends Where<E> {
    ConditionsLogic<E, LambdaWhere<E>> lazyConditionLogic;

    public static void main(String[] args) throws ReflectiveOperationException {
        new LambdaWhere<User>()
                .eq(User::getUser_id, 1)
                .ok();
        // SELECT * FROM (SELECT user_id FROM tb_user WHERE user_id > 0) WHERE user_id = 1;
    }

    public <R> ConditionsLogic<E, LambdaWhere<E>> eq(SerializableFunction<E, R> getter, R value) throws ReflectiveOperationException {
        String column = getMethodName(getter);
        super.eq(column, value);
        return lazyConditionLogic();
    }

    public <R> ConditionsLogic<E, LambdaWhere<E>> ne(SerializableFunction<E, R> getter, R value) throws ReflectiveOperationException {
        String column = getMethodName(getter);
        super.ne(column, value);
        return lazyConditionLogic();
    }

    public <R> ConditionsLogic<E, LambdaWhere<E>> gt(SerializableFunction<E, R> getter, R value) throws ReflectiveOperationException {
        String column = getMethodName(getter);
        super.gt(column, value);
        return lazyConditionLogic();
    }

    public <R> ConditionsLogic<E, LambdaWhere<E>> ge(SerializableFunction<E, R> getter, R value) throws ReflectiveOperationException {
        String column = getMethodName(getter);
        super.ge(column, value);
        return lazyConditionLogic();
    }

    public <R> ConditionsLogic<E, LambdaWhere<E>> lt(SerializableFunction<E, R> getter, R value) throws ReflectiveOperationException {
        String column = getMethodName(getter);
        super.lt(column, value);
        return lazyConditionLogic();
    }

    public <R> ConditionsLogic<E, LambdaWhere<E>> le(SerializableFunction<E, R> getter, R value) throws ReflectiveOperationException {
        String column = getMethodName(getter);
        super.le(column, value);
        return lazyConditionLogic();
    }

    public <R> ConditionsLogic<E, LambdaWhere<E>> like(SerializableFunction<E, R> getter, R value) throws ReflectiveOperationException {
        String column = getMethodName(getter);
        super.like(column, value);
        return lazyConditionLogic();
    }

    public <R> ConditionsLogic<E, LambdaWhere<E>> notLike(SerializableFunction<E, R> getter, R value) throws ReflectiveOperationException {
        String column = getMethodName(getter);
        super.notLike(column, value);
        return lazyConditionLogic();
    }

    public <R> ConditionsLogic<E, LambdaWhere<E>> between(SerializableFunction<E, R> getter, R value1, R value2) throws ReflectiveOperationException {
        String column = getMethodName(getter);
        super.between(column, value1, value2);
        return lazyConditionLogic();
    }

    public <R> ConditionsLogic<E, LambdaWhere<E>> notBetween(SerializableFunction<E, R> getter, R value1, R value2) throws ReflectiveOperationException {
        String column = getMethodName(getter);
        super.notBetween(column, value1, value2);
        return lazyConditionLogic();
    }

    public <R> ConditionsLogic<E, LambdaWhere<E>> isNull(SerializableFunction<E, R> getter) throws ReflectiveOperationException {
        String column = getMethodName(getter);
        super.isNull(column);
        return lazyConditionLogic();
    }

    public <R> ConditionsLogic<E, LambdaWhere<E>> isNotNull(SerializableFunction<E, R> getter) throws ReflectiveOperationException {
        String column = getMethodName(getter);
        super.isNotNull(column);
        return lazyConditionLogic();
    }

    public <R> ConditionsLogic<E, LambdaWhere<E>> notIn(SerializableFunction<E, R> getter, R[] values) throws ReflectiveOperationException {
        String column = getMethodName(getter);
        super.notIn(column, values);
        return lazyConditionLogic();
    }

    public <R> ConditionsLogic<E, LambdaWhere<E>> in(SerializableFunction<E, R> getter, R[] values) throws ReflectiveOperationException {
        String column = getMethodName(getter);
        super.in(column, values);
        return lazyConditionLogic();
    }

    private ConditionsLogic<E, LambdaWhere<E>> lazyConditionLogic() {
        return lazyConditionLogic == null ? lazyConditionLogic = new ConditionsLogic<>(this) : lazyConditionLogic;
    }

    private <R> String getMethodName(SerializableFunction<E, R> getter) throws ReflectiveOperationException {
        String name = ReflectionUtils.getMethodName(getter);
        return name.substring(3);
    }

    public static class User {
        int user_id;

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }
    }
}
