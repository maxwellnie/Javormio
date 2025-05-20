package io.github.maxwellnie.javormio.common.java.jdbc.datasource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

/**
 * @author Maxwell Nie
 */
public class SpringDynamicDataSource extends DefaultDynamicDataSource{
    public SpringDynamicDataSource(String defaultDataSourceName, Map<Object, DataSource> dataSources) {
        super(defaultDataSourceName, dataSources);
    }

    public SpringDynamicDataSource(String defaultDataSourceName) {
        super(defaultDataSourceName);
    }

    @Override
    public Connection getConnection() {
        return new SpringConnectionWrapper(getCurrentDataSource());
    }
}
