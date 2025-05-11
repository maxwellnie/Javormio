package io.github.maxwellnie.javormio.flexible.sql.plugin.table;

import io.github.maxwellnie.javormio.core.translation.table.column.ColumnType;

/**
 * @author Maxwell Nie
 */
public class MetaColumn {
    public String fieldName;
    public String typeName;
    public String columnName;
    public String typeHandlerClassName;
    public int columnType;
    public String getterClassName;
    public String setterClassName;
    public String keyGeneratorClassName;
    public boolean isPrimary(){
        return (columnType & ColumnType.PRIMARY) != 0;
    }
}
