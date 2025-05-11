package io.github.maxwellnie.javormio.common.java.jdbc.connection;

import io.github.maxwellnie.javormio.common.cache.CacheKey;
import io.github.maxwellnie.javormio.common.java.api.Resource;
import io.github.maxwellnie.javormio.common.java.jdbc.TransactionObject;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * 数据库连接资源
 *
 * @author Maxwell Nie
 */
public class JConnectionResource implements ConnectionResource {
    /**
     * 是否关闭
     */
    boolean isClosed;
    /**
     * 数据源
     */
    DataSource dataSource;
    /**
     * 连接
     */
    Connection connection;
    /**
     * 数据源名称
     */
    String dataSourceName;
    /**
     * 事务对象
     */
    TransactionObject transactionObject;

    public JConnectionResource(String dataSourceName, DataSource dataSource, Connection connection, TransactionObject transactionObject) {
        this.dataSourceName = dataSourceName;
        this.dataSource = dataSource;
        this.connection = connection;
        this.transactionObject = transactionObject;
    }

    /**
     * 获取数据源名称
     *
     * @return String
     */
    public String getDataSourceName() {
        return dataSourceName;
    }

    /**
     * 添加事务
     *
     * @param cacheKey
     * @return TransactionObject.AtomicTransaction
     * @throws SQLException
     */
    public TransactionObject.AtomicTransaction addTransaction(CacheKey cacheKey) throws SQLException {
        if (this.isClosed)
            throw new SQLException("Statement is closed.");
        if (connection.getAutoCommit())
            return null;
        TransactionObject.AtomicTransaction tx = new TransactionObject.AtomicTransaction(connection, dataSource, cacheKey);
        this.transactionObject.appendAtomicTransaction(tx);
        return tx;
    }

    /**
     * 是否关闭
     *
     * @return boolean
     */
    public boolean isClosed() {
        return isClosed;
    }

    /**
     * 获取连接
     *
     * @return Connection
     */
    public Connection getConnection() {
        return connection;
    }

    /**
     * 获取事务对象
     *
     * @return TransactionObject
     */
    public TransactionObject getTransactionDesc() {
        return transactionObject;
    }

    /**
     * 设置自动提交
     *
     * @param autoCommit
     * @throws SQLException
     */
    public void setAutoCommit(boolean autoCommit) throws SQLException {
        this.connection.setAutoCommit(autoCommit);
    }

    /**
     * 获取自动提交
     *
     * @return boolean
     * @throws SQLException
     */
    public boolean autoCommit() throws SQLException {
        return this.connection.getAutoCommit();
    }

    /**
     * 获取数据源
     *
     * @return DataSource
     */
    public DataSource getDataSource() {
        return dataSource;
    }

    /**
     * 关闭
     *
     * @throws Exception
     */
    @Override
    public void close() throws Exception {
        this.isClosed = true;
        this.connection.close();
    }
}
