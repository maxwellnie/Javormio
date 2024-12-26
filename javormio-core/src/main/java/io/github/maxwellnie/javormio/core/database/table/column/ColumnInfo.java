package io.github.maxwellnie.javormio.core.database.table.column;

import io.github.maxwellnie.javormio.core.database.table.TableInfo;
import io.github.maxwellnie.javormio.core.java.reflect.property.impl.meta.MetaProperty;
import io.github.maxwellnie.javormio.core.java.type.TypeHandler;

/**
 * @author Maxwell Nie
 */
public class ColumnInfo {
    private String columnName;
    private MetaProperty metaProperty;
    private TableInfo tableInfo;
    private TypeHandler typeHandler;

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public MetaProperty getMetaProperty() {
        return metaProperty;
    }

    public void setMetaProperty(MetaProperty metaProperty) {
        this.metaProperty = metaProperty;
    }

    public TableInfo getTableInfo() {
        return tableInfo;
    }

    public void setTableInfo(TableInfo tableInfo) {
        this.tableInfo = tableInfo;
    }

    public TypeHandler getTypeHandler() {
        return typeHandler;
    }

    public void setTypeHandler(TypeHandler typeHandler) {
        this.typeHandler = typeHandler;
    }
}
