package io.github.maxwellnie.javormio.flexible.sql.plugin.result;

import io.github.maxwellnie.javormio.core.translation.table.column.ColumnInfo;
import io.github.maxwellnie.javormio.flexible.sql.plugin.table.ExpressionColumnInfo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 * @author Maxwell Nie
 */
public class Tool {
    protected Map<ColumnInfo, Integer> columnIndexMap;
    protected ResultSet resultSet;
    protected Map<ColumnInfo, String> columnAliases;

    public Tool(ResultSet resultSet, Map<ColumnInfo, Integer> columnIndexMap, Map<ColumnInfo, String> columnAliases) {
        this.columnIndexMap = columnIndexMap;
        this.resultSet = resultSet;
        this.columnAliases = columnAliases;
    }

    public int getColumnIndex(ColumnInfo columnInfo) throws SQLException {
        Integer index = columnIndexMap.get(columnInfo);
        if (index == null){
            String columnName = columnAliases.get(columnInfo);
            if (columnName == null)
                columnName = columnInfo.getColumnName();
            index = resultSet.findColumn(columnName);
            columnIndexMap.put(columnInfo, index);
        }
        return index;
    }
    public <E, T> T getColumnValue(ColumnInfo<E, T> columnInfo) throws ResultParseException{
        try{
            int index = getColumnIndex(columnInfo);
            return columnInfo.getTypeHandler().getValue(resultSet, index);
        }catch (SQLException e){
            throw new ResultParseException(e);
        }
    }
    public <E, T> T getColumnValue(ExpressionColumnInfo<E, T> expressionColumnInfo) throws ResultParseException{
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
}
