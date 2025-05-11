package io.github.maxwellnie.javormio.flexible.sql.plugin;

import io.github.maxwellnie.javormio.common.java.type.TypeHandler;
import io.github.maxwellnie.javormio.core.translation.table.column.ColumnInfo;

import java.util.Map;

/**
 * @author Maxwell Nie
 */
@FunctionalInterface
public interface LambdaSqlExpression {
    void apply(SqlBuilder sqlBuilder, Map<ColumnInfo, String> aliasMap, Map<Class, TypeHandler> typeHandlerMap, ColumnInfo columnInfo);
}
