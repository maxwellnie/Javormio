package io.github.maxwellnie.javormio.common.java.type;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Maxwell Nie
 */
public class IntegerTypeHandler implements TypeHandler<Integer> {
    @Override
    public Integer getValue(ResultSet rs, int index) throws SQLException {
        return rs.getInt(index);
    }

    @Override
    public void setValue(PreparedStatement preparedStatement, int index, Object t) throws SQLException {
        if (t != null) {
            if (t instanceof String) {
                try {
                    t = Integer.parseInt((String) t);
                } catch (NumberFormatException e) {
                    throw new SQLException("The type of the parameter is not Digit String.");
                }
            } else if (t instanceof Number) {
                t = ((Number) t).intValue();
            } else
                throw new SQLException("The type of the parameter is not Number and Digit String.");
            preparedStatement.setInt(index, (Integer) t);
        } else
            preparedStatement.setNull(index, java.sql.Types.INTEGER);

    }
}
