package io.github.maxwellnie.javormio;

/**
 * @author Maxwell Nie
 */
public abstract class BaseTable {
    public final String tableName;
    public final String defaultDataSourceName;

    public BaseTable(String tableName, String defaultDataSourceName) {
        this.tableName = tableName;
        this.defaultDataSourceName = defaultDataSourceName;
    }
}
