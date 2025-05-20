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
public class SimpleStatementHelper implements StatementHelper<Statement>{
    @Override
    public Statement createStatement(Connection connection, ExecutableSql executableSql) throws SQLException {
        return null;
    }

    @Override
    public Statement createBatchStatement(Connection connection, ExecutableSql executableSql) throws SQLException {
        return null;
    }

    @Override
    public int update(Statement statement) throws SQLException {
        return 0;
    }

    @Override
    public int[] batchUpdate(Statement statement) throws SQLException {
        return new int[0];
    }

    @Override
    public <R> R query(ResultSetConvertor<R> convertor, Statement statement) throws ResultParseException {
        return null;
    }
}
