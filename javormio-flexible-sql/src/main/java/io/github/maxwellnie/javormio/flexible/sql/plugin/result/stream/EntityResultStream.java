package io.github.maxwellnie.javormio.flexible.sql.plugin.result.stream;

import io.github.maxwellnie.javormio.common.java.proxy.MethodInvocationException;
import io.github.maxwellnie.javormio.common.java.proxy.invocation.MethodInvoker;
import io.github.maxwellnie.javormio.core.translation.table.column.ColumnInfo;
import io.github.maxwellnie.javormio.flexible.sql.plugin.result.ExecutionResults;
import io.github.maxwellnie.javormio.flexible.sql.plugin.result.ResultParseException;

/**
 * @author Maxwell Nie
 */
public class EntityResultStream<T> extends BaseResultStream<T> {
    protected final ResultStream<ExecutionResults> inputResultStream;
    protected final MethodInvoker<T, T> instanceInvoker;

    public EntityResultStream(ResultStream<ExecutionResults> inputResultStream, MethodInvoker<T, T> instanceInvoker) {
        super(inputResultStream.getExecutionResults());
        this.inputResultStream = inputResultStream;
        this.instanceInvoker = instanceInvoker;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T receive() throws ResultParseException {
        try {
            T t = instanceInvoker.invoke();
            for (ColumnInfo columnInfo : executionResults.getBaseColumnInfos()) {
                columnInfo.getMetaField().set(t, executionResults.getColumnValue(columnInfo));
            }
            return t;
        } catch (MethodInvocationException e) {
            throw new ResultParseException(e);
        }
    }
}
