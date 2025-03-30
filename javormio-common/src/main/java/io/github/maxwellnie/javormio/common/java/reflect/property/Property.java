package io.github.maxwellnie.javormio.common.java.reflect.property;

/**
 * 对象从属的属性接口
 *
 * @author Maxwell Nie
 */
public interface Property {
    /**
     * 获取属性的值
     *
     * @param o
     * @param key
     * @return T
     */
    Object getValue(Object o, Object key);

    /**
     * 设置属性的值
     *
     * @param o
     * @param key
     * @param value
     * @return Object
     */
    Object setValue(Object o, Object key, Object value);
}
