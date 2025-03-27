package io.github.maxwellnie.javormio.framework.core.translation.table;

import io.github.maxwellnie.javormio.framework.core.translation.table.column.ColumnInfo;
import io.github.maxwellnie.javormio.framework.core.translation.table.primary.JoinInfo;
import io.github.maxwellnie.javormio.framework.common.utils.StringUtils;

import java.util.Map;

/**
 * 表信息
 *
 * @author Maxwell Nie
 */
public class TableInfo {
    /**
     * 列信息映射，key为字段名，value为列信息
     */
    private final Map<String, ColumnInfo> columnInfoMapping;
    /**
     * 列信息逆向映射，key为列名，value为列信息
     */
    private final Map<String, ColumnInfo> columnInfoInverseMapping;
    /**
     * 表名
     */
    private final String tableName;
    /**
     * 映射类
     */
    private final Class<?> mappingClass;
    /**
     * 主键
     */
    private final String primaryKey;
    /**
     * 唯一键
     */
    private final String[] uniqueKeys;
    /**
     * 索引键
     */
    private final String[] indexKeys;
    /**
     * 列名
     */
    private final String[] columnNames;
    /**
     * 主键字段名
     */
    private final String primaryKeyFieldName;
    /**
     * 关联信息
     */
    private final JoinInfo[] joinInfo;

    public TableInfo(Map<String, ColumnInfo> columnInfoMapping, Map<String, ColumnInfo> columnInfoInverseMapping
            , String tableName, Class<?> mappingClass, String[] columnNames, String primaryKeyFieldName, String[] uniqueKeys
            , String[] indexKeys, JoinInfo[] joinInfo) {
        this.columnInfoMapping = columnInfoMapping;
        this.columnInfoInverseMapping = columnInfoInverseMapping;
        this.tableName = tableName;
        this.mappingClass = mappingClass;
        this.columnNames = columnNames;
        this.primaryKeyFieldName = primaryKeyFieldName;
        this.uniqueKeys = uniqueKeys;
        this.indexKeys = indexKeys;
        this.joinInfo = joinInfo;
        if (StringUtils.isNotNullAndEmpty(primaryKeyFieldName)) {
            this.primaryKey = columnInfoMapping.get(primaryKeyFieldName).getColumnName();
        } else
            this.primaryKey = null;
    }

    public String getPrimaryKeyFieldName() {
        return primaryKeyFieldName;
    }

    public Map<String, ColumnInfo> getColumnInfoMapping() {
        return columnInfoMapping;
    }

    public Map<String, ColumnInfo> getColumnInfoInverseMapping() {
        return columnInfoInverseMapping;
    }


    public String getTableName() {
        return tableName;
    }


    public Class<?> getMappingClass() {
        return mappingClass;
    }


    public String getPrimaryKey() {
        return primaryKey;
    }


    public String[] getUniqueKeys() {
        return uniqueKeys;
    }

    public String[] getColumnNames() {
        return columnNames;
    }

    public String[] getIndexKeys() {
        return indexKeys;
    }

    public JoinInfo[] getJoinInfo() {
        return joinInfo;
    }

}
