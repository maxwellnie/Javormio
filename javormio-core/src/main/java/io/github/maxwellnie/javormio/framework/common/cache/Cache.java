package io.github.maxwellnie.javormio.framework.common.cache;

import java.io.Serializable;
import java.util.Collection;
import java.util.Set;

public interface Cache<K extends Serializable, V extends Serializable> {
    void put(K k, V v);

    V get(K k);

    Collection<V> findBy(K prefixKey);

    V remove(K k);

    int size();

    Set<K> keys();

    Collection<V> values();

    void clear();
}
