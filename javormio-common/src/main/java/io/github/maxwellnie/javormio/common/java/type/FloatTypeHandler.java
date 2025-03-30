package io.github.maxwellnie.javormio.common.java.type;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Maxwell Nie
 */
public class FloatTypeHandler implements TypeHandler<Float> {
    @Override
    public Float getValue(ResultSet rs, int index) throws SQLException {
        return rs.getFloat(index);
    }

    @Override
    public void setValue(PreparedStatement preparedStatement, int index, Object t) throws SQLException {
        if (t != null) {
            if (t instanceof Float) {
                preparedStatement.setFloat(index, (Float) t);
            } else if (t instanceof String) {
                try {
                    t = Float.parseFloat((String) t);
                } catch (NumberFormatException e) {
                    throw new SQLException("The type of the parameter is not Digit String.");
                }
                preparedStatement.setFloat(index, (Float) t);
            } else if (t instanceof Number) {
                t = ((Number) t).floatValue();
            } else
                throw new SQLException("The type of the parameter is not Number and Digit String.");
        } else
            preparedStatement.setNull(index, java.sql.Types.FLOAT);
    }
}
