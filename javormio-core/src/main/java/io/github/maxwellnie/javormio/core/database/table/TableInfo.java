package io.github.maxwellnie.javormio.core.database.table;

import io.github.maxwellnie.javormio.core.database.table.column.ColumnInfo;
import io.github.maxwellnie.javormio.core.database.table.primary.JoinInfo;
import io.github.maxwellnie.javormio.core.utils.StringUtils;

import java.util.Map;

/**
 * 表信息
 *
 * @author Maxwell Nie
 */
public class TableInfo {
    private final Map<String, ColumnInfo> columnInfoMapping;
    private final Map<String, ColumnInfo> columnInfoInverseMapping;
    private final String tableName;
    private final Class<?> mappingClass;
    private final String primaryKey;
    private final String[] uniqueKeys;
    private final String[] indexKeys;
    private final String primaryKeyFieldName;
    private final JoinInfo[] joinInfo;

    public TableInfo(Map<String, ColumnInfo> columnInfoMapping, Map<String, ColumnInfo> columnInfoInverseMapping
            , String tableName, Class<?> mappingClass, String primaryKeyFieldName, String[] uniqueKeys
            , String[] indexKeys, JoinInfo[] joinInfo) {
        this.columnInfoMapping = columnInfoMapping;
        this.columnInfoInverseMapping = columnInfoInverseMapping;
        this.tableName = tableName;
        this.mappingClass = mappingClass;
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


    public String[] getIndexKeys() {
        return indexKeys;
    }

    public JoinInfo[] getJoinInfo() {
        return joinInfo;
    }

}
