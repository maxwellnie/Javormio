package io.github.maxwellnie.javormio.core.translation.table.column;

import io.github.maxwellnie.javormio.common.java.reflect.property.MetaField;
import io.github.maxwellnie.javormio.common.java.type.TypeHandler;
import io.github.maxwellnie.javormio.core.translation.table.TableInfo;

/**
 * 列信息
 *
 * @author Maxwell Nie
 */
public class ColumnInfo<E, T> {
    /**
     * 列名
     */
    private String columnName;
    /**
     * 属性
     */
    private MetaField<E, T> metaField;
    /**
     * 表信息
     */
    private TableInfo tableInfo;
    /**
     * 类型处理器
     */
    private TypeHandler<T> typeHandler;

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public MetaField<E, T> getMetaField() {
        return metaField;
    }

    public void setMetaField(MetaField<E, T> metaField) {
        this.metaField = metaField;
    }

    public void setTypeHandler(TypeHandler<T> typeHandler) {
        this.typeHandler = typeHandler;
    }

    public ColumnInfo() {
    }

    public TableInfo getTableInfo() {
        return tableInfo;
    }

    public void setTableInfo(TableInfo tableInfo) {
        this.tableInfo = tableInfo;
    }

    public TypeHandler<T> getTypeHandler() {
        return typeHandler;
    }

}
