package io.github.maxwellnie.javormio.common.java.jdbc.datasource;

import java.sql.Connection;
import java.util.Map;

/**
 * @author Maxwell Nie
 */
public class SpringDataBaseModelManager extends DefaultDataBaseModelManager {
    public SpringDataBaseModelManager(String defaultDataSourceName, Map<Object, DataBaseModel> dataSources) {
        super(defaultDataSourceName, dataSources);
    }

    public SpringDataBaseModelManager(String defaultDataSourceName) {
        super(defaultDataSourceName);
    }

    @Override
    public Connection getConnection() {
        return new SpringConnectionWrapper(getCurrentDataSource().getDataSource());
    }
}
