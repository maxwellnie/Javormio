package io.github.maxwellnie.javormio.flexible.sql.plugin.execution.result.stream;

import io.github.maxwellnie.javormio.common.java.proxy.MethodInvocationException;
import io.github.maxwellnie.javormio.common.java.reflect.property.MetaField;
import io.github.maxwellnie.javormio.core.execution.result.ResultParseException;
import io.github.maxwellnie.javormio.core.translation.table.column.ColumnInfo;
import io.github.maxwellnie.javormio.flexible.sql.plugin.execution.result.convertor.ExecutionResults;

import java.util.function.Supplier;

/**
 * @author Maxwell Nie
 */
public class EntityResultStream<T> extends BaseResultStream<T> {
    protected final ResultStream<ExecutionResults> inputResultStream;
    protected final Supplier<T> instanceInvoker;

    public EntityResultStream(ResultStream<ExecutionResults> inputResultStream, Supplier<T> instanceInvoker) {
        super(inputResultStream.getExecutionResults());
        this.inputResultStream = inputResultStream;
        this.instanceInvoker = instanceInvoker;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T receive() throws ResultParseException {
        try {
            T t = instanceInvoker.get();
            for (ColumnInfo columnInfo : executionResults.getBaseColumnInfos()) {
                MetaField metaField = columnInfo.getMetaField();
                if (metaField != null)
                    metaField.set(t, executionResults.getColumnValue(columnInfo));
            }
            return t;
        } catch (MethodInvocationException e) {
            throw new ResultParseException(e);
        }
    }
}
