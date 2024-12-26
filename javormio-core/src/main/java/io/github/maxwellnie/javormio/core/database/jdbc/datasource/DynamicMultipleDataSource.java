package io.github.maxwellnie.javormio.core.database.jdbc.datasource;

import io.github.maxwellnie.javormio.core.database.jdbc.connection.ConnectionResource;
import io.github.maxwellnie.javormio.core.database.transaction.TransactionObject;
import io.github.maxwellnie.javormio.core.java.api.Registry;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Collection;

/**
 * 动态数据源
 *
 * @author Maxwell Nie
 */
public interface DynamicMultipleDataSource extends Registry<Object, DataSource> {
    /**
     * 获取当前使用的数据源
     *
     * @return DataSource
     */
    DataSource getCurrentDataSource();

    /**
     * 获取默认的数据源
     *
     * @return DataSource
     */
    DataSource getDefaultDataSource();

    /**
     * 设置当前使用的数据源
     *
     * @param dataSourceName 数据源名称
     * @return boolean
     */
    boolean setCurrentDataSource(String dataSourceName);

    /**
     * 移除当前使用的数据源
     *
     * @return boolean
     */
    boolean removeCurrentDataSource();

    /**
     * 获取连接
     *
     * @return Connection
     * @throws SQLException
     */
    ConnectionResource getConnection() throws SQLException;

    /**
     * 获取连接
     *
     * @param autoCommit
     * @return Connection
     * @throws SQLException
     */
    ConnectionResource getConnection(boolean autoCommit) throws SQLException;
    /**
     * 获取连接
     *
     * @param timeout
     * @return Connection
     * @throws SQLException
     */
    ConnectionResource getConnection(long timeout) throws SQLException;
    /**
     * 获取连接
     *
     * @param autoCommit
     * @param timeout
     * @return Connection
     * @throws SQLException
     */
    ConnectionResource getConnection(boolean autoCommit, long timeout) throws SQLException;
    /**
     * 获取所有的数据源
     *
     * @return Collection<DataSource>
     */
    Collection<DataSource> getDataSources();

    /**
     * 获取当前线程事务对象
     *
     * @return TransactionObject
     */
    TransactionObject getTransactionObject();

    /**
     * 获取当前线程事务对象
     * @param timeout
     * @return TransactionObject
     */
    TransactionObject computeIfAbsent(long timeout);
}
