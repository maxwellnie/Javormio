package io.github.maxwellnie.javormio.core.translation.method;

import io.github.maxwellnie.javormio.core.Context;

/**
 * 可对数据库进行操作的方法
 *
 * @author Maxwell Nie
 */
public interface SqlMethod {

    /**
     * 对数据库进行操作，不需要精确的参数
     *
     * @param methodFeatureCode 方法的特征码，一般是方法的HashCode
     * @param context           上下文
     * @param args              参数
     * @return Object
     */
    Object invoke(int methodFeatureCode, Context context, Object[] args) throws Throwable;
}
