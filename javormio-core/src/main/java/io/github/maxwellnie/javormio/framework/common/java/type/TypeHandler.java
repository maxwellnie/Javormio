package io.github.maxwellnie.javormio.framework.common.java.type;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Maxwell Nie
 */
public interface TypeHandler<T> {
    T getValue(ResultSet rs, int index) throws SQLException;

    void setValue(PreparedStatement preparedStatement, int index, Object t) throws SQLException;
}
