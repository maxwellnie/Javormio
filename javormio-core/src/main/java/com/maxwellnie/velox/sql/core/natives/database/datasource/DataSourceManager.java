package com.maxwellnie.velox.sql.core.natives.database.datasource;

import com.maxwellnie.velox.sql.core.natives.registry.Registry;

import javax.sql.DataSource;

/**
 * @author Maxwell Nie
 */
public class DataSourceManager {
    public static final String REGISTRY_NAME = "velox-sql:data-source:";
    public static final String DEFAULT_NAME = "default";
    private static final Registry REGISTRY = Registry.INSTANCE;

    public static DataSource getDataSource(String name) {
        return REGISTRY.getValue(REGISTRY_NAME + name);
    }

    public static void register(String name, DataSource dataSource) {
        REGISTRY.register(REGISTRY_NAME + name, dataSource);
    }

    public static void registerDefault(DataSource dataSource) {
        register(DEFAULT_NAME, dataSource);
    }

    public static DataSource getDefault() {
        return getDataSource(DEFAULT_NAME);
    }
}
