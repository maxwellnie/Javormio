package io.github.maxwellnie.javormio.framework.core.api.dynamic;

import io.github.maxwellnie.javormio.framework.core.api.dynamic.condition.InvalidColumnException;
import io.github.maxwellnie.javormio.framework.core.translation.table.TableInfo;
import io.github.maxwellnie.javormio.framework.core.translation.table.column.ColumnInfo;

/**
 * The where clause of the sql.
 *
 * @author Maxwell Nie
 */
public class Where<E> {
    TableInfo tableInfo;
    SqlFragment sqlFragment = new SqlFragment();
    StringBuilder sqlBuilder = new StringBuilder();
    ConditionsLogic<E, Where<E>> lazyConditionLogic;

    /**
     * <code>tb_user.name = ?</code>
     *
     * @param column
     * @param value
     * @param <R>
     * @return ConditionsLogic<E, Where < E>>
     */
    public <R> ConditionsLogic<E, Where<E>> eq(String column, R value) {
        ColumnInfo columnInfo = tableInfo.getColumnInfoInverseMapping().get(column);
        if (columnInfo == null)
            throw new InvalidColumnException("\'%s\' is not valid column name.", column);
        sqlBuilder.append(" ")
                .append(columnInfo.getColumnName())
                .append(" = ?");
        sqlFragment.addParam(value, columnInfo.getTypeHandler());
        return lazyConditionLogic();
    }

    /**
     * <code>tb_user.name != ?</code>
     *
     * @param column
     * @param value
     * @param <R>
     * @return ConditionsLogic <E, Where<E>>
     */
    public <R> ConditionsLogic<E, Where<E>> ne(String column, R value) {
        ColumnInfo columnInfo = tableInfo.getColumnInfoInverseMapping().get(column);
        if (columnInfo == null)
            throw new InvalidColumnException("\'%s\' is not valid column name.", column);
        sqlBuilder.append(" ")
                .append(columnInfo.getColumnName())
                .append(" != ?");
        sqlFragment.addParam(value, columnInfo.getTypeHandler());
        return lazyConditionLogic();
    }

    /**
     * <code>tb_user.name > ?</code>
     *
     * @param column
     * @param value
     * @param <R>
     * @return ConditionsLogic <E, Where<E>>
     */
    public <R> ConditionsLogic<E, Where<E>> gt(String column, R value) {
        ColumnInfo columnInfo = tableInfo.getColumnInfoInverseMapping().get(column);
        if (columnInfo == null)
            throw new InvalidColumnException("\'%s\' is not valid column name.", column);
        sqlBuilder.append(" ")
                .append(columnInfo.getColumnName())
                .append(" > ?");
        sqlFragment.addParam(value, columnInfo.getTypeHandler());
        return lazyConditionLogic();
    }

    /**
     * <code>tb_user.name >= ?</code>
     *
     * @param column
     * @param value
     * @param <R>
     * @return ConditionsLogic <E, Where<E>>
     */
    public <R> ConditionsLogic<E, Where<E>> ge(String column, R value) {
        ColumnInfo columnInfo = tableInfo.getColumnInfoInverseMapping().get(column);
        if (columnInfo == null)
            throw new InvalidColumnException("\'%s\' is not valid column name.", column);
        sqlBuilder.append(" ")
                .append(columnInfo.getColumnName())
                .append(" >= ?");
        sqlFragment.addParam(value, columnInfo.getTypeHandler());
        return lazyConditionLogic();
    }

    /**
     * <code>tb_user.name < ?</code>
     *
     * @param column
     * @param value
     * @param <R>
     * @return ConditionsLogic <E, Where<E>>
     */
    public <R> ConditionsLogic<E, Where<E>> lt(String column, R value) {
        ColumnInfo columnInfo = tableInfo.getColumnInfoInverseMapping().get(column);
        if (columnInfo == null)
            throw new InvalidColumnException("\'%s\' is not valid column name.", column);
        sqlBuilder.append(" ")
                .append(columnInfo.getColumnName())
                .append(" < ?");
        sqlFragment.addParam(value, columnInfo.getTypeHandler());
        return lazyConditionLogic();
    }

