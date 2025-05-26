package io.github.maxwellnie.javormio.flexible.sql.plugin.function;

import io.github.maxwellnie.javormio.common.java.reflect.property.MetaField;
import io.github.maxwellnie.javormio.core.translation.sql.SqlFragment;
import io.github.maxwellnie.javormio.common.java.table.column.ColumnInfo;

/**
 * @author Maxwell Nie
 */
public class SqlFunctionColumn<E, T> {
    protected SqlFunction<T> sqlFunction;
    protected MetaField<E, T> metaField;
    protected String alias;

    public SqlFunctionColumn(SqlFunction<T> sqlFunction, MetaField<E, T> metaField, String alias) {
        this.sqlFunction = sqlFunction;
        this.metaField = metaField;
        this.alias = alias;
    }

    public SqlFunctionColumn(SqlFunction<T> sqlFunction, String alias) {
        this.sqlFunction = sqlFunction;
        this.alias = alias;
    }

    public static <E, T> SqlFunctionColumn<E, T> of(SqlFunction<T> sqlFunction, MetaField<E, T> metaField, String alias) {
        return new SqlFunctionColumn<>(sqlFunction, metaField, alias);
    }

    public static <E, T> SqlFunctionColumn<E, T> of(SqlFunction<T> sqlFunction, String alias) {
        return new SqlFunctionColumn<>(sqlFunction, null, alias);
    }

    public SqlFunction<T> getSqlFunction() {
        return sqlFunction;
    }

    public MetaField<E, T> getMetaField() {
        return metaField;
    }

    public String getAlias() {
        return alias;
    }

    public SqlFunctionColumnInfo<E, T> toColumnInfo() {
        SqlFunctionColumnInfo<E, T> columnInfo = new SqlFunctionColumnInfo<>(sqlFunction.toSqlFragment());
        columnInfo.setMetaField(metaField);
        columnInfo.setTypeHandler(sqlFunction.getResultTypeHandler());
        return columnInfo;
    }

    public static class SqlFunctionColumnInfo<E, T> extends ColumnInfo<E, T> {
        protected SqlFragment function;

        public SqlFunctionColumnInfo(SqlFragment function) {
            this.function = function;
            this.setColumnName(function.toSql());
        }

        public SqlFragment getFunction() {
            return function;
        }
    }
}
