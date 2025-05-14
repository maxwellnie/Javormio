package io.github.maxwellnie.javormio.flexible.sql.plugin.result.stream;

import io.github.maxwellnie.javormio.common.java.api.ObjectMap;
import io.github.maxwellnie.javormio.flexible.sql.plugin.result.ExecutionResults;
import io.github.maxwellnie.javormio.flexible.sql.plugin.result.ResultParseException;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collector;

/**
 * 结果流
 *
 * @author Maxwell Nie
 */
public interface ResultStream<T> extends AutoCloseable {
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
     * 收集转换好的对象
     *
     * @param collector
     * @param <R>
     * @return R
     */
    <R, A> R collect(Collector<T, A, R> collector);

    /**
     * 收集转换好的对象
     *
     * @param supplier
     * @param accumulator
     * @param <R>
     * @return R
     */
    <R> R collect(Supplier<R> supplier, BiConsumer<R, ? super T> accumulator);

    ResultStream<T> filter(Predicate<T> predicate);

    /**
     * 接收查询结果集的一个结果
     *
     * @return T
     * @throws ResultParseException
     */
    T receive() throws ResultParseException;

    /**
     * 获取执行结果
     *
     * @return ExecutionResults
     */
    ExecutionResults getExecutionResults();

    /**
     * 关闭流
     *
     * @throws ResultParseException
     */
    void close() throws ResultParseException;

    /**
     * 获取数组
     *
     * @return T[]
     */
    T[] toArray();

    /**
     * 是否静态初始化数组
     *
     * @param isInitStaticArray
     * @return T[]
     */
    T[] toArray(boolean isInitStaticArray);

    /**
     * 收集为map
     *
     * @return List<Map < String, Object>>
     */
    List<Map<String, Object>> collectToMap();
}
