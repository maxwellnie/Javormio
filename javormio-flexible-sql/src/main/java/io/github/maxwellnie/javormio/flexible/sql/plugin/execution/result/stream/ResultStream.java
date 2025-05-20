package io.github.maxwellnie.javormio.flexible.sql.plugin.execution.result.stream;

import io.github.maxwellnie.javormio.common.java.api.ObjectMap;
import io.github.maxwellnie.javormio.core.execution.ExecutionException;
import io.github.maxwellnie.javormio.core.execution.result.ResultParseException;
import io.github.maxwellnie.javormio.flexible.sql.plugin.execution.result.convertor.ExecutionResults;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collector;

/**
 * 结果流
 *
 * @author Maxwell Nie
 */
public interface ResultStream<T> {
    /**
     * 转换为指定的对象
     *
     * @param supplier
     * @param objectMap
     * @param <R>
     * @return ResultStream<R>
     */
    <R> ResultStream<R> mapTo(Supplier<R> supplier, ObjectMap<T, R> objectMap);

    /**
     * 映射为指定的对象R
     *
     * @param resultMapping
     * @return ResultStream<R>
     */
    <R> ResultStream<R> mapTo(ResultMapping<T, R> resultMapping);

    /**
     * 将结果流映射为指定的对象R
     *
     * @param collector
     * @param <R>
     * @return R
     */
    <R, A> R collect(Collector<T, A, R> collector) throws ExecutionException;

    /**
     * 将结果流映射为指定的对象R
     *
     * @param supplier
     * @param accumulator
     * @param <R>
     * @return R
     */
    <R> R collect(Supplier<R> supplier, BiConsumer<R, ? super T> accumulator) throws ExecutionException;

    /**
     * 过滤
     *
     * @param predicate
     * @return ResultStream<T>
     */
    ResultStream<T> filter(Predicate<T> predicate);

    /**
     * 获取执行结果
     *
     * @return ExecutionResults
     */
    ExecutionResults getExecutionResults();

    /**
     * 获取数组
     *
     * @return T[]
     */
    T[] toArray()  throws ExecutionException;

    /**
     * 获取数组
     *
     * @return T[]
     */
    T[] toArray(int length)  throws ExecutionException;

    /**
     * 收集为map
     *
     * @return List<Map < String, Object>>
     */
    List<Map<String, Object>> collectToMap();

    /**
     * 遍历
     *
     * @param action
     */
    void forEach(Consumer<? super T> action);

    class ResultMapping<N, T> {
        final Supplier<T> supplier;
        final ObjectMap<N, T> objectMap;

        public ResultMapping(Supplier<T> supplier, ObjectMap<N, T> objectMap) {
            this.supplier = supplier;
            this.objectMap = objectMap;
        }

        public Supplier<T> getSupplier() {
            return supplier;
        }

        public ObjectMap<N, T> getObjectMap() {
            return objectMap;
        }
    }
}
