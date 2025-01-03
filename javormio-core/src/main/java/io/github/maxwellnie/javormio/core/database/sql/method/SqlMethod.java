package io.github.maxwellnie.javormio.core.database.sql.method;

/**
 * 可对数据库进行操作的方法
 *
 * @author Maxwell Nie
 */
public interface SqlMethod {
    /**
     * 对数据库进行操作，需要精确的参数
     *
     * @param args
     * @return 执行结果
     */
    Object invokeExactly(Object... args);

    /**
     * 对数据库进行操作，不需要精确的参数
     *
     * @param args
     * @return 执行结果
     */
    Object invoke(Object[] args);
}