    /**
     * <code>tb_user.name <= ?</code>
     *
     * @param column
     * @param value
     * @param <R>
     * @return ConditionsLogic <E, Where<E>>
     */
    public <R> ConditionsLogic<E, Where<E>> le(String column, R value) {
        ColumnInfo columnInfo = tableInfo.getColumnInfoInverseMapping().get(column);
        if (columnInfo == null)
            throw new InvalidColumnException("\'%s\' is not valid column name.", column);
        sqlBuilder.append(" ")
                .append(columnInfo.getColumnName())
                .append(" <= ?");
        sqlFragment.addParam(value, columnInfo.getTypeHandler());
        return lazyConditionLogic();
    }

    /**
     * <code>tb_user.name LIKE ?</code>
     *
     * @param column
     * @param value
     * @param <R>
     * @return ConditionsLogic <E, Where<E>>
     */
    public <R> ConditionsLogic<E, Where<E>> like(String column, R value) {
        ColumnInfo columnInfo = tableInfo.getColumnInfoInverseMapping().get(column);
        if (columnInfo == null)
            throw new InvalidColumnException("\'%s\' is not valid column name.", column);
        sqlBuilder.append(" ")
                .append(columnInfo.getColumnName())
                .append(" LIKE ?");
        sqlFragment.addParam(value, columnInfo.getTypeHandler());
        return lazyConditionLogic();
    }

    /**
     * <code>tb_user.name NOT LIKE ?</code>
     *
     * @param column
     * @param value
     * @param <R>
     * @return ConditionsLogic <E, Where<E>>
     */
    public <R> ConditionsLogic<E, Where<E>> notLike(String column, R value) {
        ColumnInfo columnInfo = tableInfo.getColumnInfoInverseMapping().get(column);
        if (columnInfo == null)
            throw new InvalidColumnException("\'%s\' is not valid column name.", column);
        sqlBuilder.append(" ")
                .append(columnInfo.getColumnName())
                .append(" NOT LIKE ?");
        sqlFragment.addParam(value, columnInfo.getTypeHandler());
        return lazyConditionLogic();
    }

    /**
     * <code>tb_user.name BETWEEN ? AND ?</code>
     *
     * @param column
     * @param value1
     * @param value2
     * @param <R>
     * @return ConditionsLogic <E, Where<E>>
     */
    public <R> ConditionsLogic<E, Where<E>> between(String column, R value1, R value2) {
        ColumnInfo columnInfo = tableInfo.getColumnInfoInverseMapping().get(column);
        if (columnInfo == null)
            throw new InvalidColumnException("\'%s\' is not valid column name.", column);
        sqlBuilder.append(" ")
                .append(columnInfo.getColumnName())
                .append(" BETWEEN ? AND ?");
        sqlFragment.addParam(value1, columnInfo.getTypeHandler());
        sqlFragment.addParam(value2, columnInfo.getTypeHandler());
        return lazyConditionLogic();
    }

    /**
     * <code>tb_user.name NOT BETWEEN ? AND ?</code>
     *
     * @param column
     * @param value1
     * @param value2
     * @param <R>
     * @return ConditionsLogic <E, Where<E>>
     */
    public <R> ConditionsLogic<E, Where<E>> notBetween(String column, R value1, R value2) {
        ColumnInfo columnInfo = tableInfo.getColumnInfoInverseMapping().get(column);
        if (columnInfo == null)
            throw new InvalidColumnException("\'%s\' is not valid column name.", column);
        sqlBuilder.append(" ")
                .append(columnInfo.getColumnName())
                .append(" NOT BETWEEN ? AND ?");
        sqlFragment.addParam(value1, columnInfo.getTypeHandler());
        sqlFragment.addParam(value2, columnInfo.getTypeHandler());
        return lazyConditionLogic();
    }

