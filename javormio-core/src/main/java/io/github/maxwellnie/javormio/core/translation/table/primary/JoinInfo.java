package io.github.maxwellnie.javormio.core.translation.table.primary;

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

    public JoinInfo(Map<String, ColumnInfo<E2, ?>> columnInfoMapping, Map<String, ColumnInfo<E2, ?>> columnInfoInverseMapping
            , String tableName, Class<E2> mappingClass, String[] columnNames, String primaryKeyFieldName, String[] uniqueKeys
            , String[] indexKeys, JoinInfo<E2, ?>[] joinInfo, String masterKey, String slaveKey, TableInfo<E1> masterTable) {
        super(columnInfoMapping, columnInfoInverseMapping, tableName, mappingClass, columnNames, primaryKeyFieldName, uniqueKeys, indexKeys, joinInfo);
        this.masterKey = masterKey;
        this.slaveKey = slaveKey;
        this.masterTable = masterTable;
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
