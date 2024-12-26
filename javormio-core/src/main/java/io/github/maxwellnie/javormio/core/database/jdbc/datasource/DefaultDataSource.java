package io.github.maxwellnie.javormio.core.database.jdbc.datasource;

import io.github.maxwellnie.javormio.core.database.jdbc.connection.ConnectionResource;
import io.github.maxwellnie.javormio.core.database.transaction.StackTransactionObject;
import io.github.maxwellnie.javormio.core.database.transaction.TransactionObject;
import io.github.maxwellnie.javormio.core.utils.SystemClock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 默认数据源
 *
 * @author Maxwell Nie
 */
public class DefaultDataSource implements DynamicMultipleDataSource {
    private final ThreadLocal<String> currentDataSourceName = new ThreadLocal<>();
    private final Map<Object, DataSource> dataSourceMap = new ConcurrentHashMap<>();
    private final String defaultDataSourceName;
    private final SystemClock systemClock;
    private final ThreadLocal<TransactionObject> currentTransactionObject = new ThreadLocal<>();
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultDataSource.class);
    public DefaultDataSource(String defaultDataSourceName, DataSource defaultDataSource, SystemClock systemClock) {
        this.defaultDataSourceName = defaultDataSourceName;
        register(defaultDataSourceName, defaultDataSource);
        this.systemClock = systemClock;
    }

    @Override
    public DataSource getCurrentDataSource() {
        return dataSourceMap.get(currentDataSourceName.get());
    }

    @Override
    public DataSource getDefaultDataSource() {
        return dataSourceMap.get(defaultDataSourceName);
    }

    @Override
    public boolean setCurrentDataSource(String dataSourceName) {
        this.currentDataSourceName.set(defaultDataSourceName);
        return true;
    }

    @Override
    public boolean removeCurrentDataSource() {
        currentDataSourceName.remove();
        return true;
    }

    @Override
    public ConnectionResource getConnection() throws SQLException {
        String dataSourceName = this.currentDataSourceName.get();
        DataSource dataSource = getCurrentDataSource();
        TransactionObject transactionObject;
        if (currentTransactionObject.get() == null)
            transactionObject = new StackTransactionObject();
        else
            transactionObject = currentTransactionObject.get();
        return new ConnectionResource(dataSourceName, dataSource, dataSource.getConnection(), transactionObject);
    }

    @Override
    public ConnectionResource getConnection(boolean autoCommit) throws SQLException {
        ConnectionResource connectionResource = getConnection();
        connectionResource.setAutoCommit(autoCommit);
        return connectionResource;
    }

    @Override
    public ConnectionResource getConnection(long timeout) throws SQLException {
        String dataSourceName = this.currentDataSourceName.get();
        DataSource dataSource = getCurrentDataSource();
        TransactionObject transactionObject;
        if (currentTransactionObject.get() == null)
            transactionObject = new StackTransactionObject(timeout, systemClock.now());
        else {
            LOGGER.warn("TransactionObject has set expire time,so resetting is failed now.");
            transactionObject = currentTransactionObject.get();
        }
        currentTransactionObject.set(transactionObject);
        return new ConnectionResource(dataSourceName, dataSource, dataSource.getConnection(), transactionObject);
    }

    @Override
    public ConnectionResource getConnection(boolean autoCommit, long timeout) throws SQLException {
        ConnectionResource connectionResource = getConnection(timeout);
        connectionResource.setAutoCommit(autoCommit);
        return connectionResource;
    }

    @Override
    public Collection<DataSource> getDataSources() {
        return this.dataSourceMap.values();
    }

    @Override
    public TransactionObject getTransactionObject() {
        return currentTransactionObject.get();
    }

    @Override
    public TransactionObject computeIfAbsent(long timeout) {
        TransactionObject transactionObject = this.currentTransactionObject.get();
        if (transactionObject == null) {
            transactionObject = new StackTransactionObject(timeout, systemClock.now());
            this.currentTransactionObject.set(transactionObject);
        }
        return transactionObject;
    }

    @Override
    public void register(Object key, DataSource object) {
        this.dataSourceMap.put(key, object);
    }

    public DefaultDataSource addDataSource(Object key, DataSource object) {
        register(key, object);
        return this;
    }

    @Override
    public DataSource get(Object key) {
        return dataSourceMap.get(key);
    }

}
