package io.github.maxwellnie.javormio.common.java.api;

import java.io.Serializable;

/**
 * 映射接口，如开启对象映射缓存
 * @param <N> 源对象
 * @param <E> 目标对象
 * @author Maxwell Nie
 */
@FunctionalInterface
public interface ObjectMap<N, E> extends Serializable {
    /**
     * 映射
     * @param n 源对象
     * @param e 目标对象
     */
    E map(N n, E e);
}
