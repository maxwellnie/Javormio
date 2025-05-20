package io.github.maxwellnie.javormio.core.execution.executor;

import io.github.maxwellnie.javormio.common.java.api.JavormioException;
import io.github.maxwellnie.javormio.core.execution.ExecutionException;
import io.github.maxwellnie.javormio.core.execution.executor.parameter.ExecutableSql;
import io.github.maxwellnie.javormio.core.execution.executor.parameter.ExecutorParameters;
import io.github.maxwellnie.javormio.core.execution.result.ResultSetConvertor;
import io.github.maxwellnie.javormio.core.execution.statement.StatementHelper;
import io.github.maxwellnie.javormio.core.translation.SqlType;
import io.github.maxwellnie.javormio.core.translation.table.primary.KeyGenerator;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;

/**
 * @author Maxwell Nie
 */
public class SingleSqlExecutor implements SqlExecutor {
    @Override
    public <T extends Statement, E> E query(ExecutorParameters<T, E> executorParameters) {
        ErrorInstance errorInstance = ErrorInstance.instance();
        ExecutableSql executableSql = executorParameters.getExecutableSql();
        StatementHelper<T> statementHelper = executorParameters.getStatementHelper();
        ResultSetConvertor<E> resultSetConvertor = executorParameters.getResultSetConvertor();
        try (Connection connection = executorParameters.getConnection();T statement = openStatement(statementHelper, executableSql, connection)){
            return statementHelper.query(resultSetConvertor, statement);
        } catch (SQLException e) {
            errorInstance.setSQLError(e);
            errorInstance.setSql(executableSql.getSql());
            errorInstance.setParametersList(executableSql.getParametersList());
            throw new ExecutionException(errorInstance.toString());
        }catch (JavormioException e){
            errorInstance.setSqlErrorMsg(e.getMessage());
            errorInstance.setCause(e);
            throw new ExecutionException(errorInstance.toString());
        }
    }

    protected <T extends Statement> T openStatement(StatementHelper<T> statementHelper, ExecutableSql executableSql, Connection connection) throws SQLException {
        int type = executableSql.getType();
        if (SqlType.isBatch(type))
            return statementHelper.createStatement(connection, executableSql);
        else
            return statementHelper.createBatchStatement(connection, executableSql);
    }

    @Override
    public <T extends Statement> int update(ExecutorParameters<T, ?> executorParameters) {
        ErrorInstance errorInstance = ErrorInstance.instance();
        StatementHelper<T> statementHelper = executorParameters.getStatementHelper();
        ExecutableSql executableSql = executorParameters.getExecutableSql();
        KeyGenerator[] keyGenerators = executableSql.getKeyGenerators();
        T statement = null;
        try (Connection connection = executorParameters.getConnection()){
            if (SqlType.isInsert(executableSql.getType())){
                beforeInsert(executableSql, keyGenerators);
            }

            statement =  openStatement(statementHelper, executableSql, connection);
            int rowCount = statementHelper.update(statement);
            if (SqlType.isInsert(executableSql.getType())){
                afterInsert(executableSql, keyGenerators, statement);
            }
            return rowCount;
        } catch (SQLException e) {
            errorInstance.setSQLError(e);
            errorInstance.setSql(executableSql.getSql());
            errorInstance.setParametersList(executableSql.getParametersList());
            throw new ExecutionException(errorInstance.toString());
        }catch (JavormioException e){
            errorInstance.setSqlErrorMsg(e.getMessage());
            errorInstance.setCause(e);
            throw new ExecutionException(errorInstance.toString());
        }finally {
            try {
                if (statement != null)
                    statement.close();
            } catch (SQLException ignored) {
            }
        }
    }
    @SuppressWarnings("unchecked")
    private <T extends Statement> void afterInsert(ExecutableSql executableSql, KeyGenerator[] keyGenerators, T statement) throws SQLException {
        Collection<?> keyGeneratorParameters = executableSql.getKeyGeneratorParameters();
        if (keyGeneratorParameters != null){
            for (KeyGenerator keyGenerator : keyGenerators){
                if (keyGenerator.isAcceptGenerateKeys())
                    keyGenerator.afterInsert(keyGeneratorParameters, statement);
                else
                    keyGenerator.afterInsert(keyGeneratorParameters, null);
            }
        }
    }
    @Override
    public <T extends Statement> int[] batchUpdate(ExecutorParameters<T, ?> executorParameters) {
        ErrorInstance errorInstance = ErrorInstance.instance();
        StatementHelper<T> statementHelper = executorParameters.getStatementHelper();
        ExecutableSql executableSql = executorParameters.getExecutableSql();
        T statement = null;
        try(Connection connection = executorParameters.getConnection()) {
            KeyGenerator[] keyGenerators = executableSql.getKeyGenerators();
            if (SqlType.isInsert(executableSql.getType())) {
                beforeInsert(executableSql, keyGenerators);
            }
            statement = openStatement(statementHelper, executableSql, connection);
            int[] updateCounts = statementHelper.batchUpdate(statement);
            if (SqlType.isInsert(executableSql.getType())) {
                afterInsert(executableSql, keyGenerators, statement);
            }
            return updateCounts;
        } catch (SQLException e) {
            errorInstance.setSQLError(e);
            errorInstance.setSql(executableSql.getSql());
            errorInstance.setParametersList(executableSql.getParametersList());
            throw new ExecutionException(errorInstance.toString());
        }catch (JavormioException e){
            errorInstance.setSqlErrorMsg(e.getMessage());
            errorInstance.setCause(e);
            throw new ExecutionException(errorInstance.toString());
        }finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException ignored) {
                }
            }
        }
    }
    @SuppressWarnings("unchecked")
    private void beforeInsert(ExecutableSql executableSql, KeyGenerator[] keyGenerators) {
        Collection<?> keyGeneratorParameters = executableSql.getKeyGeneratorParameters();
        if (keyGeneratorParameters != null) {
            for (KeyGenerator keyGenerator : keyGenerators) {
                keyGenerator.beforeInsert(keyGeneratorParameters);
            }
        }
    }
}
