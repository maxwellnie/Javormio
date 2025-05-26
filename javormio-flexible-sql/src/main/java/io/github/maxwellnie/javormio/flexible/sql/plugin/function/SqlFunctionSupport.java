package io.github.maxwellnie.javormio.flexible.sql.plugin.function;

import io.github.maxwellnie.javormio.AbstractColumnExpression;
import io.github.maxwellnie.javormio.common.java.reflect.property.MetaField;
import io.github.maxwellnie.javormio.core.Context;
import io.github.maxwellnie.javormio.common.java.table.column.ColumnInfo;
import io.github.maxwellnie.javormio.flexible.sql.plugin.expression.SqlExpressionSupport;
import io.github.maxwellnie.javormio.flexible.sql.plugin.table.ExpressionColumnInfo;

import java.util.Collection;
import java.util.Iterator;

/**
 * @author Maxwell Nie
 */
public class SqlFunctionSupport {
    protected final Context context;

    public SqlFunctionSupport(Context context) {
        this.context = context;
    }
    public <T extends Number> SqlFunction<T> count(Class<T> returnType, String... columns) {
        return new Count<>(returnType, context, columns);
    }
    public <T extends Number> SqlFunction<T> count(Class<T> returnType, ColumnInfo... columns) {
        String[] columnNames = getColumnNames(columns);
        return new Count<>(returnType, context, columnNames);
    }
    public <T extends Number> SqlFunction<T> count(Class<T> returnType, Collection<? extends ColumnInfo> columns) {
        String[] columnNames = getColumnNames(columns);
        return new Count<>(returnType, context, columnNames);
    }
    public <T extends Number> SqlFunction<T> count(Class<T> returnType, AbstractColumnExpression... columns) {
        String[] columnNames = getColumnNames(columns);
        return count(returnType, columnNames);
    }
    public <E, T extends Number> SqlFunctionColumn<E, T> count(MetaField<E, T> metaField, String alias, String... columns) {
        Count<T> sqlFunction = new Count<>(metaField.getType(), context, columns);
        return SqlFunctionColumn.of(sqlFunction, metaField, alias);
    }
    public <E, T extends Number> SqlFunctionColumn<E, T> count(MetaField<E, T> metaField, String alias, AbstractColumnExpression... columns) {
        String[] columnNames = getColumnNames(columns);
        return count(metaField, alias, columnNames);
    }
    public <E, T extends Number> SqlFunctionColumn<E, T> count(AbstractColumnExpression<E, T> expressionColumnInfo, String... columns) {
        MetaField<E, T> metaField = expressionColumnInfo.getColumnInfo().getMetaField();
        Count<T> sqlFunction = new Count<>(metaField.getType(), expressionColumnInfo.getColumnInfo(), columns);
        return SqlFunctionColumn.of(sqlFunction, metaField, expressionColumnInfo.getColumnInfo().getColumnName());
    }
    public <E, T extends Number> SqlFunctionColumn<E, T> count(AbstractColumnExpression<E, T> expressionColumnInfo, AbstractColumnExpression... columns) {
        String[] columnNames = getColumnNames(columns);
        return count(expressionColumnInfo, columnNames);
    }

    protected String[] getColumnNames(AbstractColumnExpression[] columns) {
        String[] columnNames = new String[columns.length];
        for (int i = 0; i < columns.length; i++) {
            columnNames[i] = columns[i].getColumnInfo().getColumnName();
        }
        return columnNames;
    }
    protected String[] getColumnNames(Collection<? extends ColumnInfo> columns) {
        String[] columnNames = new String[columns.size()];
        Iterator<? extends ColumnInfo>  iterator = columns.iterator();
        for (int i = 0; i < columns.size() && iterator.hasNext(); i++) {
            columnNames[i] = iterator.next().getColumnName();
        }
        return columnNames;
    }
    protected String[] getColumnNames(ColumnInfo[] columns) {
        String[] columnNames = new String[columns.length];
        for (int i = 0; i < columns.length; i++) {
            columnNames[i] = columns[i].getColumnName();
        }
        return columnNames;
    }

    /**
     * 统计字段，不映射字段到实体
     *
     * @param expressionColumnInfo
     * @param columns
     * @param <E>
     * @return SqlFunctionColumn<E, Long>
     */
    public <E> SqlFunctionColumn<E, Long> unMapCount(AbstractColumnExpression<E, ?> expressionColumnInfo, String... columns) {
        Count<Long> sqlFunction = new Count<>(Long.class, context, columns);
        return SqlFunctionColumn.of(sqlFunction, expressionColumnInfo.getColumnInfo().getColumnName());
    }
    public <E> SqlFunctionColumn<E, Long> unMapCount(AbstractColumnExpression<E, ?> expressionColumnInfo, AbstractColumnExpression... columns) {
        String[] columnNames = getColumnNames(columns);
        return unMapCount(expressionColumnInfo, columnNames);
    }
    /**
     * 统计字段，不映射字段到实体
     *
     * @param expressionColumnInfo
     * @param type
     * @param columns
     * @param <E>
     * @param <T>
     * @return SqlFunctionColumn<E, T>
     */
    public <E, T extends Number> SqlFunctionColumn<E, T> unMapCount(AbstractColumnExpression<E, T> expressionColumnInfo, Class<T> type, String... columns) {
        Count<T> sqlFunction = new Count<>(type, context, columns);
        return SqlFunctionColumn.of(sqlFunction, expressionColumnInfo.getColumnInfo().getColumnName());
    }
    public <E, T extends Number> SqlFunctionColumn<E, T> unMapCount(ExpressionColumnInfo<? extends SqlExpressionSupport, E, T> expressionColumnInfo, Class<T> type, AbstractColumnExpression... columns) {
        String[] columnNames = getColumnNames(columns);
        return unMapCount(expressionColumnInfo, type, columnNames);
    }
}
