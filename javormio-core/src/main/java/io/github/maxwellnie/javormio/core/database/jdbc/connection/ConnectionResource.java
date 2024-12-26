package io.github.maxwellnie.javormio.core.database.jdbc.connection;

import io.github.maxwellnie.javormio.core.cache.HashCacheKey;
import io.github.maxwellnie.javormio.core.database.transaction.TransactionObject;
import io.github.maxwellnie.javormio.core.java.api.Resource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * 数据库连接资源
 *
 * @author Maxwell Nie
 */
public class ConnectionResource implements Resource, AutoCloseable {
    boolean isClosed;
    DataSource dataSource;
    Connection connection;
    String dataSourceName;
    TransactionObject transactionObject;

    public ConnectionResource(String dataSourceName, DataSource dataSource, Connection connection, TransactionObject transactionObject) {
        this.dataSourceName = dataSourceName;
        this.dataSource = dataSource;
        this.connection = connection;
        this.transactionObject = transactionObject;
    }

    public String getDataSourceName() {
        return dataSourceName;
    }

    public TransactionObject.AtomicTransaction addTransaction(HashCacheKey hashCacheKey) throws SQLException {
        if (this.isClosed)
            throw new SQLException("Statement is closed.");
        if (connection.getAutoCommit())
            return null;
        TransactionObject.AtomicTransaction tx = new TransactionObject.AtomicTransaction(connection, dataSource, hashCacheKey);
        this.transactionObject.appendAtomicTransaction(tx);
        return tx;
    }

    public boolean isClosed() {
        return isClosed;
    }

    public Connection getConnection() {
        return connection;
    }

    public TransactionObject getTransactionDesc() {
        return transactionObject;
    }

    public void setAutoCommit(boolean autoCommit) throws SQLException {
        this.connection.setAutoCommit(autoCommit);
    }

    public boolean autoCommit() throws SQLException {
        return this.connection.getAutoCommit();
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    @Override
    public void close() throws Exception {
        this.isClosed = true;
    }
}