    /**
     * <code>tb_user.name IS NULL</code>
     *
     * @param column
     * @return ConditionsLogic <E, Where<E>>
     */
    public <R> ConditionsLogic<E, Where<E>> isNull(String column) {
        ColumnInfo columnInfo = tableInfo.getColumnInfoInverseMapping().get(column);
        if (columnInfo == null)
            throw new InvalidColumnException("\'%s\' is not valid column name.", column);
        sqlBuilder.append(" ")
                .append(columnInfo.getColumnName())
                .append(" IS NULL");
        return lazyConditionLogic();
    }

    /**
     * <code>tb_user.name IS NOT NULL</code>
     *
     * @param column
     * @param <R>
     * @return ConditionsLogic <E, Where<E>>
     */
    public <R> ConditionsLogic<E, Where<E>> isNotNull(String column) {
        ColumnInfo columnInfo = tableInfo.getColumnInfoInverseMapping().get(column);
        if (columnInfo == null)
            throw new InvalidColumnException("\'%s\' is not valid column name.", column);
        sqlBuilder.append(" ")
                .append(columnInfo.getColumnName())
                .append(" IS NOT NULL");
        return lazyConditionLogic();
    }

    /**
     * <code>tb_user.name NOT IN (?,?,...)</code>
     *
     * @param column
     * @param values
     * @param <R>
     * @return ConditionsLogic <E, Where<E>>
     */
    public <R> ConditionsLogic<E, Where<E>> notIn(String column, R[] values) {
        ColumnInfo columnInfo = tableInfo.getColumnInfoInverseMapping().get(column);
        if (columnInfo == null)
            throw new InvalidColumnException("\'%s\' is not valid column name.", column);
        sqlBuilder.append(" ")
                .append(columnInfo.getColumnName())
                .append(" NOT IN (");
        for (int i = 0; i < values.length; i++) {
            sqlBuilder.append("?");
            if (i != values.length - 1)
                sqlBuilder.append(",");
            sqlFragment.addParam(values[i], columnInfo.getTypeHandler());
        }
        sqlBuilder.append(")");
        return lazyConditionLogic();
    }

    /**
     * <code>tb_user.name IN (?,?,...)</code>
     *
     * @param column
     * @param values
     * @param <R>
     * @return ConditionsLogic <E, Where<E>>
     */
    public <R> ConditionsLogic<E, Where<E>> in(String column, Object[] values) {
        ColumnInfo columnInfo = tableInfo.getColumnInfoInverseMapping().get(column);
        if (columnInfo == null)
            throw new InvalidColumnException("\'%s\' is not valid column name.", column);
        sqlBuilder.append(" ")
                .append(columnInfo.getColumnName())
                .append(" IN (");
        for (int i = 0; i < values.length; i++) {
            sqlBuilder.append("?");
            if (i != values.length - 1)
                sqlBuilder.append(",");
            sqlFragment.addParam(values[i], columnInfo.getTypeHandler());
        }
        sqlBuilder.append(")");
        return lazyConditionLogic();
    }

    private ConditionsLogic<E, Where<E>> lazyConditionLogic() {
        return lazyConditionLogic == null ? lazyConditionLogic = new ConditionsLogic<>(this) : lazyConditionLogic;
    }

    /**
     * <p>
     * Generate sql.
     * </p>
     *
     * @return SqlFragment
     */
    public SqlFragment ok() {
        String sql = sqlBuilder.toString();
        if (sql.endsWith(" AND"))
            sql = sql.substring(0, sql.length() - 4);
        else if (sql.endsWith(" OR"))
            sql = sql.substring(0, sql.length() - 3);
        sqlFragment.setSql(sql);
        return sqlFragment;
    }
}
