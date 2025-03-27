package io.github.maxwellnie.javormio.framework.core.executor.result;

import java.sql.ResultSet;
import java.util.List;

/**
 * @author Maxwell Nie
 */
public class DefaultResultSetConvertor implements ResultSetConvertor{
    private Object simpleConvert(ResultSet resultSet, TypeMapping typeMapping) {
        return null;
    }
    private Object multipleTableConvert(ResultSet resultSet, TypeMapping typeMapping) {
        return null;
    }
    @Override
    public Object convert(ResultSet resultSet, TypeMapping typeMapping, boolean multipleTable) {
        if(multipleTable)
            return multipleTableConvert(resultSet, typeMapping);
        return simpleConvert(resultSet, typeMapping);
    }

    @Override
    public Object convert(List<ResultSet> resultSet, TypeMapping typeMapping, boolean multipleTable) {
        return null;
    }
}
