package io.github.maxwellnie.javormio.framework.core.execution.result.alpha;

import io.github.maxwellnie.javormio.framework.Context;
import io.github.maxwellnie.javormio.framework.core.execution.result.ConvertException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 * @author Maxwell Nie
 */
public interface ResultSetConvertor {
    Object convert(TypeMappingTree typeMappingTree, Map<Object, Integer> columnIndexMap, Context context, ResultSet resultSet) throws ConvertException, SQLException;
}
