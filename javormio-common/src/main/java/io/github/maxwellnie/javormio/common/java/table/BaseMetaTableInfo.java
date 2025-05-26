package io.github.maxwellnie.javormio.common.java.table;

import io.github.maxwellnie.javormio.common.java.table.column.ColumnInfo;
import io.github.maxwellnie.javormio.common.java.table.primary.KeyGenerator;
import io.github.maxwellnie.javormio.common.java.table.primary.PrimaryInfo;

import java.util.function.Supplier;

/**
 * @author Maxwell Nie
 */
public abstract class BaseMetaTableInfo<E> implements Cloneable{
    public final String tableName;
    public final String defaultDataSourceName;
    public final Class<E> mappingClass;
    public final Supplier<E> instanceInvoker;

    public BaseMetaTableInfo(String tableName, String defaultDataSourceName, Class<E> mappingClass, Supplier<E> instanceInvoker) {
        this.tableName = tableName;
        this.defaultDataSourceName = defaultDataSourceName;
        this.mappingClass = mappingClass;
        this.instanceInvoker = instanceInvoker;
    }

    public abstract ColumnInfo<E, ?>[] getColumnInfos();
    public abstract PrimaryInfo<E, ?>[] getPrimaryInfos();
    public abstract KeyGenerator<E, ?>[] getAllKeyGenerators();
}
