package io.github.maxwellnie.javormio.flexible.sql.plugin.execution.result.convertor;

import io.github.maxwellnie.javormio.core.execution.result.ResultParseException;
import io.github.maxwellnie.javormio.core.execution.result.ResultSetConvertor;
import io.github.maxwellnie.javormio.flexible.sql.plugin.execution.result.stream.BaseResultStream;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.function.Consumer;

/**
 * @author Maxwell Nie
 */
public class FNonConvertor<T> implements ResultSetConvertor<Object> {
    protected BaseResultStream<T> resultStream;
    protected Consumer<? super T> consumer;

    public FNonConvertor(BaseResultStream<T> resultStream, Consumer<? super T> consumer) {
        this.resultStream = resultStream;
        this.consumer = consumer;
    }

    @Override
    public Object convert(Statement statement) throws SQLException, ResultParseException {
        ResultSet rs = statement.getResultSet();
        if (rs != null) {
            resultStream.getExecutionResults().resultSet = rs;
            while (rs.next()) {
                T t = resultStream.receive();
                if (t != null)
                    consumer.accept(t);
            }
            rs.close();
            return null;
        }
        return null;
    }
}
