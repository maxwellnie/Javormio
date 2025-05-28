package io.github.maxwellnie.javormio.core.translation.table;

import io.github.maxwellnie.javormio.common.java.reflect.TypeGraph;
import io.github.maxwellnie.javormio.common.java.reflect.TypeGraphHolder;
import io.github.maxwellnie.javormio.common.java.table.BaseMetaTableInfo;
import io.github.maxwellnie.javormio.common.java.table.column.ColumnInfo;
import io.github.maxwellnie.javormio.common.java.table.column.ColumnType;
import io.github.maxwellnie.javormio.common.java.table.primary.KeyGenerator;
import io.github.maxwellnie.javormio.common.java.table.primary.PrimaryInfo;

import java.util.Arrays;
import java.util.function.Supplier;

/**
 * 表信息
 *
 * @author Maxwell Nie
 */
public class TableInfo<E> extends BaseMetaTableInfo<E> implements TypeGraphHolder {

    private final PrimaryInfo<E, ?>[] primaryInfos;
    private final KeyGenerator<E, ?>[] keyGenerators;
    private final ColumnInfo<E, ?>[] columnInfos;
    @SuppressWarnings("unchecked")
    public TableInfo(String tableName, String defaultDataSourceName, Class<E> mappingClass, Supplier<E> instanceInvoker, ColumnInfo<E, ?>[] columnInfos) {
        super(tableName, defaultDataSourceName, mappingClass, instanceInvoker);
        this.columnInfos = columnInfos;
        this.primaryInfos = Arrays.stream(columnInfos).filter(c->(c.getColumnType()& ColumnType.PRIMARY)!=0).map(c->(PrimaryInfo<E, ?>)c).toArray(PrimaryInfo[]::new);
        this.keyGenerators = Arrays.stream(primaryInfos).filter(c->c.getKeyGenerator()!=null).map(PrimaryInfo::getKeyGenerator).toArray(KeyGenerator[]::new);
    }

    @Override
    public ColumnInfo<E, ?>[] getColumnInfos() {
        return columnInfos;
    }

    @Override
    public PrimaryInfo<E, ?>[] getPrimaryInfos() {
        return primaryInfos;
    }

    @Override
    public KeyGenerator<E, ?>[] getAllKeyGenerators() {
        return keyGenerators;
    }

    @Override
    public TypeGraph getTypeGraph() {
        return null;
    }
}
