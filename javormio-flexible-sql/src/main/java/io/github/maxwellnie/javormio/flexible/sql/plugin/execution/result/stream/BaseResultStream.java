package io.github.maxwellnie.javormio.flexible.sql.plugin.execution.result.stream;

import io.github.maxwellnie.javormio.common.java.api.ObjectMap;
import io.github.maxwellnie.javormio.core.execution.result.ResultParseException;
import io.github.maxwellnie.javormio.flexible.sql.plugin.execution.result.convertor.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collector;

/**
 * @author Maxwell Nie
 */
public abstract class BaseResultStream<T> implements ResultStream<T> {
    protected ExecutionResults executionResults;

    public BaseResultStream(ExecutionResults executionResults) {
        if (executionResults == null)
            throw new ResultParseException("\"executionResults\" must not be null!");
        this.executionResults = executionResults;
    }

    @Override
    public <R> ResultStream<R> mapTo(Supplier<R> supplier, ObjectMap<T, R> objectMap) {
        return new MapResultStream<>(executionResults, this, supplier, objectMap);
    }

    @Override
    public <R> ResultStream<R> mapTo(ResultMapping<T, R> resultMapping) {
        return new MapResultStream<>(executionResults, this, resultMapping.supplier, resultMapping.objectMap);
    }

    @Override
    public ResultStream<T> filter(Predicate<T> predicate) {
        return new FilterResultStream<>(predicate, this);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <R, A> R collect(Collector<T, A, R> collector) {
        executionResults.getExecutorParameters().setResultSetConvertor(new FCollectorConvertor<>(this, collector));
        return (R) executionResults.getSqlExecutor().query(executionResults.getExecutorParameters());
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> collectToMap() {
        executionResults.getExecutorParameters().setResultSetConvertor(new FMapConvertor(this));
        return (List<Map<String, Object>>) executionResults.getSqlExecutor().query(executionResults.getExecutorParameters());
    }

    @Override
    @SuppressWarnings("unchecked")
    public void forEach(Consumer<? super T> action) throws ResultParseException {
        executionResults.getExecutorParameters().setResultSetConvertor(new FNonConvertor<>(this, action));
        executionResults.getSqlExecutor().query(executionResults.getExecutorParameters());
    }
    /**
     * 接收查询结果集的一个结果
     *
     * @return T
     * @throws SQLException
     */
    public abstract T receive() throws SQLException;
    @Override
    @SuppressWarnings("unchecked")
    public <R> R collect(Supplier<R> supplier, BiConsumer<R, ? super T> accumulator) {
        executionResults.getExecutorParameters().setResultSetConvertor(new FCustomCollectorConvertor<>(this, supplier, accumulator));
        return (R) executionResults.getSqlExecutor().query(executionResults.getExecutorParameters());
    }

    @Override
    public ExecutionResults getExecutionResults() {
        return executionResults;
    }


    @Override
    @SuppressWarnings("unchecked")
    public T[] toArray() {
        executionResults.getExecutorParameters().setResultSetConvertor(new FArrayConvertor<>(this));
        return (T[]) executionResults.getSqlExecutor().query(executionResults.getExecutorParameters());
    }


    @Override
    @SuppressWarnings("unchecked")
    public T[] toArray(int length) {
        executionResults.getExecutorParameters().setResultSetConvertor(new FArrayConvertor<>(this, length));
        return (T[]) executionResults.getSqlExecutor().query(executionResults.getExecutorParameters());
    }

    static class MapResultStream<In, Out> extends BaseResultStream<Out> {
        final BaseResultStream<In> inputResultStream;
        final Supplier<Out> supplier;
        final ObjectMap<In, Out> objectMap;

        public MapResultStream(ExecutionResults executionResults, BaseResultStream<In> inputResultStream, Supplier<Out> supplier, ObjectMap<In, Out> objectMap) {
            super(executionResults);
            this.inputResultStream = inputResultStream;
            this.supplier = supplier;
            this.objectMap = objectMap;
        }

        @Override
        public Out receive() throws SQLException {
            In in = inputResultStream.receive();
            return objectMap.map(in, supplier.get());
        }
    }
}
