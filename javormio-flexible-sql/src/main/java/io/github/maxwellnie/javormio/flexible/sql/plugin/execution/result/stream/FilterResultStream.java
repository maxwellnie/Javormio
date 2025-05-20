package io.github.maxwellnie.javormio.flexible.sql.plugin.execution.result.stream;

import java.sql.SQLException;
import java.util.function.Predicate;

/**
 * @author Maxwell Nie
 */
public class FilterResultStream<T> extends BaseResultStream<T> {
    protected Predicate<T> predicate;
    protected BaseResultStream<T> inputResultStream;

    public FilterResultStream(Predicate<T> predicate, BaseResultStream<T> inputResultStream) {
        super(inputResultStream.getExecutionResults());
        this.predicate = predicate;
        this.inputResultStream = inputResultStream;
    }

    @Override
    public T receive() throws SQLException {
        T t = inputResultStream.receive();
        if (predicate.test(t))
            return t;
        return null;
    }
}
