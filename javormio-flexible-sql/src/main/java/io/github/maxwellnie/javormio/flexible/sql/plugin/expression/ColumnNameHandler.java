package io.github.maxwellnie.javormio.flexible.sql.plugin.expression;

import io.github.maxwellnie.javormio.core.translation.table.BaseMetaTableInfo;
import io.github.maxwellnie.javormio.core.translation.table.column.ColumnInfo;

import java.util.Map;

/**
 * @author Maxwell Nie
 */
public class ColumnNameHandler {
    public static String getColumnName(ColumnInfo columnInfo, Map<ColumnInfo, String> columnAliases, Map<BaseMetaTableInfo, String> tableAliases) {
        if (columnInfo.getColumnName() == null) {
            String alias = columnAliases.get(columnInfo);
            alias = alias == null ? columnInfo.getColumnName() : alias;
            if (alias == null)
                throw new SqlExpressionException("The column name of \"" + columnInfo + "\" is not found,if you used sql function as column alias, please add alias to sql function");
        }
        String tableName = tableAliases.get(columnInfo.getTable());
        if (tableName == null) {
            tableName = columnInfo.getTable().tableName;
        }
        String columnName = columnInfo.getColumnName();
        return tableName + "." + columnName;
    }
}
