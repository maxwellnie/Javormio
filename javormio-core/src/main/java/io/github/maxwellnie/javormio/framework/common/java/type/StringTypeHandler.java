package io.github.maxwellnie.javormio.framework.common.java.type;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Maxwell Nie
 */
public class StringTypeHandler implements TypeHandler<String>{
    @Override
    public String getValue(ResultSet rs, int index) throws SQLException {
        return rs.getString(index);
    }

    @Override
    public void setValue(PreparedStatement preparedStatement, int index, Object t) throws SQLException {
        if (t != null)
            preparedStatement.setString(index, t.toString());
        else
            preparedStatement.setNull(index, java.sql.Types.VARCHAR);
    }
}
