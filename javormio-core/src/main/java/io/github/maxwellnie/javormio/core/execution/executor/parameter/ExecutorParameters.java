package io.github.maxwellnie.javormio.core.execution.executor.parameter;

import io.github.maxwellnie.javormio.core.execution.result.ResultSetConvertor;
import io.github.maxwellnie.javormio.core.execution.statement.StatementHelper;
import io.github.maxwellnie.javormio.core.translation.method.DaoMethodFeature;

import java.sql.Connection;
import java.sql.Statement;
import java.util.Map;

/**
 * @author Maxwell Nie
 */
public class ExecutorParameters<T extends Statement, E> {
    /**
     * 连接资源
     */
    Connection connection;
    /**
     * 执行的sql
     */
    ExecutableSql executableSql;
    /**
     * 结果集转换器
     */
    ResultSetConvertor<E> resultSetConvertor;
    /**
     * 语句助手
     */
    StatementHelper<T> statementHelper;
    /**
     * 命名空间
     */
     String namespace;
    /**
     * 其他参数
     */
    Map<String, Object> properties;

    public ExecutorParameters(Connection connection, ExecutableSql executableSql,
                              ResultSetConvertor<E> resultSetConvertor, StatementHelper<T> statementHelper,
                              String namespace) {
        this.connection = connection;
        this.executableSql = executableSql;
        this.resultSetConvertor = resultSetConvertor;
        this.statementHelper = statementHelper;
        this.namespace = namespace;
    }

    public ExecutorParameters(Connection connection, ExecutableSql executableSql,
                              ResultSetConvertor<E> resultSetConvertor, StatementHelper<T> statementHelper,
                              String namespace, Map<String, Object> properties) {
        this.connection = connection;
        this.executableSql = executableSql;
        this.resultSetConvertor = resultSetConvertor;
        this.statementHelper = statementHelper;
        this.namespace = namespace;
        this.properties = properties;
    }

    public ExecutorParameters() {
    }


    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public ExecutableSql getExecutableSql() {
        return executableSql;
    }

    public void setExecutableSql(ExecutableSql executableSql) {
        this.executableSql = executableSql;
    }

    public ResultSetConvertor<E> getResultSetConvertor() {
        return resultSetConvertor;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setResultSetConvertor(ResultSetConvertor<E> resultSetConvertor) {
        this.resultSetConvertor = resultSetConvertor;
    }
    public StatementHelper<T> getStatementHelper() {
        return statementHelper;
    }

    public void setStatementHelper(StatementHelper<T> statementHelper) {
        this.statementHelper = statementHelper;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }
}
