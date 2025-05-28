package io.github.maxwellnie.javormio.common.java.sql;

import io.github.maxwellnie.javormio.common.java.table.column.ColumnInfo;

/**
 * @author Maxwell Nie
 */
public interface ColumnEscapeKit {
    default String escape(ColumnInfo columnInfo){
        return escape(columnInfo.getColumnName());
    }
    String escape(String columnName);
    boolean isEscape(String columnName);
    default boolean isEscape(ColumnInfo columnInfo){
        return isEscape(columnInfo.getColumnName());
    }
}
