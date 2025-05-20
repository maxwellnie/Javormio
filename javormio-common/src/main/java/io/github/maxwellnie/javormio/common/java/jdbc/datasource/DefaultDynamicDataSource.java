package io.github.maxwellnie.javormio.common.java.jdbc.datasource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Maxwell Nie
 */
public class DefaultDynamicDataSource implements DynamicDataSource {
    private final ThreadLocal<String> currentDataSourceName;
    private final Map<Object, DataSource> dataSources;
    private final String defaultDataSourceName;

    public DefaultDynamicDataSource(final String defaultDataSourceName, final Map<Object, DataSource> dataSources) {
        this.currentDataSourceName = ThreadLocal.withInitial(() -> defaultDataSourceName);
        this.dataSources = dataSources;
        this.defaultDataSourceName = defaultDataSourceName;
    }

    public DefaultDynamicDataSource(String defaultDataSourceName) {
        this.defaultDataSourceName = defaultDataSourceName;
        this.currentDataSourceName = ThreadLocal.withInitial(() -> defaultDataSourceName);
        this.dataSources = new ConcurrentHashMap<>();
    }

    @Override
    public DataSource getCurrentDataSource() {
        return this.dataSources.get(this.currentDataSourceName.get());
    }

    @Override
    public DataSource getDefaultDataSource() {
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
    public Collection<DataSource> getDataSources() {
        return Collections.unmodifiableCollection(this.dataSources.values());
    }

    @Override
    public void register(Object key, DataSource object) {
        this.dataSources.put(key, object);
    }

    @Override
    public DataSource get(Object key) {
        return this.dataSources.get(key);
    }

    @Override
    public Connection getConnection() {
        try {
            return dataSources.get(currentDataSourceName.get()).getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
