package io.github.maxwellnie.javormio.framework.common.utils;

import io.github.maxwellnie.javormio.framework.common.java.api.Resource;
import io.github.maxwellnie.javormio.framework.common.java.reflect.property.PropertyType;

import java.time.temporal.TemporalAccessor;
import java.util.Collection;
import java.util.Date;

/**
 * 类型工具
 *
 * @author Maxwell Nie
 */
public class TypeUtils {
    /**
     * 判断是否为集合
     *
     * @param clazz
     * @return boolean
     */
    public static boolean isCollection(Class<?> clazz) {
        return Collection.class.isAssignableFrom(clazz);
    }

    /**
     * 判断是否为Map
     *
     * @param clazz
     * @return boolean
     */
    public static boolean isMap(Class<?> clazz) {
        return java.util.Map.class.isAssignableFrom(clazz);
    }

    /**
     * 判断是否为数组
     *
     * @param clazz
     * @return boolean
     */
    public static boolean isArray(Class<?> clazz) {
        return clazz.isArray();
    }

    /**
     * 判断是否为集合
     *
     * @param clazz
     * @return boolean
     */
    public static boolean isSet(Class<?> clazz) {
        return java.util.Set.class.isAssignableFrom(clazz);
    }

    /**
     * 判断是否为基本类型，由指可被写入数据库的数据类型
     *
     * @param clazz
     * @return boolean
     */
    public static boolean isBasic(Class<?> clazz) {
        String name = clazz.getName();
        return clazz.isPrimitive()
                || Date.class.isAssignableFrom(clazz)
                || TemporalAccessor.class.isAssignableFrom(clazz)
                || name.startsWith("java.math")
                || String.class.isAssignableFrom(clazz);
    }

    /**
     * 判断是否为日期类型
     *
     * @param clazz
     * @return 0 Type extend Date,1 Type implements TemporalAccessor,-1 is not
     */
    public static int isDate(Class<?> clazz) {
        return Date.class.isAssignableFrom(clazz) ? 0 : TemporalAccessor.class.isAssignableFrom(clazz) ? 1 : -1;
    }

    /**
     * 判断是否为枚举
     *
     * @param clazz
     * @return boolean
     */
    public static boolean isEnum(Class<?> clazz) {
        return clazz.isEnum();
    }

    /**
     * 获取枚举
     *
     * @param clazz
     * @param name
     * @return T
     */
    public static <T extends Enum<T>> T getEnum(Class<T> clazz, String name) {
        if (!isEnum(clazz))
            return null;
        return Enum.valueOf(clazz, name);
    }

    /**
     * 判断是否为bean
     *
     * @param clazz
     * @return boolean
     */
    public static boolean isComplexType(Class<?> clazz) {
        return Resource.class.isAssignableFrom(clazz) || (!isBasic(clazz) && !isArray(clazz) && !isCollection(clazz));
    }

    /**
     * 获取属性类型
     *
     * @param clazz
     * @return PropertyType
     */
    public static PropertyType getPropertyType(Class<?> clazz) {
        if (isArray(clazz)) {
            return PropertyType.ARRAY;
        } else if (isMap(clazz)) {
            return PropertyType.MAP;
        } else if (isList(clazz)) {
            return PropertyType.LIST;
        } else if (isSet(clazz)) {
            return PropertyType.SET;
        } else if (isCollection(clazz)) {
            return PropertyType.COLLECTION;
        } else if (isComplexType(clazz)) {
            return PropertyType.BEAN;
        } else
            return PropertyType.BASIC;
    }

    /**
     * 判断是否为List
     *
     * @param clazz
     * @return boolean
     */
    public static boolean isList(Class<?> clazz) {
        return java.util.List.class.isAssignableFrom(clazz);
    }
}
