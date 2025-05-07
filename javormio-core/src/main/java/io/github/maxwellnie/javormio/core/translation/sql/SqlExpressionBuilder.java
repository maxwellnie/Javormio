package io.github.maxwellnie.javormio.core.translation.sql;

import io.github.maxwellnie.javormio.common.annotation.document.ExtensionPoint;
import io.github.maxwellnie.javormio.common.java.type.NullTypeHandler;
import io.github.maxwellnie.javormio.common.java.type.TypeHandler;
import io.github.maxwellnie.javormio.core.translation.SqlParameter;

import java.util.Map;

@ExtensionPoint
public class SqlExpressionBuilder {

    public SqlBuilder eq(SqlBuilder sqlBuilder, String columnName, Object value, Map<Class<?>, TypeHandler<?>> typeHandlerMap, boolean valueIsSql) {
        if (valueIsSql)
            sqlBuilder.append(" " + columnName + " = " + value);
        else
            sqlBuilder.append(" " + columnName + " = ?", getSqlParameter(value, typeHandlerMap));
        return sqlBuilder;
    }

    public SqlBuilder notEq(SqlBuilder sqlBuilder, String columnName, Object value, Map<Class<?>, TypeHandler<?>> typeHandlerMap, boolean valueIsSql) {
        if (valueIsSql)
            sqlBuilder.append(" " + columnName + " <> " + value);
        else
            sqlBuilder.append(" " + columnName + " <> ?", getSqlParameter(value, typeHandlerMap));
        return sqlBuilder;
    }

    public SqlBuilder greater(SqlBuilder sqlBuilder, String columnName, Object value, Map<Class<?>, TypeHandler<?>> typeHandlerMap, boolean valueIsSql) {
        if (valueIsSql)
            sqlBuilder.append(" " + columnName + " > " + value);
        else
            sqlBuilder.append(" " + columnName + " > ?", getSqlParameter(value, typeHandlerMap));
        return sqlBuilder;
    }

    public SqlBuilder less(SqlBuilder sqlBuilder, String columnName, Object value, Map<Class<?>, TypeHandler<?>> typeHandlerMap, boolean valueIsSql) {
        if (valueIsSql)
            sqlBuilder.append(" " + columnName + " < " + value);
        else
            sqlBuilder.append(" " + columnName + " < ?", getSqlParameter(value, typeHandlerMap));
        return sqlBuilder;
    }

    public SqlBuilder greaterEqual(SqlBuilder sqlBuilder, String columnName, Object value, Map<Class<?>, TypeHandler<?>> typeHandlerMap, boolean valueIsSql) {
        if (valueIsSql)
            sqlBuilder.append(" " + columnName + " >= " + value);
        else
            sqlBuilder.append(" " + columnName + " >= ?", getSqlParameter(value, typeHandlerMap));
        return sqlBuilder;
    }

    public SqlBuilder lessEqual(SqlBuilder sqlBuilder, String columnName, Object value, Map<Class<?>, TypeHandler<?>> typeHandlerMap, boolean valueIsSql) {
        if (valueIsSql)
            sqlBuilder.append(" " + columnName + " <= " + value);
        else
            sqlBuilder.append(" " + columnName + " <= ?", getSqlParameter(value, typeHandlerMap));
        return sqlBuilder;
    }

    /**
     * ======================================以下方法无需valueIsSql参数=================================
     **/
    public SqlBuilder between(SqlBuilder sqlBuilder, String columnName, Object value1, Object value2, Map<Class<?>, TypeHandler<?>> typeHandlerMap) {
        sqlBuilder.append(" " + columnName + " BETWEEN ? AND ?", getSqlParameter(value1, typeHandlerMap), getSqlParameter(value2, typeHandlerMap));
        return sqlBuilder;
    }

    public SqlBuilder notBetween(SqlBuilder sqlBuilder, String columnName, Object value1, Object value2, Map<Class<?>, TypeHandler<?>> typeHandlerMap) {
        sqlBuilder.append(" " + columnName + " NOT BETWEEN ? AND ?", getSqlParameter(value1, typeHandlerMap), getSqlParameter(value2, typeHandlerMap));
        return sqlBuilder;
    }

    public SqlBuilder like(SqlBuilder sqlBuilder, String columnName, Object value, Map<Class<?>, TypeHandler<?>> typeHandlerMap, int mode) {
        sqlBuilder.append(" " + columnName + " LIKE ?");
        appendValue(sqlBuilder, value, typeHandlerMap, mode);
        return sqlBuilder;
    }

