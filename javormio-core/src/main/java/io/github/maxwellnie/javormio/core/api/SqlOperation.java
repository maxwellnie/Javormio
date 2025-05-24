package io.github.maxwellnie.javormio.core.api;

import io.github.maxwellnie.javormio.core.execution.result.ResultSetConvertor;

import java.util.List;
import java.util.Map;

/**
 * @author Maxwell Nie
 */
public interface SqlOperation {
    <T> T select(String sql, ResultSetConvertor<T> convertor, Object... args);
    int insert(String sql, Object... args);
    int update(String sql, Object... args);
    int delete(String sql, Object... args);
    int[] batchUpdate(String sql, List<Object[]> args);
    int[] batchInsert(String sql, List<Object[]> args);
    int[] batchDelete(String sql, List<Object[]> args);
}
