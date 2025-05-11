package io.github.maxwellnie.javormio.core.translation.table;

import io.github.maxwellnie.javormio.common.java.proxy.invocation.MethodInvoker;
import io.github.maxwellnie.javormio.core.translation.table.column.ColumnInfo;
import io.github.maxwellnie.javormio.core.translation.table.primary.PrimaryInfo;

/**
 * @author Maxwell Nie
 */
public abstract class BaseMetaTableInfo<E> {
    public final String tableName;
    public final String defaultDataSourceName;
    public final Class<E> mappingClass;
    public final MethodInvoker<E, E> instanceInvoker;

    public BaseMetaTableInfo(String tableName, String defaultDataSourceName, Class<E> mappingClass, MethodInvoker<E, E> instanceInvoker) {
        this.tableName = tableName;
        this.defaultDataSourceName = defaultDataSourceName;
        this.mappingClass = mappingClass;
        this.instanceInvoker = instanceInvoker;
    }

    public abstract ColumnInfo<E, ?>[] getColumnInfos();
    public abstract PrimaryInfo<E, ?>[] getPrimaryInfos();
}
