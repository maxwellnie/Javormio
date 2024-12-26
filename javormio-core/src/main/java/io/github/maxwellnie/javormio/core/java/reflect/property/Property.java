package io.github.maxwellnie.javormio.core.java.reflect.property;

/**
 * 对象从属的属性接口
 *
 * @author Maxwell Nie
 */
public interface Property<K> {
    /**
     * 获取属性的值
     *
     * @param o
     * @param k
     * @param <T>
     * @return T
     */
    <T> T getValue(Object o, K k);

    /**
     * 设置属性的值
     *
     * @param o
     * @param k
     * @param value
     * @return Object
     */
    Object setValue(Object o, K k, Object value);
}
