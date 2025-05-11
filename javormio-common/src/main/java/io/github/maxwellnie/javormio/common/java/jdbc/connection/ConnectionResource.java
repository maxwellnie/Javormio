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
public interface ConnectionResource<D extends DataSource, C extends Connection> extends Resource, AutoCloseable {

    /**
     * 获取数据源名称
     *
     * @return String
     */
    String getDataSourceName();
    /**
     * 添加事务
     *
     * @param cacheKey
     * @return TransactionObject.AtomicTransaction
     * @throws SQLException
     */
    TransactionObject.AtomicTransaction addTransaction(CacheKey cacheKey) throws SQLException;

    /**
     * 是否关闭
     *
     * @return boolean
     */
    boolean isClosed();
    /**
     * 获取连接
     *
     * @return Connection
     */
    C getConnection() ;

    /**
     * 获取事务对象
     *
     * @return TransactionObject
     */
    TransactionObject getTransactionDesc();

    /**
     * 设置自动提交
     *
     * @param autoCommit
     * @throws SQLException
     */
    void setAutoCommit(boolean autoCommit) throws SQLException;

    /**
     * 获取自动提交
     *
     * @return boolean
     * @throws SQLException
     */
    boolean autoCommit() throws SQLException ;

    /**
     * 获取数据源
     *
     * @return DataSource
     */
    D getDataSource();
}
