package io.github.maxwellnie.javormio.framework.common.java.api;

/**
 * 注册表
 *
 * @author Maxwell Nie
 */
public interface Registry<K, V> {
    /**
     * 最好在框架运行前注册
     *
     * @param key
     * @param object
     */
    void register(K key, V object);

    /**
     * 获取被注册的值
     *
     * @param key
     * @return V
     */
    V get(K key);
}
