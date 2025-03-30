package io.github.maxwellnie.javormio.core.execution.result.alpha;

import io.github.maxwellnie.javormio.core.Context;
import io.github.maxwellnie.javormio.core.execution.result.ConvertException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 * @author Maxwell Nie
 */
public interface ResultSetConvertor {
    Object convert(TypeMappingTree typeMappingTree, Map<Object, Integer> columnIndexMap, Context context, ResultSet resultSet) throws ConvertException, SQLException;
}
