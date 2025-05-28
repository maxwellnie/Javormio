package io.github.maxwellnie.javormio.common.java.jdbc.datasource;

import io.github.maxwellnie.javormio.common.java.sql.dialect.Dialect;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Maxwell Nie
 */
public class DefaultDataBaseModelManager implements DataBaseModelManager {
    private final ThreadLocal<String> currentDataSourceName;
    private final Map<Object, DataBaseModel> dataSources;
    private final String defaultDataSourceName;

    public DefaultDataBaseModelManager(final String defaultDataSourceName, final Map<Object, DataBaseModel> dataSources) {
        this.currentDataSourceName = ThreadLocal.withInitial(() -> defaultDataSourceName);
        this.dataSources = dataSources;
        this.defaultDataSourceName = defaultDataSourceName;
    }

    public DefaultDataBaseModelManager(String defaultDataSourceName) {
        this.defaultDataSourceName = defaultDataSourceName;
        this.currentDataSourceName = ThreadLocal.withInitial(() -> defaultDataSourceName);
        this.dataSources = new ConcurrentHashMap<>();
    }

    @Override
    public DataBaseModel getCurrentDataSource() {
        return this.dataSources.get(this.currentDataSourceName.get());
    }

    @Override
    public DataBaseModel getDefaultDataSource() {
        return this.dataSources.get(this.defaultDataSourceName);
    }

    @Override
    public void setCurrentDataSource(String dataSourceName) {
        this.currentDataSourceName.set(dataSourceName);
    }

    @Override
    public void resetCurrentDataSource() {
        this.currentDataSourceName.set(this.defaultDataSourceName);
    }

    @Override
    public Collection<DataBaseModel> getDataSources() {
        return Collections.unmodifiableCollection(this.dataSources.values());
    }

    @Override
    public Dialect getDialect() {
        return null;
    }

    @Override
    public void register(Object key, DataBaseModel object) {
        this.dataSources.put(key, object);
    }

    @Override
    public DataBaseModel get(Object key) {
        return this.dataSources.get(key);
    }

    @Override
    public Connection getConnection() {
        try {
            return dataSources.get(currentDataSourceName.get()).getDataSource().getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
