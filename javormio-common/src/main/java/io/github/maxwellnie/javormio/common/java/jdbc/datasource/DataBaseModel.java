package io.github.maxwellnie.javormio.common.java.jdbc.datasource;

import io.github.maxwellnie.javormio.common.java.sql.dialect.Dialect;

import javax.sql.DataSource;

/**
 * @author Maxwell Nie
 */
public class DataBaseModel {
    final DataSource dataSource;
    final Dialect dialect;

    public DataBaseModel(DataSource dataSource, Dialect dialect) {
        this.dataSource = dataSource;
        this.dialect = dialect;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public Dialect getDialect() {
        return dialect;
    }
}
