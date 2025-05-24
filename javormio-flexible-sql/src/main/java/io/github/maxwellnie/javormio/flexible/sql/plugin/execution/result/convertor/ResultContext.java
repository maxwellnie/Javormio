package io.github.maxwellnie.javormio.flexible.sql.plugin.execution.result.convertor;

import io.github.maxwellnie.javormio.core.execution.executor.SqlExecutor;
import io.github.maxwellnie.javormio.core.execution.executor.parameter.ExecutorParameters;
import io.github.maxwellnie.javormio.core.execution.result.ResultParseException;
import io.github.maxwellnie.javormio.core.translation.table.column.ColumnInfo;
import io.github.maxwellnie.javormio.flexible.sql.plugin.expression.SqlExpressionSupport;
import io.github.maxwellnie.javormio.flexible.sql.plugin.table.ExpressionColumnInfo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * 结果集
 *
 * @author Maxwell Nie
 */
public class ResultContext {
    protected Map<ColumnInfo, Integer> columnIndexMap;
    protected ResultSet resultSet;
    protected List<ColumnInfo> baseColumnInfos;
    protected Map<ColumnInfo, String> columnAliases;
    protected SqlExecutor sqlExecutor;
    protected ExecutorParameters executorParameters;

    public ResultContext(Map<ColumnInfo, Integer> columnIndexMap, ResultSet resultSet, List<ColumnInfo> baseColumnInfos, Map<ColumnInfo, String> columnAliases, SqlExecutor sqlExecutor, ExecutorParameters executorParameters) {
        this.columnIndexMap = columnIndexMap;
        this.resultSet = resultSet;
        this.baseColumnInfos = baseColumnInfos;
        this.columnAliases = columnAliases;
        this.sqlExecutor = sqlExecutor;
        this.executorParameters = executorParameters;
    }

    public int getColumnIndex(ColumnInfo columnInfo) throws SQLException {
        Integer index = columnIndexMap.get(columnInfo);
        if (index == null) {
            String columnName = columnAliases.get(columnInfo);
            if (columnName == null)
                columnName = columnInfo.getColumnName();
            index = resultSet.findColumn(columnName);
            columnIndexMap.put(columnInfo, index);
        }
        return index;
    }

    public <E, T> T getColumnValue(ColumnInfo<E, T> columnInfo) throws ResultParseException {
        try {
            int index = getColumnIndex(columnInfo);
            return columnInfo.getTypeHandler().getValue(resultSet, index);
        } catch (SQLException e) {
            throw new ResultParseException(e);
        }
    }

    public <E, T> T getColumnValue(ExpressionColumnInfo<? extends SqlExpressionSupport,E, T> expressionColumnInfo) throws ResultParseException {
        return getColumnValue(expressionColumnInfo.getColumnInfo());
    }

    public Map<ColumnInfo, Integer> getColumnIndexMap() {
        return columnIndexMap;
    }

    public ResultSet getResultSet() {
        return resultSet;
    }

    public Map<ColumnInfo, String> getColumnAliases() {
        return columnAliases;
    }

    public ResultsIterator iterator() {
        return new ResultsIterator(this);
    }

    public SqlExecutor getSqlExecutor() {
        return sqlExecutor;
    }

    public ExecutorParameters getExecutorParameters() {
        return executorParameters;
    }

    public List<ColumnInfo> getBaseColumnInfos() {
        return baseColumnInfos;
    }

    /**
     * 结果集迭代器
     *
     * @author Maxwell Nie
     */
    public static class ResultsIterator implements Iterator<ResultContext> {
        final ResultContext resultContext;

        ResultsIterator(ResultContext resultContext) {
            this.resultContext = resultContext;
        }

        @Override
        public boolean hasNext() {
            try {
                return !resultContext.getResultSet().isLast();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public ResultContext next() {
            try {
                resultContext.getResultSet().next();
                return resultContext;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public void remove() {
            try {
                resultContext.getResultSet().deleteRow();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public void forEachRemaining(Consumer<? super ResultContext> action) {
            while (true) {
                try {
                    if (!resultContext.getResultSet().next()) break;
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                action.accept(next());
            }
        }

        public ResultContext getExecutionResults() {
            return resultContext;
        }
    }
}
