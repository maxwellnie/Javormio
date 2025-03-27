package io.github.maxwellnie.javormio.framework.core.translation.table.primary;

import io.github.maxwellnie.javormio.framework.core.translation.table.TableInfo;
import io.github.maxwellnie.javormio.framework.core.translation.table.column.ColumnInfo;

import java.util.Map;

/**
 * 表关联信息
 *
 * @author Maxwell Nie
 */
public class JoinInfo extends TableInfo {
    /**
     * 主表
     */
    private final TableInfo masterTable;
    /**
     * 主表关联字段
     */
    private final String masterKey;
    /**
     * 从表关联字段
     */
    private final String slaveKey;

    public JoinInfo(Map<String, ColumnInfo> columnInfoMapping, Map<String, ColumnInfo> columnInfoInverseMapping
            , String tableName, Class<?> mappingClass, String[] columnNames, String primaryKeyFieldName, String[] uniqueKeys
            , String[] indexKeys, JoinInfo[] joinInfo, String masterKey, String slaveKey, TableInfo masterTable) {
        super(columnInfoMapping, columnInfoInverseMapping, tableName, mappingClass, columnNames, primaryKeyFieldName, uniqueKeys, indexKeys, joinInfo);
        this.masterKey = masterKey;
        this.slaveKey = slaveKey;
        this.masterTable = masterTable;
    }

    public TableInfo getMasterTable() {
        return masterTable;
    }

    public String getMasterKey() {
        return masterKey;
    }

    public String getSlaveKey() {
        return slaveKey;
    }
}
