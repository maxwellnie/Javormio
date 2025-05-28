package io.github.maxwellnie.javormio.core.translation.name;

import io.github.maxwellnie.javormio.common.java.sql.ColumnEscapeKit;
import io.github.maxwellnie.javormio.common.java.table.BaseMetaTableInfo;
import io.github.maxwellnie.javormio.common.java.table.column.ColumnInfo;
import io.github.maxwellnie.javormio.common.java.table.column.ColumnType;
import io.github.maxwellnie.javormio.core.translation.sql.SqlExpressionException;

import java.util.Map;

/**
 * @author Maxwell Nie
 */
public class ColumnNameHandler {
    public static String getColumnName(ColumnInfo columnInfo, Map<ColumnInfo, String> columnAliases, Map<BaseMetaTableInfo, String> tableAliases, ColumnEscapeKit columnEscapeKit) {
        if (columnInfo.getColumnName() == null) {
            String alias = columnAliases.get(columnInfo);
            if (alias == null)
                throw new SqlExpressionException("The column name of \"" + columnInfo + "\" is not found,if you used sql function as column alias, please add alias to sql function");
            if (columnEscapeKit.isEscape(alias))
                alias = columnEscapeKit.escape(alias);
            return alias;
        }
        String tableName = tableAliases.get(columnInfo.getTable());
        if (tableName == null) {
            tableName = columnInfo.getTable().tableName;
        }
        String columnName = columnInfo.getColumnName();
        if ((columnInfo.getColumnType()& ColumnType.ESCAPING)!=0)
            columnName = columnEscapeKit.escape(columnName);
        return tableName + "." + columnName;
    }
}
