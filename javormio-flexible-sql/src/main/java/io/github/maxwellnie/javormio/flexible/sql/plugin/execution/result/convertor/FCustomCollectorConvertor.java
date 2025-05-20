package io.github.maxwellnie.javormio.flexible.sql.plugin.execution.result.convertor;

import io.github.maxwellnie.javormio.core.execution.result.ResultParseException;
import io.github.maxwellnie.javormio.core.execution.result.ResultSetConvertor;
import io.github.maxwellnie.javormio.flexible.sql.plugin.execution.result.stream.BaseResultStream;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

/**
 * @author Maxwell Nie
 */
public class FCustomCollectorConvertor<A, T> implements ResultSetConvertor<A> {
    final BaseResultStream<T> resultStream;
    final Supplier<A> supplier;
    final BiConsumer<A, ? super T> accumulator;

    public FCustomCollectorConvertor(BaseResultStream<T> resultStream, Supplier<A> supplier, BiConsumer<A, ? super T> accumulator) {
        this.resultStream = resultStream;
        this.supplier = supplier;
        this.accumulator = accumulator;
    }

    @Override
    public A convert(Statement statement) throws SQLException, ResultParseException {
        ResultSet rs = statement.getResultSet();
        if (rs != null) {
            resultStream.getExecutionResults().resultSet = rs;
            A result = supplier.get();
            while (rs.next()) {
                T t = resultStream.receive();
                if (t != null)
                    accumulator.accept(result, t);
            }
            rs.close();
            return result;
        }
        return null;
    }
}
