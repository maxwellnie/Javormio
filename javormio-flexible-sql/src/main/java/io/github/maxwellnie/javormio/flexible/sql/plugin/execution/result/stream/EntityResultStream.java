package io.github.maxwellnie.javormio.flexible.sql.plugin.execution.result.stream;

import io.github.maxwellnie.javormio.common.java.proxy.MethodInvocationException;
import io.github.maxwellnie.javormio.common.java.reflect.property.MetaField;
import io.github.maxwellnie.javormio.core.execution.result.ResultParseException;
import io.github.maxwellnie.javormio.common.java.table.column.ColumnInfo;
import io.github.maxwellnie.javormio.flexible.sql.plugin.execution.result.convertor.ResultContext;

import java.util.function.Supplier;

/**
 * @author Maxwell Nie
 */
public class EntityResultStream<T> extends BaseResultStream<T> {
    protected final ResultStream<ResultContext> inputResultStream;
    protected final Supplier<T> instanceInvoker;

    public EntityResultStream(ResultStream<ResultContext> inputResultStream, Supplier<T> instanceInvoker) {
        super(inputResultStream.getExecutionResults());
        this.inputResultStream = inputResultStream;
        this.instanceInvoker = instanceInvoker;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T receive() throws ResultParseException {
        try {
            T t = instanceInvoker.get();
            for (ColumnInfo columnInfo : resultContext.getBaseColumnInfos()) {
                MetaField metaField = columnInfo.getMetaField();
                if (metaField != null)
                    metaField.set(t, resultContext.getColumnValue(columnInfo));
            }
            return t;
        } catch (MethodInvocationException e) {
            throw new ResultParseException(e);
        }
    }
}
