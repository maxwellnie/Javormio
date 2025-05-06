package io.github.maxwellnie.javormio.core.translation.table.column;

import io.github.maxwellnie.javormio.common.annotation.document.ExtensionPoint;
import io.github.maxwellnie.javormio.core.translation.sql.SqlBuilder;

@ExtensionPoint
public class DefaultSqlExpressionBuilder {

    public SqlBuilder eq(SqlBuilder sqlBuilder, String columnName, Object value, boolean valueIsSql) {
        if (valueIsSql)
            sqlBuilder.append(" " + columnName + " = " + value);
        else
            sqlBuilder.append(" " + columnName + " = ?", value);
        return sqlBuilder;
    }

    public SqlBuilder notEq(SqlBuilder sqlBuilder, String columnName, Object value, boolean valueIsSql) {
        if (valueIsSql)
            sqlBuilder.append(" " + columnName + " <> " + value);
        else
            sqlBuilder.append(" " + columnName + " <> ?", value);
        return sqlBuilder;
    }

    public SqlBuilder greater(SqlBuilder sqlBuilder, String columnName, Object value, boolean valueIsSql) {
        if (valueIsSql)
            sqlBuilder.append(" " + columnName + " > " + value);
        else
            sqlBuilder.append(" " + columnName + " > ?", value);
        return sqlBuilder;
    }

    public SqlBuilder less(SqlBuilder sqlBuilder, String columnName, Object value, boolean valueIsSql) {
        if (valueIsSql)
            sqlBuilder.append(" " + columnName + " < " + value);
        else
            sqlBuilder.append(" " + columnName + " < ?", value);
        return sqlBuilder;
    }

    public SqlBuilder greaterEqual(SqlBuilder sqlBuilder, String columnName, Object value, boolean valueIsSql) {
        if (valueIsSql)
            sqlBuilder.append(" " + columnName + " >= " + value);
        else
            sqlBuilder.append(" " + columnName + " >= ?", value);
        return sqlBuilder;
    }

    public SqlBuilder lessEqual(SqlBuilder sqlBuilder, String columnName, Object value, boolean valueIsSql) {
        if (valueIsSql)
            sqlBuilder.append(" " + columnName + " <= " + value);
        else
            sqlBuilder.append(" " + columnName + " <= ?", value);
        return sqlBuilder;
    }

    /**======================================以下方法无需valueIsSql参数=================================**/
    public SqlBuilder between(SqlBuilder sqlBuilder, String columnName, Object value1, Object value2) {
        sqlBuilder.append(" " + columnName + " BETWEEN ? AND ?", value1, value2);
        return sqlBuilder;
    }

    public SqlBuilder notBetween(SqlBuilder sqlBuilder, String columnName, Object value1, Object value2) {
        sqlBuilder.append(" " + columnName + " NOT BETWEEN ? AND ?", value1, value2);
        return sqlBuilder;
    }

    public SqlBuilder like(SqlBuilder sqlBuilder, String columnName, Object value, int mode) {
        sqlBuilder.append(" " + columnName + " LIKE ?");
        if (mode == 0) {
            sqlBuilder.append(null, "%"+value+"%");
        } else if (mode == 1) {
            sqlBuilder.append(null, "%"+value);
        } else if (mode == 2) {
            sqlBuilder.append(null, value+"%");
        } else {
            throw new IllegalArgumentException("mode must be 0, 1, 2");
        }
        return sqlBuilder;
    }

    public SqlBuilder notLike(SqlBuilder sqlBuilder, String columnName, Object value, int mode) {
        sqlBuilder.append(" " + columnName + " NOT LIKE ?");
        if (mode == 0) {
            sqlBuilder.append(null, "%"+value+"%");
        } else if (mode == 1) {
            sqlBuilder.append(null, "%"+value);
        } else if (mode == 2) {
            sqlBuilder.append(null, value+"%");
        } else {
            throw new IllegalArgumentException("mode must be 0, 1, 2");
        }
        return sqlBuilder;
    }
    
    public SqlBuilder in(SqlBuilder sqlBuilder, String columnName, Object[] values) {
        StringBuilder sql = new StringBuilder();
        sql.append(" ").append(columnName).append(" IN (");
        for (int i = 0; i < values.length; i++) {
            sql.append("?");
            if (i != values.length - 1) sql.append(",");
        }
        sql.append(")");
        sqlBuilder.append(sql.toString(), values);
        return sqlBuilder;
    }

    public SqlBuilder notIn(SqlBuilder sqlBuilder, String columnName, Object[] values) {
        StringBuilder sql = new StringBuilder();
        sql.append(" ").append(columnName).append(" NOT IN (");
        for (int i = 0; i < values.length; i++) {
            sql.append("?");
            if (i != values.length - 1) sql.append(",");
        }
        sql.append(")");
        sqlBuilder.append(sql.toString(), values);
        return sqlBuilder;
    }

    public SqlBuilder is(SqlBuilder sqlBuilder, String columnName, Object value, boolean valueIsSql) {
        if (valueIsSql)
            sqlBuilder.append(" " + columnName + " IS " + value);
        else
            sqlBuilder.append(" " + columnName + " IS ?", value);
        return sqlBuilder;
    }

    public SqlBuilder isNot(SqlBuilder sqlBuilder, String columnName, Object value, boolean valueIsSql) {
        if (valueIsSql)
            sqlBuilder.append(" " + columnName + " IS NOT " + value);
        else
            sqlBuilder.append(" " + columnName + " IS NOT ?", value);
        return sqlBuilder;
    }

    public SqlBuilder not(SqlBuilder sqlBuilder, String columnName, Object value, boolean valueIsSql) {
        if (valueIsSql)
            sqlBuilder.append(" " + columnName + " NOT " + value);
        else
            sqlBuilder.append(" " + columnName + " NOT ?", value);
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
}
