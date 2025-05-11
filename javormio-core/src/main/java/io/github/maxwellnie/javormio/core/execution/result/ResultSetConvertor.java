package io.github.maxwellnie.javormio.core.execution.result;

import io.github.maxwellnie.javormio.common.java.api.ObjectMap;
import io.github.maxwellnie.javormio.common.java.proxy.invocation.MethodInvoker;

import java.sql.ResultSet;
import java.util.List;

/**
 * 将ResultSet转换为Java实体对象
 *
 * @author Maxwell Nie
 */
public interface ResultSetConvertor {
    /**
     * 将ResultSet转换为Java实体对象
     *
     * @param resultSet
     * @param typeMapping 类型映射
     * @param instanceInvoker 实例化方法
     * @return Object
     */
    <E> E convert(ResultSet resultSet, ObjectMap<ResultSet, E> typeMapping, MethodInvoker<E, E> instanceInvoker) throws ConvertException;

    /**
     * 将ResultSet列表转换为Java实体对象
     *
     * @param resultSets
     * @param typeMapping 类型映射
     * @param instanceInvoker 实例化方法
     * @return Object
     */
    <E> E convert(List<ResultSet> resultSets, ObjectMap<ResultSet, E> typeMapping, MethodInvoker<E, E> instanceInvoker) throws ConvertException;
}
