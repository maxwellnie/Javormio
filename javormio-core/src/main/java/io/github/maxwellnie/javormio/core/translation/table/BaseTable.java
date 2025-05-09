package io.github.maxwellnie.javormio.core.translation.table;

import io.github.maxwellnie.javormio.core.translation.table.column.ColumnInfo;
import io.github.maxwellnie.javormio.core.translation.table.primary.PrimaryInfo;

/**
 * @author Maxwell Nie
 */
public abstract class BaseTable<E> {
    public final String tableName;
    public final String defaultDataSourceName;
    public final Class<E> mappingClass;

    public BaseTable(String tableName, String defaultDataSourceName, Class<E> mappingClass) {
        this.tableName = tableName;
        this.defaultDataSourceName = defaultDataSourceName;
        this.mappingClass = mappingClass;
    }
    public abstract ColumnInfo<E, ?>[] getColumnInfos();
    public abstract PrimaryInfo<E, ?>[] getPrimaryInfos();
}
