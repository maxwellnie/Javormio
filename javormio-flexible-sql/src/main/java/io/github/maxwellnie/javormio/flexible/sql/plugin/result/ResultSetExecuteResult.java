package io.github.maxwellnie.javormio.flexible.sql.plugin.result;

import io.github.maxwellnie.javormio.core.execution.ExecutorContext;
import io.github.maxwellnie.javormio.core.execution.StatementWrapper;
import io.github.maxwellnie.javormio.core.translation.table.column.ColumnInfo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author Maxwell Nie
 */
public class ResultSetExecuteResult extends ExecuteResult<Tool, ResultSet> {
    protected StatementWrapper statementWrapper;

    public ResultSetExecuteResult(ResultSet object, ResultSet resultSet, List<ColumnInfo> columnInfos, Map<ColumnInfo, Integer> columnIndexes, Map<ColumnInfo, String> columnAliases, StatementWrapper statementWrapper, ExecutorContext executorContext) throws ResultParseException {
        super(object, resultSet, columnInfos, columnIndexes, columnAliases, executorContext);
        this.statementWrapper = statementWrapper;
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
        }finally {
            finish();
        }
    }

    @Override
    public void finish() {
        try{
            if (resultSet != null && !resultSet.isClosed())
                resultSet.close();
            if (statementWrapper != null && !statementWrapper.isAutoClosed())
                statementWrapper.close();
            if (executorContext != null && executorContext.getConnectionResource() != null){
                executorContext.getConnectionResource().close();
            }
        }catch (Exception e){
            throw new ResultParseException(e);
        }
    }
}
