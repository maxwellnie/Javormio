package io.github.maxwellnie.javormio.flexible.sql.plugin.result;

import io.github.maxwellnie.javormio.core.translation.table.column.ColumnInfo;

import java.sql.ResultSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author Maxwell Nie
 */
public class ResultSetExecuteResult extends ExecuteResult<Tool, ResultSet> {
    public ResultSetExecuteResult(ResultSet object, ResultSet resultSet, List<ColumnInfo> columnInfos, Map<ColumnInfo, Integer> columnIndexes, Map<ColumnInfo, String> columnAliases) {
        super(object, resultSet, columnInfos, columnIndexes, columnAliases);
    }

    @Override
    public Tool parse() {
        return tool;
    }
    public List<Map<String, Object>> collectToMap(){
        try {
            List<Map<String, Object>> list = new LinkedList<>();
            while (resultSet.next()){
                Map<String, Object> map = new LinkedHashMap<>();
                for (ColumnInfo columnInfo : columnInfos){
                    String columnName = tool.columnAliases.get(columnInfo);
                    if (columnName == null)
                        columnName = columnInfo.getColumnName();
                    Object value = columnInfo.getTypeHandler().getValue(resultSet, tool.getColumnIndex(columnInfo));
                    map.put(columnName, value);
                }
                list.add(map);
            }
            return list;
        }catch (Throwable e){
            throw new ResultParseException(e);
        }
    }
}
