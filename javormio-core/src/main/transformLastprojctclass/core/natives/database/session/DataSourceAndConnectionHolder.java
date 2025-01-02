package com.maxwellnie.velox.sql.core.natives.database.session;

import com.maxwellnie.velox.sql.core.natives.database.transaction.impl.jdbc.Connections;

import java.sql.SQLException;

/**
 * @author Maxwell Nie
 */
public interface DataSourceAndConnectionHolder {
    Connections.DataSourceAndConnection getDataSourceAndConnection() throws SQLException;
}
