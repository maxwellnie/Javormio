package io.github.maxwellnie.javormio.flexible.sql.plugin.execution.result.convertor;

import io.github.maxwellnie.javormio.core.execution.result.ResultParseException;
import io.github.maxwellnie.javormio.core.execution.result.ResultSetConvertor;
import io.github.maxwellnie.javormio.flexible.sql.plugin.execution.result.stream.BaseResultStream;
import io.github.maxwellnie.javormio.flexible.sql.plugin.execution.result.stream.ResultStream;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.stream.Collector;

/**
 * @author Maxwell Nie
 */
public class FCollectorConvertor<T, A, R> implements ResultSetConvertor<R> {
    final BaseResultStream<T> resultStream;
    final Collector<T, A, R> collector;

    public FCollectorConvertor(BaseResultStream<T> resultStream, Collector<T, A, R> collector) {
        this.resultStream = resultStream;
        this.collector = collector;
    }

    @Override
    @SuppressWarnings("unchecked")
    public R convert(Statement statement) throws SQLException, ResultParseException {
        ResultSet rs = statement.getResultSet();

        if (rs != null) {
            resultStream.getExecutionResults().resultSet = rs;
            A container = collector.supplier().get();
            while (rs.next()) {
                T t = resultStream.receive();
                if (t != null)
                    collector.accumulator().accept(container, t);
            }
            rs.close();
            return collector.characteristics().contains(Collector.Characteristics.IDENTITY_FINISH)
                    ? (R) container
                    : collector.finisher().apply(container);
        }
        return null;
    }
}
