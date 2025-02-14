package io.github.maxwellnie.javormio.core.java.reflect;

import io.github.maxwellnie.javormio.core.java.proxy.invocation.MethodInvoker;
import io.github.maxwellnie.javormio.core.java.reflect.property.MetaField;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;

/**
 * 反射接口
 * @author Maxwell Nie
 */
public interface Reflection<T> {
    /**
     * 获取反射方法调用器
     * @param name 方法名
     * @param parameterTypes 参数类型
     * @return MethodInvoker
     * @throws NoSuchMethodException
     */
    MethodInvoker getInvoker(String name, Class<?>[] parameterTypes) throws NoSuchMethodException;
    /**
     * 获取反射字段
     * @param name 字段名
     * @param deepSearch
     * @return MetaField
     * @throws NoSuchMethodException
     * @throws NoSuchFieldException
     */
    MetaField getMetaField(String name, boolean deepSearch) throws NoSuchMethodException, NoSuchFieldException;

    /**
     * 寻找此类（包含父类）的全部字段，并返回一个关于字段名和字段对象的哈希映射
     * @return Map<String, MetaField>
     * @throws NoSuchMethodException
     * @throws NoSuchFieldException
     */
    Map<String, MetaField> linedFindAllFieldsMap() throws NoSuchMethodException, NoSuchFieldException;
    /**
     * 寻找此类（包含父类）的全部字段
     * @return Collection<Field>
     */
    default Collection<MetaField> linedFindAllFields() throws NoSuchFieldException, NoSuchMethodException {
        Map<String, MetaField> fieldMap = linedFindAllFieldsMap();
        return fieldMap.values();
    }
    /**
     * 获取反射方法
     * @param name 方法名
     * @param parameterTypes 参数类型
     * @return Method
     * @throws NoSuchMethodException
     */
    Method getMethod(String name, Class<?>[] parameterTypes) throws NoSuchMethodException;
    /**
     * 获取对象工厂
     * @return ObjectFactory
     * @throws NoSuchMethodException
     */
    ObjectFactory<T> getObjectFactory() throws NoSuchMethodException;
    /**
     * 获取声明的类
     * @return Class
     */
    Class<?> getDeclaringClass();
}
