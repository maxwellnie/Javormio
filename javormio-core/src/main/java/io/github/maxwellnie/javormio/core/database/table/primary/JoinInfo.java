package io.github.maxwellnie.javormio.core.database.table.primary;

import io.github.maxwellnie.javormio.core.database.table.TableInfo;
import io.github.maxwellnie.javormio.core.database.table.column.ColumnInfo;

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
            , String tableName, Class<?> mappingClass, String primaryKeyFieldName, String[] uniqueKeys
            , String[] indexKeys, JoinInfo[] joinInfo, String masterKey, String slaveKey, TableInfo masterTable) {
        super(columnInfoMapping, columnInfoInverseMapping, tableName, mappingClass, primaryKeyFieldName, uniqueKeys, indexKeys, joinInfo);
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
