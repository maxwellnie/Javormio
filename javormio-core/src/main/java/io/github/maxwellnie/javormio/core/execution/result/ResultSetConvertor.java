package io.github.maxwellnie.javormio.core.execution.result;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author Maxwell Nie
 */
public interface ResultSetConvertor<T> {
    T convert(Statement statement) throws SQLException, ResultParseException;
}
