package io.github.maxwellnie.javormio.core.translation.table;

import io.github.maxwellnie.javormio.common.java.table.BaseMetaTableInfo;
import io.github.maxwellnie.javormio.common.java.table.column.ColumnInfo;
import io.github.maxwellnie.javormio.common.java.table.primary.KeyGenerator;
import io.github.maxwellnie.javormio.common.java.table.primary.PrimaryInfo;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * 表信息
 *
 * @author Maxwell Nie
 */
public class TableInfo<E> extends BaseMetaTableInfo<E> {
    /**
     * 列信息映射，key为字段名，value为列信息
     */
    private final Map<String, ColumnInfo<E, ?>> columnInfoMapping;
    /**
     * 列信息逆向映射，key为列名，value为列信息
     */
    private final Map<String, ColumnInfo<E, ?>> columnInfoInverseMapping;

    /**
     * 关联信息
     */
    private final JoinInfo<E, ?>[] joinInfo;
    private final PrimaryInfo<E, ?>[] primaryInfos;
    private final KeyGenerator<E, ?>[] keyGenerators;
    @SuppressWarnings("unchecked")
    public TableInfo(String tableName, String defaultDataSourceName, Class<E> mappingClass, Supplier<E> instanceInvoker, JoinInfo<E, ?>[] joinInfo) {
        super(tableName, defaultDataSourceName, mappingClass, instanceInvoker);
        this.columnInfoMapping = new LinkedHashMap<>();
        this.columnInfoInverseMapping = new LinkedHashMap<>();
        for (ColumnInfo<E, ?> columnInfo : this.getColumnInfos()){
            columnInfoMapping.put(columnInfo.getColumnName(), columnInfo);
            columnInfoInverseMapping.put(columnInfo.getMetaField().getName(), columnInfo);
        }
        this.joinInfo = joinInfo;
        this.primaryInfos = (PrimaryInfo<E, ?>[]) this.columnInfoMapping.values().stream().filter(c->c instanceof PrimaryInfo).toArray();
        this.keyGenerators = (KeyGenerator<E, ?>[]) Arrays.stream(this.primaryInfos).map(PrimaryInfo::getKeyGenerator).toArray();
    }


    public Map<String, ColumnInfo<E, ?>> getColumnInfoMapping() {
        return columnInfoMapping;
    }

    public Map<String, ColumnInfo<E, ?>> getColumnInfoInverseMapping() {
        return columnInfoInverseMapping;
    }

    public JoinInfo<E, ?>[] getJoinInfo() {
        return joinInfo;
    }

    @Override
    public ColumnInfo<E, ?>[] getColumnInfos() {
        return columnInfoMapping.values().toArray(new ColumnInfo[0]);
    }

    @Override
    public PrimaryInfo<E, ?>[] getPrimaryInfos() {
        return primaryInfos;
    }

    @Override
    public KeyGenerator<E, ?>[] getAllKeyGenerators() {
        return keyGenerators;
    }
}
