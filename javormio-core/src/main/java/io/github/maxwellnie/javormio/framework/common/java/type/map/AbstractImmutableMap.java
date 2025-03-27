package io.github.maxwellnie.javormio.framework.common.java.type.map;

import java.util.*;

/**
 * @author Maxwell Nie
 */
public class AbstractImmutableMap<K,V> implements ImmutableMap<K,V>{
    protected final Map<K,V> container;

    protected AbstractImmutableMap(Map<K,V> mutableMap, Map<K,V> container) {
        this.container = container;
        this.container.putAll(mutableMap);
    }
    protected AbstractImmutableMap(Map<K,V> mutableMap){
        this.container = mutableMap;
    }
    public static <K,V>  AbstractImmutableMap<K,V> of (Map<K,V> mutableMap){
        return new AbstractImmutableMap<>(mutableMap,new LinkedHashMap<>());
    }
    public static <K,V>  AbstractImmutableMap<K,V> immutable (Map<K,V> mutableMap){
        return new AbstractImmutableMap<>(mutableMap);
    }
    @Override
    public int size() {
        return this.container.size();
    }

    @Override
    public boolean isEmpty() {
        return this.container.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return this.container.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return this.container.containsValue(value);
    }

    @Override
    public V get(Object key) {
        return this.container.get(key);
    }

    @Override
    public Set<K> keySet() {
        return this.container.keySet();
    }

    @Override
    public Collection<V> values() {
        return this.container.values();
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return this.container.entrySet();
    }
}
