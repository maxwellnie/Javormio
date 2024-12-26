package io.github.maxwellnie.javormio.core.java.concurrent;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Supplier;

/**
 * 线程本地变量
 *
 * @author Maxwell Nie
 */
public class ThreadLocalManager<T> extends ThreadLocal<T> {
    private final ConcurrentMap<Long, T> threadLocal = new ConcurrentHashMap<>();

    public static <S> ThreadLocal<S> withInitial(Supplier<? extends S> supplier) {
        return new SuppliedThreadLocal<>(supplier);
    }

    @Override
    public void set(T value) {
        set(Thread.currentThread(), value);
    }

    /**
     * 设置线程t的本地变量
     *
     * @param t
     * @param value
     */
    public void set(Thread t, T value) {
        threadLocal.put(t.getId(), value);
    }

    @Override
    public T get() {
        return get(Thread.currentThread());
    }

    /**
     * 获取线程t的本地变量
     *
     * @param t
     * @return
     */
    public T get(Thread t) {
        return threadLocal.get(t.getId());
    }

    @Override
    public void remove() {
        remove(Thread.currentThread());
    }

    /**
     * 删除线程t的本地变量
     *
     * @param t
     */
    public void remove(Thread t) {
        threadLocal.remove(t.getId());
    }

    static final class SuppliedThreadLocal<T> extends ThreadLocalManager<T> {

        private final Supplier<? extends T> supplier;

        SuppliedThreadLocal(Supplier<? extends T> supplier) {
            this.supplier = Objects.requireNonNull(supplier);
        }

        @Override
        protected T initialValue() {
            return supplier.get();
        }
    }
}
