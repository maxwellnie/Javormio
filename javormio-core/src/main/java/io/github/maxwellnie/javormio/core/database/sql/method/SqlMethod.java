package io.github.maxwellnie.javormio.core.database.sql.method;

/**
 * 可对数据库进行操作的方法
 *
 * @author Maxwell Nie
 */
public interface SqlMethod {
    /**
     * 对数据库进行操作，需要精确的参数
     * @param methodFeatureCode 方法的特征码，一般是方法的HashCode
     * @param args 参数
     * @return Object
     */
    Object invokeExactly(int methodFeatureCode, Object... args) throws Throwable;

    /**
     * 对数据库进行操作，不需要精确的参数
     *
     * @param methodFeatureCode 方法的特征码，一般是方法的HashCode
     * @param args 参数
     * @return Object
     */
    Object invoke(int methodFeatureCode, Object[] args) throws Throwable;
}
