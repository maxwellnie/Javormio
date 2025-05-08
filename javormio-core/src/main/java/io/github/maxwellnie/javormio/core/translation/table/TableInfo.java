package io.github.maxwellnie.javormio.core.translation.table;

import io.github.maxwellnie.javormio.common.utils.StringUtils;
import io.github.maxwellnie.javormio.core.translation.table.column.ColumnInfo;
import io.github.maxwellnie.javormio.core.translation.table.primary.JoinInfo;

import java.util.Map;

/**
 * 表信息
 *
 * @author Maxwell Nie
 */
public class TableInfo<E> {
    /**
     * 列信息映射，key为字段名，value为列信息
     */
    private final Map<String, ColumnInfo<E, ?>> columnInfoMapping;
    /**
     * 列信息逆向映射，key为列名，value为列信息
     */
    private final Map<String, ColumnInfo<E, ?>> columnInfoInverseMapping;
    /**
     * 表名
     */
    private final String tableName;
    /**
     * 映射类
     */
    private final Class<E> mappingClass;
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
    private final JoinInfo<E, ?>[] joinInfo;

    public TableInfo(Map<String, ColumnInfo<E, ?>> columnInfoMapping, Map<String, ColumnInfo<E, ?>> columnInfoInverseMapping
            , String tableName, Class<E> mappingClass, String[] columnNames, String primaryKeyFieldName, String[] uniqueKeys
            , String[] indexKeys, JoinInfo<E, ?>[] joinInfo) {
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

    public Map<String, ColumnInfo<E, ?>> getColumnInfoMapping() {
        return columnInfoMapping;
    }

    public Map<String, ColumnInfo<E, ?>> getColumnInfoInverseMapping() {
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

    public JoinInfo<E, ?>[] getJoinInfo() {
        return joinInfo;
    }

}
