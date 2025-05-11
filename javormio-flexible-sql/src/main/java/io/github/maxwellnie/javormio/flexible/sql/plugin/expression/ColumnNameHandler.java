package io.github.maxwellnie.javormio.flexible.sql.plugin.expression;

import io.github.maxwellnie.javormio.core.translation.table.BaseMetaTableInfo;
import io.github.maxwellnie.javormio.core.translation.table.column.ColumnInfo;

import java.util.Map;

/**
 * @author Maxwell Nie
 */
public class ColumnNameHandler {
    public static String getColumnName(ColumnInfo columnInfo, Map<ColumnInfo, String> columnAliases, Map<BaseMetaTableInfo, String> tableAliases) {
        String tableName = tableAliases.get(columnInfo.getTable());
        if (tableName == null){
            tableName = columnInfo.getTable().tableName;
        }
        String columnName = columnInfo.getColumnName();
        return tableName + "." + columnName;
    }
}
