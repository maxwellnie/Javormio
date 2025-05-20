package io.github.maxwellnie.javormio.flexible.sql.plugin.expression;

import io.github.maxwellnie.javormio.common.annotation.document.ExtensionPoint;
import io.github.maxwellnie.javormio.common.java.type.NullTypeHandler;
import io.github.maxwellnie.javormio.common.java.type.TypeHandler;
import io.github.maxwellnie.javormio.core.translation.SqlParameter;
import io.github.maxwellnie.javormio.flexible.sql.plugin.SqlBuilder;


@ExtensionPoint
public class SqlExpressionSupport {

    public <T> SqlBuilder eq(SqlBuilder sqlBuilder, String columnName, T value, TypeHandler<T> typeHandler, boolean valueIsSql) {
        if (valueIsSql)
            sqlBuilder.append(" " + columnName + " = " + value);
        else
            sqlBuilder.append(" " + columnName + " = ?", getSqlParameter(value, typeHandler));
        return sqlBuilder;
    }

    public <T> SqlBuilder notEq(SqlBuilder sqlBuilder, String columnName, T value, TypeHandler<T> typeHandler, boolean valueIsSql) {
        if (valueIsSql)
            sqlBuilder.append(" " + columnName + " <> " + value);
        else
            sqlBuilder.append(" " + columnName + " <> ?", getSqlParameter(value, typeHandler));
        return sqlBuilder;
    }

    public <T> SqlBuilder greater(SqlBuilder sqlBuilder, String columnName, T value, TypeHandler<T> typeHandler, boolean valueIsSql) {
        if (valueIsSql)
            sqlBuilder.append(" " + columnName + " > " + value);
        else
            sqlBuilder.append(" " + columnName + " > ?", getSqlParameter(value, typeHandler));
        return sqlBuilder;
    }

    public <T> SqlBuilder less(SqlBuilder sqlBuilder, String columnName, T value, TypeHandler<T> typeHandler, boolean valueIsSql) {
        if (valueIsSql)
            sqlBuilder.append(" " + columnName + " < " + value);
        else
            sqlBuilder.append(" " + columnName + " < ?", getSqlParameter(value, typeHandler));
        return sqlBuilder;
    }

    public <T> SqlBuilder greaterEqual(SqlBuilder sqlBuilder, String columnName, T value, TypeHandler<T> typeHandler, boolean valueIsSql) {
        if (valueIsSql)
            sqlBuilder.append(" " + columnName + " >= " + value);
        else
            sqlBuilder.append(" " + columnName + " >= ?", getSqlParameter(value, typeHandler));
        return sqlBuilder;
    }

    public <T> SqlBuilder lessEqual(SqlBuilder sqlBuilder, String columnName, T value, TypeHandler<T> typeHandler, boolean valueIsSql) {
        if (valueIsSql)
            sqlBuilder.append(" " + columnName + " <= " + value);
        else
            sqlBuilder.append(" " + columnName + " <= ?", getSqlParameter(value, typeHandler));
        return sqlBuilder;
    }

    public <T> SqlBuilder between(SqlBuilder sqlBuilder, String columnName, T value1, T value2, TypeHandler<T> typeHandler) {
        sqlBuilder.append(" " + columnName + " BETWEEN ? AND ?",
                getSqlParameter(value1, typeHandler),
                getSqlParameter(value2, typeHandler));
        return sqlBuilder;
    }

    public <T> SqlBuilder notBetween(SqlBuilder sqlBuilder, String columnName, T value1, T value2, TypeHandler<T> typeHandler) {
        sqlBuilder.append(" " + columnName + " NOT BETWEEN ? AND ?",
                getSqlParameter(value1, typeHandler),
                getSqlParameter(value2, typeHandler));
        return sqlBuilder;
    }

    public <T> SqlBuilder like(SqlBuilder sqlBuilder, String columnName, T value, TypeHandler<String> typeHandler, int mode) {
        sqlBuilder.append(" " + columnName + " LIKE ?");
        appendValue(sqlBuilder, value, typeHandler, mode);
        return sqlBuilder;
    }

    public <T> SqlBuilder notLike(SqlBuilder sqlBuilder, String columnName, T value, TypeHandler<String> typeHandler, int mode) {
        sqlBuilder.append(" " + columnName + " NOT LIKE ?");
        appendValue(sqlBuilder, value, typeHandler, mode);
        return sqlBuilder;
    }

