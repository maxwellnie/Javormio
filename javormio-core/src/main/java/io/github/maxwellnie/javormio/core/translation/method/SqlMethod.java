package io.github.maxwellnie.javormio.core.translation.method;

import io.github.maxwellnie.javormio.common.java.api.JavormioException;
import io.github.maxwellnie.javormio.core.Context;

import java.util.Map;

/**
 * 可对数据库进行操作的方法
 *
 * @author Maxwell Nie
 */
public interface SqlMethod<T> {

    /**
     * 对数据库进行操作，不需要精确的参数
     *
     * @param args              参数
     * @return Object
     */
     T invoke(Object[] args) throws JavormioException;
}
