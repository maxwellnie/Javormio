package io.github.maxwellnie.javormio.core.java.type;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Maxwell Nie
 */
public class LongTypeHandler implements TypeHandler<Long>{
    @Override
    public Long getValue(ResultSet rs, int index) throws SQLException {
        return rs.getLong(index);
    }

    @Override
    public void setValue(PreparedStatement preparedStatement, int index, Object t) throws SQLException {
        if (t != null){
            if (t instanceof String){
                try {
                    t = Long.parseLong((String) t);
                } catch (NumberFormatException e){
                    throw new SQLException("The type of the parameter is not Digit String.");
                }
            } else if (t instanceof Number){
                t = ((Number) t).longValue();
            } else
                throw new SQLException("The type of the parameter is not Number and Digit String.");
        }else
            preparedStatement.setNull(index, java.sql.Types.BIGINT);
    }
}
