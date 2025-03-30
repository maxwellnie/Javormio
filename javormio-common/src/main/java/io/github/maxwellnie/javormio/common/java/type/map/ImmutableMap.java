package io.github.maxwellnie.javormio.common.java.type.map;

import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * 不可变映射
 *
 * @author Maxwell Nie
 */
public interface ImmutableMap<K, V> extends Map<K, V> {

    @Override
    default V put(K key, V value) {
        throw new UnsupportedOperationException("The map is immutable.");
    }

    @Override
    default V remove(Object key) {
        throw new UnsupportedOperationException("The map is immutable.");
    }

    @Override
    default void putAll(Map<? extends K, ? extends V> m) {
        throw new UnsupportedOperationException("The map is immutable.");
    }

    @Override
    default void clear() {
        throw new UnsupportedOperationException("The map is immutable.");
    }


    @Override
    default V getOrDefault(Object key, V defaultValue) {
        V value = get(key);
        if (value == null)
            throw new UnsupportedOperationException("The map is immutable.");
        else
            return value;
    }


    @Override
    default void replaceAll(BiFunction<? super K, ? super V, ? extends V> function) {
        throw new UnsupportedOperationException("The map is immutable.");
    }

    @Override
    default V putIfAbsent(K key, V value) {
        throw new UnsupportedOperationException("The map is immutable.");
    }

    @Override
    default boolean remove(Object key, Object value) {
        throw new UnsupportedOperationException("The map is immutable.");
    }

    @Override
    default boolean replace(K key, V oldValue, V newValue) {
        throw new UnsupportedOperationException("The map is immutable.");
    }

    @Override
    default V replace(K key, V value) {
        throw new UnsupportedOperationException("The map is immutable.");
    }

    @Override
    default V computeIfAbsent(K key, Function<? super K, ? extends V> mappingFunction) {
        throw new UnsupportedOperationException("The map is immutable.");
    }

    @Override
    default V computeIfPresent(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        throw new UnsupportedOperationException("The map is immutable.");
    }

    @Override
    default V compute(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        throw new UnsupportedOperationException("The map is immutable.");
    }

    @Override
    default V merge(K key, V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
        throw new UnsupportedOperationException("The map is immutable.");
    }
}
