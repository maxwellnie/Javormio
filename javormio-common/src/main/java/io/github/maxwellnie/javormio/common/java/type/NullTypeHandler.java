package io.github.maxwellnie.javormio.common.java.type;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Maxwell Nie
 */
public class NullTypeHandler implements TypeHandler<Object> {
    public static final NullTypeHandler INSTANCE = new NullTypeHandler();

    @Override
    public Object getValue(ResultSet rs, int index) throws SQLException {
        return null;
    }

    @Override
    public void setValue(PreparedStatement preparedStatement, int index, Object t) throws SQLException {
        preparedStatement.setNull(index, java.sql.Types.NULL);
    }
}
