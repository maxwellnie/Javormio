package io.github.maxwellnie.javormio.flexible.sql.plugin;

import io.github.maxwellnie.javormio.core.translation.table.BaseMetaTableInfo;
import io.github.maxwellnie.javormio.core.translation.table.column.ColumnInfo;
import io.github.maxwellnie.javormio.flexible.sql.plugin.table.ExpressionColumnInfo;

import java.util.LinkedHashMap;

/**
 * @author Maxwell Nie
 */
public class AliasTable {
    protected LinkedHashMap<ColumnInfo, String> columnAliasMap;
    protected LinkedHashMap<BaseMetaTableInfo, String> tableAliasMap;

    public AliasTable() {
        columnAliasMap = new LinkedHashMap<>();
        tableAliasMap = new LinkedHashMap<>();
    }

    public AliasTable aliasColumn(ExpressionColumnInfo columnInfo, String alias) {
        columnAliasMap.put(columnInfo.getColumnInfo(), alias);
        return this;
    }

    public <E> AliasTable aliasTable(BaseMetaTableInfo<E> table, String alias) {
        this.tableAliasMap.put(table, alias);
        return this;
    }


    public LinkedHashMap<ColumnInfo, String> getColumnAliasMap() {
        return columnAliasMap;
    }

    public LinkedHashMap<BaseMetaTableInfo, String> getTableAliasMap() {
        return tableAliasMap;
    }
}
