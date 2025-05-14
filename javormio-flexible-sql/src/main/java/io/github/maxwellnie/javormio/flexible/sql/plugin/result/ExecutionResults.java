package io.github.maxwellnie.javormio.flexible.sql.plugin.result;

import io.github.maxwellnie.javormio.core.translation.table.column.ColumnInfo;
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
public class ExecutionResults {
    protected Map<ColumnInfo, Integer> columnIndexMap;
    protected ResultSet resultSet;
    protected List<ColumnInfo> baseColumnInfos;
    protected Map<ColumnInfo, String> columnAliases;
    protected AutoCloseable[] autoCloseableResources;

    public ExecutionResults(ResultSet resultSet, Map<ColumnInfo, Integer> columnIndexMap, List<ColumnInfo> baseColumnInfos, Map<ColumnInfo, String> columnAliases) {
        this.columnIndexMap = columnIndexMap;
        this.resultSet = resultSet;
        this.baseColumnInfos = baseColumnInfos;
        this.columnAliases = columnAliases;
    }

    public ExecutionResults(ResultSet resultSet, Map<ColumnInfo, Integer> columnIndexMap, List<ColumnInfo> baseColumnInfos, Map<ColumnInfo, String> columnAliases, AutoCloseable[] autoCloseableResources) {
        this.columnIndexMap = columnIndexMap;
        this.resultSet = resultSet;
        this.baseColumnInfos = baseColumnInfos;
        this.columnAliases = columnAliases;
        this.autoCloseableResources = autoCloseableResources;
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

    public <E, T> T getColumnValue(ExpressionColumnInfo<E, T> expressionColumnInfo) throws ResultParseException {
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

    public AutoCloseable[] getAutoCloseableResources() {
        return autoCloseableResources;
    }

    public ResultsIterator iterator() {
        return new ResultsIterator(this);
    }

    public List<ColumnInfo> getBaseColumnInfos() {
        return baseColumnInfos;
    }

    /**
     * 结果集迭代器
     *
     * @author Maxwell Nie
     */
    public static class ResultsIterator implements Iterator<ExecutionResults> {
        final ExecutionResults executionResults;

        ResultsIterator(ExecutionResults executionResults) {
            this.executionResults = executionResults;
        }

        @Override
        public boolean hasNext() {
            try {
                return !executionResults.getResultSet().isLast();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public ExecutionResults next() {
            try {
                executionResults.getResultSet().next();
                return executionResults;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public void remove() {
            try {
                executionResults.getResultSet().deleteRow();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public void forEachRemaining(Consumer<? super ExecutionResults> action) {
            while (true) {
                try {
                    if (!executionResults.getResultSet().next()) break;
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                action.accept(next());
            }
        }

        public ExecutionResults getExecutionResults() {
            return executionResults;
        }
    }
}
