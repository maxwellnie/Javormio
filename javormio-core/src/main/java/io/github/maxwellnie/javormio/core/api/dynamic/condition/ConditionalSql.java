package io.github.maxwellnie.javormio.core.api.dynamic.condition;

import io.github.maxwellnie.javormio.common.annotation.Incompletely;
import io.github.maxwellnie.javormio.common.java.reflect.method.SerializableFunction;
import io.github.maxwellnie.javormio.common.java.reflect.method.Action;

import java.util.function.Function;

import static io.github.maxwellnie.javormio.common.utils.ReflectionUtils.getMethodName;


/**
 * 这是一个条件修改SQL的类，暂时没有加入Cache来缓存生产的代理方法
 *
 * @author Maxwell Nie
 */
@Incompletely
public class ConditionalSql {

    /**
     * 条件修改Sql
     *
     * @param condition   条件
     * @param sqlFragment SQL片段
     * @param sqlHandler  SQL语句处理器
     * @param <T>         实体类对象
     * @param <S>         SQL语句
     * @return Action<T, S, S>
     */
    public static <T, S> Action<T, S, S> conditional(Function<T, Boolean> condition, String sqlFragment, Action<String, S, S> sqlHandler) {
        return (t, s) -> {
            boolean value = condition.apply(t);
            if (value)
                return sqlHandler.perform(sqlFragment, s);
            return s;
        };
    }

    /**
     * 字段空值条件下修改Sql
     *
     * @param getter     Getter方法
     * @param sqlHandler SQL语句处理器
     * @param <T>        实体类对象
     * @param <R>        Getter方法返回值类型
     * @param <S>        SQL语句
     * @return Action<T, S, S>
     */
    public static <T, R, S> Action<T, S, S> ifNullable(SerializableFunction<T, R> getter, Action<String, S, S> sqlHandler) {
        return (t, s) -> {
            String methodName = null;
            try {
                methodName = getMethodName(getter);
            } catch (ReflectiveOperationException e) {
                throw new RuntimeException(e);
            }
            R value = getter.apply(t);
            if (value == null)
                return sqlHandler.perform(methodName, s);
            return s;
        };
    }



    /**
     * 字段非空条件下修改Sql
     *
     * @param getter     Getter方法
     * @param sqlHandler SQL语句处理器
     * @param <T>        实体类对象
     * @param <R>        Getter方法返回值类型
     * @param <S>        SQL语句
     * @return Action<T, S, S>
     */
    public static <T, R, S> Action<T, S, S> ifNotNullable(SerializableFunction<T, R> getter, Action<String, S, S> sqlHandler) {
        return (t, s) -> {
            String methodName = null;
            try {
                methodName = getMethodName(getter);
            } catch (ReflectiveOperationException e) {
                throw new RuntimeException(e);
            }
            R value = getter.apply(t);
            if (value != null)
                return sqlHandler.perform(methodName, s);
            return s;
        };
    }
}
