package io.github.maxwellnie.javormio.flexible.sql.plugin;

import io.github.maxwellnie.javormio.common.java.table.column.ColumnInfo;
import io.github.maxwellnie.javormio.flexible.sql.plugin.table.ExpressionColumnInfo;

import java.util.Map;

/**
 * @author Maxwell Nie
 */
public class AliasHelper {
    protected Map<ColumnInfo, String> aliasMap;

    public AliasHelper(Map<ColumnInfo, String> aliasMap) {
        this.aliasMap = aliasMap;
    }

    public AliasHelper alias(ExpressionColumnInfo expressionColumnInfo, String alias) {
        aliasMap.put(expressionColumnInfo.getColumnInfo(), alias);
        return this;
    }
}
