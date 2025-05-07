package io.github.maxwellnie.javormio.core.api.dynamic;

import io.github.maxwellnie.javormio.common.java.reflect.method.Action;
import io.github.maxwellnie.javormio.common.java.reflect.method.Chainable;
import io.github.maxwellnie.javormio.core.api.dynamic.condition.InvalidColumnException;
import io.github.maxwellnie.javormio.core.translation.table.TableInfo;
import io.github.maxwellnie.javormio.core.translation.table.column.ColumnInfo;

/**
 * The where clause of the sql.
 *
 * @author Maxwell Nie
 */
public class Where<E, P> implements Chainable<P> {
    TableInfo tableInfo;
    SqlFragment sqlFragment;
    StringBuilder sqlBuilder;
    ConditionsLogic<E, P, Where<E, P>> lazyConditionLogic;
    P p;
    Action<P, SqlFragment, P> callback;

    public Where(P p, Action<P, SqlFragment, P> callback, TableInfo tableInfo) {
        this.p = p;
        this.callback = callback;
        this.tableInfo = tableInfo;
    }

    /**
     * <code>tb_user.name = ?</code>
     *
     * @param column
     * @param value
     * @param <R>
     * @return ConditionsLogic<E, Where < E>>
     */
    public <R> ConditionsLogic<E, P, Where<E, P>> eq(String column, R value) {
        ColumnInfo columnInfo = tableInfo.getColumnInfoInverseMapping().get(column);
        if (columnInfo == null)
            throw new InvalidColumnException("\'%s\' is not valid column name.", column);
        lazyLoad();
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
    public <R> ConditionsLogic<E, P, Where<E, P>> ne(String column, R value) {
        ColumnInfo columnInfo = tableInfo.getColumnInfoInverseMapping().get(column);
        if (columnInfo == null)
            throw new InvalidColumnException("\'%s\' is not valid column name.", column);
        lazyLoad();
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
    public <R> ConditionsLogic<E, P, Where<E, P>> gt(String column, R value) {
        ColumnInfo columnInfo = tableInfo.getColumnInfoInverseMapping().get(column);
        if (columnInfo == null)
            throw new InvalidColumnException("\'%s\' is not valid column name.", column);
        lazyLoad();
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
    public <R> ConditionsLogic<E, P, Where<E, P>> ge(String column, R value) {
        ColumnInfo columnInfo = tableInfo.getColumnInfoInverseMapping().get(column);
        if (columnInfo == null)
            throw new InvalidColumnException("\'%s\' is not valid column name.", column);
        lazyLoad();
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
    public <R> ConditionsLogic<E, P, Where<E, P>> lt(String column, R value) {
        ColumnInfo columnInfo = tableInfo.getColumnInfoInverseMapping().get(column);
        if (columnInfo == null)
            throw new InvalidColumnException("\'%s\' is not valid column name.", column);
        lazyLoad();
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
    public <R> ConditionsLogic<E, P, Where<E, P>> le(String column, R value) {
        ColumnInfo columnInfo = tableInfo.getColumnInfoInverseMapping().get(column);
        if (columnInfo == null)
            throw new InvalidColumnException("\'%s\' is not valid column name.", column);
        lazyLoad();
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
    public <R> ConditionsLogic<E, P, Where<E, P>> like(String column, R value) {
        ColumnInfo columnInfo = tableInfo.getColumnInfoInverseMapping().get(column);
        if (columnInfo == null)
            throw new InvalidColumnException("\'%s\' is not valid column name.", column);
        lazyLoad();
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
    public <R> ConditionsLogic<E, P, Where<E, P>> notLike(String column, R value) {
        ColumnInfo columnInfo = tableInfo.getColumnInfoInverseMapping().get(column);
        if (columnInfo == null)
            throw new InvalidColumnException("\'%s\' is not valid column name.", column);
        lazyLoad();
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
    public <R> ConditionsLogic<E, P, Where<E, P>> between(String column, R value1, R value2) {
        ColumnInfo columnInfo = tableInfo.getColumnInfoInverseMapping().get(column);
        if (columnInfo == null)
            throw new InvalidColumnException("\'%s\' is not valid column name.", column);
        lazyLoad();
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
    public <R> ConditionsLogic<E, P, Where<E, P>> notBetween(String column, R value1, R value2) {
        ColumnInfo columnInfo = tableInfo.getColumnInfoInverseMapping().get(column);
        if (columnInfo == null)
            throw new InvalidColumnException("\'%s\' is not valid column name.", column);
        lazyLoad();
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
    public <R> ConditionsLogic<E, P, Where<E, P>> isNull(String column) {
        ColumnInfo columnInfo = tableInfo.getColumnInfoInverseMapping().get(column);
        if (columnInfo == null)
            throw new InvalidColumnException("\'%s\' is not valid column name.", column);
        lazyLoad();
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
    public <R> ConditionsLogic<E, P, Where<E, P>> isNotNull(String column) {
        ColumnInfo columnInfo = tableInfo.getColumnInfoInverseMapping().get(column);
        if (columnInfo == null)
            throw new InvalidColumnException("\'%s\' is not valid column name.", column);
        lazyLoad();
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
    public <R> ConditionsLogic<E, P, Where<E, P>> notIn(String column, R[] values) {
        ColumnInfo columnInfo = tableInfo.getColumnInfoInverseMapping().get(column);
        if (columnInfo == null)
            throw new InvalidColumnException("\'%s\' is not valid column name.", column);
        lazyLoad();
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
    public <R> ConditionsLogic<E, P, Where<E, P>> in(String column, Object[] values) {
        ColumnInfo columnInfo = tableInfo.getColumnInfoInverseMapping().get(column);
        if (columnInfo == null)
            throw new InvalidColumnException("\'%s\' is not valid column name.", column);
        lazyLoad();
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

    private ConditionsLogic<E, P, Where<E, P>> lazyConditionLogic() {
        return lazyConditionLogic == null ? lazyConditionLogic = new ConditionsLogic<>(this) : lazyConditionLogic;
    }

    private void lazyLoad() {
        if (sqlFragment == null)
            sqlFragment = new SqlFragment();
        if (sqlBuilder == null)
            sqlBuilder = new StringBuilder();
    }

    @Override
    public P end() {
        if (sqlBuilder != null && sqlFragment != null) {
            String sql = sqlBuilder.toString();
            if (sql.endsWith(" AND"))
                sql = sql.substring(0, sql.length() - 4);
            else if (sql.endsWith(" OR"))
                sql = sql.substring(0, sql.length() - 3);
            sqlFragment.setSql(sql);
            callback.perform(p, sqlFragment);
        }
        return p;
    }
}
