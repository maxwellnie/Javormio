package io.github.maxwellnie.javormio.core.translation.table;

import io.github.maxwellnie.javormio.common.java.proxy.invocation.MethodInvoker;
import io.github.maxwellnie.javormio.common.utils.StringUtils;
import io.github.maxwellnie.javormio.core.translation.table.column.ColumnInfo;
import io.github.maxwellnie.javormio.core.translation.table.primary.JoinInfo;
import io.github.maxwellnie.javormio.core.translation.table.primary.PrimaryInfo;

import java.util.Map;

/**
 * 表信息
 *
 * @author Maxwell Nie
 */
public class TableInfo<E> extends BaseMetaTableInfo<E>{
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
    @SuppressWarnings("unchecked")
    public TableInfo(String tableName, String defaultDataSourceName, Class<E> mappingClass, MethodInvoker<E, E> instanceInvoker, Map<String, ColumnInfo<E, ?>> columnInfoMapping, Map<String, ColumnInfo<E, ?>> columnInfoInverseMapping, String tableName1, Class<E> mappingClass1, JoinInfo<E, ?>[] joinInfo) {
        super(tableName, defaultDataSourceName, mappingClass, instanceInvoker);
        this.columnInfoMapping = columnInfoMapping;
        this.columnInfoInverseMapping = columnInfoInverseMapping;
        this.joinInfo = joinInfo;
        this.primaryInfos = (PrimaryInfo<E, ?>[]) this.columnInfoMapping.values().stream().filter(c->c instanceof PrimaryInfo).toArray();
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
}
