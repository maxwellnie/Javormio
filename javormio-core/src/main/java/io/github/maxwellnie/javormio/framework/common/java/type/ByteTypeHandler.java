package io.github.maxwellnie.javormio.framework.common.java.type;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

/**
 * @author Maxwell Nie
 */
public class ByteTypeHandler implements TypeHandler<Byte> {
    @Override
    public Byte getValue(ResultSet rs, int index) throws SQLException {
        return rs.getByte(index);
    }

    @Override
    public void setValue(PreparedStatement preparedStatement, int index, Object t) throws SQLException {
        if (t != null && !(t instanceof Byte)) {
            if (t instanceof String) {
                try {
                    t = Byte.parseByte((String) t);
                } catch (NumberFormatException e) {
                    throw new SQLException("The type of the parameter is not Digit String.");
                }
            } else if (t instanceof Number) {
                t = ((Number) t).byteValue();
            } else
                throw new SQLException("The type of the parameter is not Number or Digit String.");
            preparedStatement.setByte(index, (Byte) t);
        } else
            preparedStatement.setNull(index, Types.TINYINT);
    }
}
