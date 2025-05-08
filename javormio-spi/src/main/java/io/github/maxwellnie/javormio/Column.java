package io.github.maxwellnie.javormio;

import io.github.maxwellnie.javormio.core.translation.table.column.ColumnType;

/**
 * @author Maxwell Nie
 */
public class Column {
    public String fieldName;
    public String typeName;
    public String columnName;
    public String typeHandlerClassName;
    public int columnType;
    public String getterClassName;
    public String setterClassName;
    public boolean isPrimary(){
        return (columnType & ColumnType.PRIMARY) != 0;
    }
}
