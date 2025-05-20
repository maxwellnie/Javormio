package io.github.maxwellnie.javormio.core.execution.result;


import io.github.maxwellnie.javormio.common.java.type.TypeHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author Maxwell Nie
 */
public class ValueResultSetConvertor<T> implements ResultSetConvertor<T>{
    protected final TypeHandler<T> typeHandler;

    public ValueResultSetConvertor(TypeHandler<T> typeHandler) {
        this.typeHandler = typeHandler;
    }

    @Override
    public T convert(Statement statement) throws SQLException, ResultParseException {
        if (statement.getResultSet() != null){
            ResultSet rs = statement.getResultSet();
            if (rs != null && rs.next()){
                T t = typeHandler.getValue(rs, 1);
                rs.close();
                return t;
            }
        }
        return null;
    }
}
