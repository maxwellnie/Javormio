package io.github.maxwellnie.javormio.core.translation.table.primary;

import io.github.maxwellnie.javormio.common.java.proxy.invocation.MethodInvoker;
import io.github.maxwellnie.javormio.core.translation.table.TableInfo;
import io.github.maxwellnie.javormio.core.translation.table.column.ColumnInfo;

import java.util.Map;

/**
 * 表关联信息
 *
 * @author Maxwell Nie
 */
public class JoinInfo<E1, E2> extends TableInfo<E2> {
    /**
     * 主表
     */
    private final TableInfo<E1> masterTable;
    /**
     * 主表关联字段
     */
    private final String masterKey;
    /**
     * 从表关联字段
     */
    private final String slaveKey;

    public JoinInfo(String tableName, String defaultDataSourceName, Class<E2> mappingClass, MethodInvoker<E2, E2> instanceInvoker, Map<String, ColumnInfo<E2, ?>> columnInfoMapping, Map<String, ColumnInfo<E2, ?>> columnInfoInverseMapping, String tableName1, Class<E2> mappingClass1, JoinInfo<E2, ?>[] joinInfo, TableInfo<E1> masterTable, String masterKey, String slaveKey) {
        super(tableName, defaultDataSourceName, mappingClass, instanceInvoker, columnInfoMapping, columnInfoInverseMapping, tableName1, mappingClass1, joinInfo);
        this.masterTable = masterTable;
        this.masterKey = masterKey;
        this.slaveKey = slaveKey;
    }

    public TableInfo<E1> getMasterTable() {
        return masterTable;
    }

    public String getMasterKey() {
        return masterKey;
    }

    public String getSlaveKey() {
        return slaveKey;
    }
}
