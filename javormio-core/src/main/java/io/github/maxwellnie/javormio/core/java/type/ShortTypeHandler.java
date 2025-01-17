package io.github.maxwellnie.javormio.core.java.type;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Maxwell Nie
 */
public class ShortTypeHandler implements TypeHandler<Short>{
    @Override
    public Short getValue(ResultSet rs, int index) throws SQLException {
        return rs.getShort(index);
    }

    @Override
    public void setValue(PreparedStatement preparedStatement, int index, Object t) throws SQLException {
        if (t != null){
            if (t instanceof Short)
                preparedStatement.setShort(index, (Short) t);
            else if (t instanceof String)
                preparedStatement.setShort(index, Short.parseShort((String) t));
            else if (t instanceof Number)
                preparedStatement.setShort(index, ((Number) t).shortValue());
            else
                throw new SQLException("The type of the parameter is not Number and Digit String.");
        }else
            preparedStatement.setNull(index, java.sql.Types.SMALLINT);
    }
}
