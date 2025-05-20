package io.github.maxwellnie.javormio.flexible.sql.plugin.execution.result.convertor;

import io.github.maxwellnie.javormio.core.execution.result.ResultParseException;
import io.github.maxwellnie.javormio.core.execution.result.ResultSetConvertor;
import io.github.maxwellnie.javormio.flexible.sql.plugin.execution.result.stream.BaseResultStream;
import io.github.maxwellnie.javormio.flexible.sql.plugin.execution.result.stream.ResultStream;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Maxwell Nie
 */
public class FArrayConvertor<E> implements ResultSetConvertor<E[]> {
    protected BaseResultStream<E> resultStream;
    protected int length = -1;

    public FArrayConvertor(BaseResultStream<E> resultStream, int length) {
        this.resultStream = resultStream;
        this.length = length;
    }

    public FArrayConvertor(BaseResultStream<E> resultStream) {
        this.resultStream = resultStream;
    }

    @Override
    public E[] convert(Statement statement) throws SQLException, ResultParseException {
        if (length == -1)
            return dynamicArray(statement);
        return staticArray(statement);
    }

    @SuppressWarnings("unchecked")
    public E[] dynamicArray(Statement statement) throws SQLException, ResultParseException {
        ResultSet rs = statement.getResultSet();

        if (rs != null) {
            resultStream.getExecutionResults().resultSet = rs;
            E[] array;
            List<E> resultList = new LinkedList<>();
            while (rs.next()) {
                E e = resultStream.receive();
                if (e != null)
                    resultList.add(e);
            }
            array = (E[]) resultList.toArray();
            rs.close();
            return array;
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public E[] staticArray(Statement statement) throws SQLException, ResultParseException {
        ResultSet rs = statement.getResultSet();

        if (rs != null) {
            resultStream.getExecutionResults().resultSet = rs;
            E[] array = (E[]) new Object[length];
            int i = 0;
            while (rs.next()) {
                E e = resultStream.receive();
                if (e != null)
                    array[i++] = e;
            }
            rs.close();
            return array;
        }
        return null;
    }
}
