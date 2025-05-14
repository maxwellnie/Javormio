package io.github.maxwellnie.javormio.flexible.sql.plugin.result.stream;

import io.github.maxwellnie.javormio.common.java.api.ObjectMap;
import io.github.maxwellnie.javormio.core.translation.table.column.ColumnInfo;
import io.github.maxwellnie.javormio.flexible.sql.plugin.result.ExecutionResults;
import io.github.maxwellnie.javormio.flexible.sql.plugin.result.ResultParseException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collector;

/**
 * @author Maxwell Nie
 */
public abstract class BaseResultStream<T> implements ResultStream<T> {
    protected ExecutionResults executionResults;
    protected Predicate<T> predicate;

    public BaseResultStream(ExecutionResults executionResults) {
        if (executionResults == null)
            throw new ResultParseException("\"executionResults\" must not be null!");
        this.executionResults = executionResults;
    }

    @Override
    public <R> ResultStream<R> mapTo(Supplier<R> supplier, ObjectMap<T, R> objectMap) {
        return new MapResultStream<T, R>(executionResults, this, supplier, objectMap);
    }

    @Override
    public ResultStream<T> filter(Predicate<T> predicate) {
        this.predicate = predicate;
        return this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <R, A> R collect(Collector<T, A, R> collector) {
        try {
            A container = collector.supplier().get();
            ResultSet resultSet = getExecutionResults().getResultSet();
            while (!resultSet.isClosed() && resultSet.next()) {
                T t = receive();
                if (predicate != null && !predicate.test(t))
                    continue;
                collector.accumulator().accept(container, t);
            }
            return collector.characteristics().contains(Collector.Characteristics.IDENTITY_FINISH)
                    ? (R) container
                    : collector.finisher().apply(container);
        } catch (Throwable e) {
            throw new ResultParseException(e);
        } finally {
            close();
        }
    }

    @Override
    public List<Map<String, Object>> collectToMap() {
        try {
            ResultSet resultSet = getExecutionResults().getResultSet();
            List<ColumnInfo> columnInfos = executionResults.getBaseColumnInfos();
            Map<ColumnInfo, String> columnAliases = executionResults.getColumnAliases();
            List<Map<String, Object>> list = new LinkedList<>();
            while (resultSet.next()) {
                Map<String, Object> map = new LinkedHashMap<>();
                for (ColumnInfo columnInfo : columnInfos) {
                    String columnName = columnAliases.get(columnInfo);
                    if (columnName == null)
                        columnName = columnInfo.getColumnName();
                    Object value = columnInfo.getTypeHandler().getValue(resultSet, executionResults.getColumnIndex(columnInfo));
                    map.put(columnName, value);
                }
                list.add(map);
            }
            return list;
        } catch (Throwable e) {
            throw new ResultParseException(e);
        } finally {
            close();
        }
    }

    @Override
    public <R> R collect(Supplier<R> supplier, BiConsumer<R, ? super T> accumulator) {
        try {
            R container = supplier.get();
            ResultSet resultSet = getExecutionResults().getResultSet();
            while (resultSet.next()) {
                T t = receive();
                if (predicate != null && !predicate.test(t))
                    continue;
                accumulator.accept(container, t);
            }
            return container;
        } catch (Throwable e) {
            throw new ResultParseException(e);
        } finally {
            close();
        }
    }

    @Override
    public ExecutionResults getExecutionResults() {
        return executionResults;
    }

    @Override
    public void close() throws ResultParseException {
        if (executionResults.getAutoCloseableResources() != null) {
            for (AutoCloseable autoCloseable : executionResults.getAutoCloseableResources()) {
                try {
                    autoCloseable.close();
                } catch (Exception e) {
                    throw new ResultParseException(e);
                }
            }
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public T[] toArray() {
        int count = -1;
        ResultSet resultSet = executionResults.getResultSet();
        try {
            count = resultSet.getMetaData().getColumnCount();
        } catch (SQLException ignored) {
        }
        T[] array;
        if (count == -1) {
            array = dynamicInitResultArray(resultSet);
        } else {
            try {
                array = (T[]) new Object[count];
                count = 0;
                while (resultSet.next()) {
                    T t = receive();
                    array[count++] = t;
                }
            } catch (SQLException e) {
                throw new ResultParseException(e);
            }
        }
        return array;
    }

    @SuppressWarnings("unchecked")
    protected T[] dynamicInitResultArray(ResultSet resultSet) {
        T[] array;
        try {
            List<T> resultList = new LinkedList<>();
            while (resultSet.next()) {
                T t = receive();
                resultList.add(t);
            }
            array = (T[]) resultList.toArray();
        } catch (SQLException e) {
            throw new ResultParseException(e);
        }
        return array;
    }

    @Override
    public T[] toArray(boolean isInitStaticArray) {
        return isInitStaticArray ? toArray() : dynamicInitResultArray(executionResults.getResultSet());
    }

    static class MapResultStream<In, Out> extends BaseResultStream<Out> {
        final ResultStream<In> inputResultStream;
        final Supplier<Out> supplier;
        final ObjectMap<In, Out> objectMap;

        public MapResultStream(ExecutionResults executionResults, ResultStream<In> inputResultStream, Supplier<Out> supplier, ObjectMap<In, Out> objectMap) {
            super(executionResults);
            this.inputResultStream = inputResultStream;
            this.supplier = supplier;
            this.objectMap = objectMap;
        }

        @Override
        public Out receive() throws ResultParseException {
            In in = inputResultStream.receive();
            return objectMap.map(in, supplier.get());
        }
    }
}