    protected <T> void appendValue(SqlBuilder sqlBuilder, T value, TypeHandler<String> typeHandler, int mode) {
        String formattedValue;
        switch (mode) {
            case 0:
                formattedValue = "%" + value + "%";
                break;
            case 1:
                formattedValue = "%" + value;
                break;
            case 2:
                formattedValue = value + "%";
                break;
            default:
                throw new IllegalArgumentException("mode must be 0, 1, 2");
        }
        sqlBuilder.append(getSqlParameter(formattedValue, typeHandler));
    }

    public <T> SqlBuilder in(SqlBuilder sqlBuilder, String columnName, T[] values, TypeHandler<T> typeHandler) {
        StringBuilder sql = new StringBuilder();
        sql.append(" ").append(columnName).append(" IN (");
        return appendValues(sqlBuilder, values, typeHandler, sql);
    }

    public <T> SqlBuilder notIn(SqlBuilder sqlBuilder, String columnName, T[] values, TypeHandler<T> typeHandler) {
        StringBuilder sql = new StringBuilder();
        sql.append(" ").append(columnName).append(" NOT IN (");
        return appendValues(sqlBuilder, values, typeHandler, sql);
    }

    protected <T> SqlBuilder appendValues(SqlBuilder sqlBuilder, T[] values, TypeHandler<T> typeHandler, StringBuilder sql) {
        for (int i = 0; i < values.length; i++) {
            sql.append("?");
            if (i != values.length - 1) sql.append(",");
        }
        sql.append(")");
        sqlBuilder.append(sql.toString(), getSqlParameters(values, typeHandler));
        return sqlBuilder;
    }

    public <T> SqlBuilder is(SqlBuilder sqlBuilder, String columnName, T value, TypeHandler<T> typeHandler, boolean valueIsSql) {
        if (valueIsSql)
            sqlBuilder.append(" " + columnName + " IS " + value);
        else
            sqlBuilder.append(" " + columnName + " IS ?", getSqlParameter(value, typeHandler));
        return sqlBuilder;
    }

    public <T> SqlBuilder isNot(SqlBuilder sqlBuilder, String columnName, T value, TypeHandler<T> typeHandler, boolean valueIsSql) {
        if (valueIsSql)
            sqlBuilder.append(" " + columnName + " IS NOT " + value);
        else
            sqlBuilder.append(" " + columnName + " IS NOT ?", getSqlParameter(value, typeHandler));
        return sqlBuilder;
    }

    public <T> SqlBuilder not(SqlBuilder sqlBuilder, String columnName, T value, TypeHandler<T> typeHandler, boolean valueIsSql) {
        if (valueIsSql)
            sqlBuilder.append(" " + columnName + " NOT " + value);
        else
            sqlBuilder.append(" " + columnName + " NOT ?", getSqlParameter(value, typeHandler));
        return sqlBuilder;
    }

    public SqlBuilder isNull(SqlBuilder sqlBuilder, String columnName) {
        sqlBuilder.append(" " + columnName + " IS NULL");
        return sqlBuilder;
    }

    public SqlBuilder notNull(SqlBuilder sqlBuilder, String columnName) {
        sqlBuilder.append(" " + columnName + " IS NOT NULL");
        return sqlBuilder;
    }


    public SqlBuilder and(SqlBuilder sqlBuilder) {
        sqlBuilder.append(" AND");
        return sqlBuilder;
    }

    public SqlBuilder or(SqlBuilder sqlBuilder) {
        sqlBuilder.append(" OR");
        return sqlBuilder;
    }

    protected <T> SqlParameter getSqlParameter(T value, TypeHandler<T> typeHandler) {
        return new SqlParameter(value, typeHandler != null ? typeHandler : NullTypeHandler.INSTANCE);
    }

    protected <T> SqlParameter[] getSqlParameters(T[] values, TypeHandler<T> typeHandler) {
        SqlParameter[] sqlParameters = new SqlParameter[values.length];
        for (int i = 0; i < values.length; i++) {
            sqlParameters[i] = getSqlParameter(values[i], typeHandler);
        }
        return sqlParameters;
    }
}
