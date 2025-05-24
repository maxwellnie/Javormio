package io.github.maxwellnie.javormio.flexible.sql.plugin.execution.result.stream;

import io.github.maxwellnie.javormio.common.java.api.ObjectMap;
import io.github.maxwellnie.javormio.core.execution.result.ResultParseException;
import io.github.maxwellnie.javormio.flexible.sql.plugin.execution.result.convertor.*;

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
    protected ResultContext resultContext;

    public BaseResultStream(ResultContext resultContext) {
        if (resultContext == null)
            throw new ResultParseException("\"resultContext\" must not be null!");
        this.resultContext = resultContext;
    }

    @Override
    public <R> ResultStream<R> mapTo(Supplier<R> supplier, ObjectMap<T, R> objectMap) {
        return new MapResultStream<>(resultContext, this, supplier, objectMap);
    }

    @Override
    public <R> ResultStream<R> mapTo(ResultMapping<T, R> resultMapping) {
        return new MapResultStream<>(resultContext, this, resultMapping.supplier, resultMapping.objectMap);
    }

    @Override
    public ResultStream<T> filter(Predicate<T> predicate) {
        return new FilterResultStream<>(predicate, this);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <R, A> R collect(Collector<T, A, R> collector) {
        resultContext.getExecutorParameters().setResultSetConvertor(new FCollectorConvertor<>(this, collector));
        return (R) resultContext.getSqlExecutor().query(resultContext.getExecutorParameters());
    }

    @Override
    @SuppressWarnings("unchecked")
    public void forEach(Consumer<? super T> action) throws ResultParseException {
        resultContext.getExecutorParameters().setResultSetConvertor(new FNonConvertor<>(this, action));
        resultContext.getSqlExecutor().query(resultContext.getExecutorParameters());
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
        resultContext.getExecutorParameters().setResultSetConvertor(new FCustomCollectorConvertor<>(this, supplier, accumulator));
        return (R) resultContext.getSqlExecutor().query(resultContext.getExecutorParameters());
    }

    @Override
    public ResultContext getExecutionResults() {
        return resultContext;
    }


    @Override
    @SuppressWarnings("unchecked")
    public T[] toArray() {
        resultContext.getExecutorParameters().setResultSetConvertor(new FArrayConvertor<>(this));
        return (T[]) resultContext.getSqlExecutor().query(resultContext.getExecutorParameters());
    }


    @Override
    @SuppressWarnings("unchecked")
    public T[] toArray(int length) {
        resultContext.getExecutorParameters().setResultSetConvertor(new FArrayConvertor<>(this, length));
        return (T[]) resultContext.getSqlExecutor().query(resultContext.getExecutorParameters());
    }

    static class MapResultStream<In, Out> extends BaseResultStream<Out> {
        final BaseResultStream<In> inputResultStream;
        final Supplier<Out> supplier;
        final ObjectMap<In, Out> objectMap;

        public MapResultStream(ResultContext resultContext, BaseResultStream<In> inputResultStream, Supplier<Out> supplier, ObjectMap<In, Out> objectMap) {
            super(resultContext);
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
