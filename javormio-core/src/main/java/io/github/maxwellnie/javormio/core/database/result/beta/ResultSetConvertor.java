package io.github.maxwellnie.javormio.core.database.result.beta;

import io.github.maxwellnie.javormio.core.OperationContext;
import io.github.maxwellnie.javormio.core.database.result.ConvertException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 * @author Maxwell Nie
 */
public interface ResultSetConvertor {
    Object convert(TypeMappingTree typeMappingTree, Map<String, Integer> columnIndexMap, OperationContext context, ResultSet resultSet) throws ConvertException, SQLException;
}