    public SqlBuilder notLike(SqlBuilder sqlBuilder, String columnName, Object value, Map<Class<?>, TypeHandler<?>> typeHandlerMap, int mode) {
        sqlBuilder.append(" " + columnName + " NOT LIKE ?");
        appendValue(sqlBuilder, value, typeHandlerMap, mode);
        return sqlBuilder;
    }

    protected void appendValue(SqlBuilder sqlBuilder, Object value, Map<Class<?>, TypeHandler<?>> typeHandlerMap, int mode) {
        if (mode == 0) {
            sqlBuilder.append(null, getSqlParameter("%" + value + "%", typeHandlerMap));
        } else if (mode == 1) {
            sqlBuilder.append(null, getSqlParameter("%" + value, typeHandlerMap));
        } else if (mode == 2) {
            sqlBuilder.append(null, getSqlParameter(value + "%", typeHandlerMap));
        } else {
            throw new IllegalArgumentException("mode must be 0, 1, 2");
        }
    }

    public SqlBuilder in(SqlBuilder sqlBuilder, String columnName, Object[] values, Map<Class<?>, TypeHandler<?>> typeHandlerMap) {
        StringBuilder sql = new StringBuilder();
        sql.append(" ").append(columnName).append(" IN (");
        return appendValues(sqlBuilder, values, typeHandlerMap, sql);
    }

    public SqlBuilder notIn(SqlBuilder sqlBuilder, String columnName, Object[] values, Map<Class<?>, TypeHandler<?>> typeHandlerMap) {
        StringBuilder sql = new StringBuilder();
        sql.append(" ").append(columnName).append(" NOT IN (");
        return appendValues(sqlBuilder, values, typeHandlerMap, sql);
    }

    protected SqlBuilder appendValues(SqlBuilder sqlBuilder, Object[] values, Map<Class<?>, TypeHandler<?>> typeHandlerMap, StringBuilder sql) {
        for (int i = 0; i < values.length; i++) {
            sql.append("?");
            if (i != values.length - 1) sql.append(",");
        }
        sql.append(")");
        sqlBuilder.append(sql.toString(), getSqlParameters(values, typeHandlerMap));
        return sqlBuilder;
    }

    public SqlBuilder is(SqlBuilder sqlBuilder, String columnName, Object value, Map<Class<?>, TypeHandler<?>> typeHandlerMap, boolean valueIsSql) {
        if (valueIsSql)
            sqlBuilder.append(" " + columnName + " IS " + value);
        else
            sqlBuilder.append(" " + columnName + " IS ?", getSqlParameter(value, typeHandlerMap));
        return sqlBuilder;
    }

    public SqlBuilder isNot(SqlBuilder sqlBuilder, String columnName, Object value, Map<Class<?>, TypeHandler<?>> typeHandlerMap, boolean valueIsSql) {
        if (valueIsSql)
            sqlBuilder.append(" " + columnName + " IS NOT " + value);
        else
            sqlBuilder.append(" " + columnName + " IS NOT ?", getSqlParameter(value, typeHandlerMap));
        return sqlBuilder;
    }

    public SqlBuilder not(SqlBuilder sqlBuilder, String columnName, Object value, Map<Class<?>, TypeHandler<?>> typeHandlerMap, boolean valueIsSql) {
        if (valueIsSql)
            sqlBuilder.append(" " + columnName + " NOT " + value);
        else
            sqlBuilder.append(" " + columnName + " NOT ?", getSqlParameter(value, typeHandlerMap));
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

    public SqlBuilder asc(SqlBuilder sqlBuilder, String columnName) {
        sqlBuilder.append(" " + columnName + " ASC");
        return sqlBuilder;
    }

    public SqlBuilder desc(SqlBuilder sqlBuilder, String columnName) {
        sqlBuilder.append(" " + columnName + " DESC");
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

    protected SqlParameter getSqlParameter(Object value, Map<Class<?>, TypeHandler<?>> typeHandlerMap) {
        TypeHandler<?> typeHandler = value == null ? NullTypeHandler.INSTANCE : typeHandlerMap.get(value.getClass());
        return new SqlParameter(value, typeHandler);
    }

    protected SqlParameter[] getSqlParameters(Object[] values, Map<Class<?>, TypeHandler<?>> typeHandlerMap) {
        SqlParameter[] sqlParameters = new SqlParameter[values.length];
        for (int i = 0; i < values.length; i++) {
            sqlParameters[i] = getSqlParameter(values[i], typeHandlerMap);
        }
        return sqlParameters;
    }
}
