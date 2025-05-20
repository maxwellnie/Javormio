package io.github.maxwellnie.javormio.common.java.jdbc.datasource;

import org.springframework.jdbc.datasource.DataSourceUtils;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * @author Maxwell Nie
 */
public class SpringConnectionWrapper extends ConnectionWrapper{
    protected boolean closed = false;
    public SpringConnectionWrapper (DataSource dataSource) {
        super(DataSourceUtils.getConnection(dataSource), dataSource);
    }
    @Override
    public void close() throws SQLException {
        DataSourceUtils.releaseConnection(getConnection(), getDataSource());
        closed = true;
    }

    @Override
    public boolean isClosed() throws SQLException {
        return closed || super.isClosed();
    }
}
