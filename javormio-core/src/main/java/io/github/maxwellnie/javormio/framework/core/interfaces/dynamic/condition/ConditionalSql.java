package io.github.maxwellnie.javormio.framework.core.interfaces.dynamic.condition;

import com.maxwellnie.velox.sql.core.annotation.Incompletely;
import io.github.maxwellnie.javormio.framework.core.interfaces.dynamic.SerializableFunction;

import java.sql.SQLException;
import java.util.function.Function;

import static io.github.maxwellnie.javormio.framework.common.utils.ReflectionUtils.getMethodName;

/**
 * 这是一个条件修改SQL的类，暂时没有加入Cache来缓存生产的代理方法
 * @author Maxwell Nie
 */
@Incompletely
public class ConditionalSql {
    /**
     * 代理执行方法接口
     * @param <T> 实体类对象
     * @param <R> 执行结果
     * @param <P> 执行参数
     */
    @FunctionalInterface
    public interface ProxyFunction<T, R, P>{
        /**
         * 执行
         * @param t 实体类对象
         * @param p 参数
         * @return R
         * @throws SQLException
         */
        R apply(T t, P p) throws SQLException, ReflectiveOperationException;
    }

    /**
     * 条件修改Sql
     * @param condition 条件
     * @param sqlFragment SQL片段
     * @param sqlHandler SQL语句处理器
     * @return ProxyFunction<T, S, S>
     * @param <T> 实体类对象
     * @param <S>  SQL语句
     */
    public static <T, S> ProxyFunction<T, S, S> conditional(Function<T, Boolean> condition, String sqlFragment, ProxyFunction<String, S, S> sqlHandler){
        return (t, s) -> {
            boolean value = condition.apply(t);
            if (value)
                return sqlHandler.apply(sqlFragment, s);
            return s;
        };
    }
    /**
     * 字段空值条件下修改Sql
     * @param getter Getter方法
     * @param sqlHandler SQL语句处理器
     * @return ProxyFunction<T, S, S>
     * @param <T> 实体类对象
     * @param <R> Getter方法返回值类型
     * @param <S>  SQL语句
     */
    public static <T, R, S> ProxyFunction<T, S, S> ifNullable(SerializableFunction<T, R> getter, ProxyFunction<String, S, S> sqlHandler){
        return (t, s) -> {
            String methodName = getMethodName(getter);
            R value = getter.apply(t);
            if (value != null)
                return sqlHandler.apply(methodName, s);
            return s;
        };
    }
    /**
     * 字段非空条件下修改Sql
     * @param getter Getter方法
     * @param sqlHandler SQL语句处理器
     * @return ProxyFunction<T, S, S>
     * @param <T> 实体类对象
     * @param <R> Getter方法返回值类型
     * @param <S>  SQL语句
     */
    public static <T, R, S> ProxyFunction<T, S, S> ifNotNullable(SerializableFunction<T, R> getter, ProxyFunction<String, S, S> sqlHandler){
        return (t, s) -> {
            String methodName = getMethodName(getter);
            R value = getter.apply(t);
            if (value != null)
                return sqlHandler.apply(methodName, s);
            return s;
        };
    }

    public static void main(String[] args) throws ReflectiveOperationException, SQLException {
        ProxyFunction<String, Boolean, Boolean> proxyFunction = ifNullable(String::toString, (methodName, s) -> true);
        if(proxyFunction.apply("", false))
            System.out.println("可将此列填入");
        else
            System.out.println("不可将此列填入");
    }
}
