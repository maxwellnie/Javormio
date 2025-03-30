package io.github.maxwellnie.javormio.common.java.type;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Maxwell Nie
 */
public class DoubleTypeHandler implements TypeHandler<Double> {
    @Override
    public Double getValue(ResultSet rs, int index) throws SQLException {
        return rs.getDouble(index);
    }

    @Override
    public void setValue(PreparedStatement preparedStatement, int index, Object t) throws SQLException {
        if (t != null) {
            if (t instanceof Double)
                preparedStatement.setDouble(index, (Double) t);
            else if (t instanceof String)
                preparedStatement.setDouble(index, Double.parseDouble((String) t));
            else if (t instanceof Number)
                preparedStatement.setDouble(index, ((Number) t).doubleValue());
            else
                throw new SQLException("The type of the parameter is not Number and Digit String.");
        } else {
            preparedStatement.setNull(index, java.sql.Types.DOUBLE);
        }
    }
}
