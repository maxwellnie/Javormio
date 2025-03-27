package io.github.maxwellnie.javormio.framework.core.db.jdbc.connection.factory.impl;

import io.github.maxwellnie.javormio.framework.core.db.jdbc.connection.ConnectionResource;
import io.github.maxwellnie.javormio.framework.core.db.jdbc.connection.factory.ConnectionFactory;
import io.github.maxwellnie.javormio.framework.core.db.jdbc.datasource.DynamicMultipleDataSource;

import java.sql.SQLException;

/**
 * @author Maxwell Nie
 */
public class DefaultConnectionFactory implements ConnectionFactory {
    private final DynamicMultipleDataSource dataSource;
    private volatile boolean autoCommit;
    private volatile boolean closed;

    public DefaultConnectionFactory(DynamicMultipleDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public DefaultConnectionFactory(DynamicMultipleDataSource dataSource, boolean autoCommit) {
        this.dataSource = dataSource;
        this.autoCommit = autoCommit;
    }

    @Override
    public void setAutoCommit(boolean autoCommit) {
        this.autoCommit = autoCommit;
    }

    @Override
    public boolean isClosed() {
        return this.closed;
    }

    @Override
    public ConnectionResource getConnection() throws SQLException {
        return dataSource.getConnection();
    }


    @Override
    public boolean autoCommit() {
        return this.autoCommit;
    }

    @Override
    public void close() {
        this.closed = true;
    }
}
