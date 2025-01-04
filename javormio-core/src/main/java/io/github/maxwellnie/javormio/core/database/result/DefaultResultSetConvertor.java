package io.github.maxwellnie.javormio.core.database.result;

import java.sql.ResultSet;
import java.util.List;

/**
 * @author Maxwell Nie
 */
public class DefaultResultSetConvertor implements ResultSetConvertor{
    @Override
    public Object convert(ResultSet resultSet, TypeMapping typeMapping) {
        if(typeMapping.isMultipleTableJoined())
            return multipleTableConvert(resultSet, typeMapping);
        return simpleConvert(resultSet, typeMapping);
    }
    private Object simpleConvert(ResultSet resultSet, TypeMapping typeMapping) {
        return null;
    }
    private Object multipleTableConvert(ResultSet resultSet, TypeMapping typeMapping) {
        return null;
    }
    @Override
    public Object convert(List<ResultSet> resultSet, TypeMapping typeMapping) {
        return null;
    }
}
