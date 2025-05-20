package io.github.maxwellnie.javormio.core.execution.statement;

import io.github.maxwellnie.javormio.core.execution.executor.parameter.ExecutableSql;
import io.github.maxwellnie.javormio.core.execution.result.ResultParseException;
import io.github.maxwellnie.javormio.core.execution.result.ResultSetConvertor;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author Maxwell Nie
 */
public interface StatementHelper<T extends Statement>{
    /**
     * 创建预处理表单
     *
     * @param connection
     * @param executableSql
     * @return T
     */
    T createStatement(Connection connection, ExecutableSql executableSql) throws SQLException;
    /**
     * 创建预处理表单
     *
     * @param connection
     * @param executableSql
     * @return T
     */
    T createBatchStatement(Connection connection, ExecutableSql executableSql) throws SQLException;
    /**
     * 执行更新
     *
     * @param statement
     * @return int
     */
    int update(T statement) throws SQLException;
    /**
     * 执行批量更新
     *
     * @param statement
     * @return int[]
     */
    int[] batchUpdate(T statement) throws SQLException;
    /**
     * 执行查询
     *
     * @param convertor
     * @param statement
     * @return Object
     */
    <R> R query(ResultSetConvertor<R> convertor, T statement) throws ResultParseException, SQLException;
}
