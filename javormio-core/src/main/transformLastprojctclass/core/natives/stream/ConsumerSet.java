package com.maxwellnie.velox.sql.core.natives.stream;

import java.util.HashSet;
import java.util.function.Consumer;

/**
 * 消费者集合
 * @author Maxwell Nie
 */
public class ConsumerSet<T> implements ActionSet<T, Consumer<T>> {
    private HashSet<Consumer<T>> consumers = new HashSet<>();

    @Override
    public void add(Consumer<T> consumer) {
        consumers.add(consumer);
    }

    @Override
    public void remove(Consumer<T> consumer) {
        consumers.remove(consumer);
    }

    @Override
    public void accept(T t) {
        if (t != null)
            consumers.forEach(consumer -> consumer.accept(t));
    }

    @Override
    public void clear() {
        consumers.clear();
    }
}
