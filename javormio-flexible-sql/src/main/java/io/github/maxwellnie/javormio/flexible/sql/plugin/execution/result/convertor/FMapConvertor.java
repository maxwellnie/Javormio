package io.github.maxwellnie.javormio.flexible.sql.plugin.execution.result.convertor;

import io.github.maxwellnie.javormio.core.execution.result.ResultParseException;
import io.github.maxwellnie.javormio.core.execution.result.ResultSetConvertor;
import io.github.maxwellnie.javormio.core.translation.table.column.ColumnInfo;
import io.github.maxwellnie.javormio.flexible.sql.plugin.execution.result.stream.ResultStream;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author Maxwell Nie
 */
public class FMapConvertor implements ResultSetConvertor<List<Map<String, Object>>> {
    protected ResultStream<?> resultStream;

    public FMapConvertor(ResultStream<?> resultStream) {
        this.resultStream = resultStream;
    }

    @Override
    public List<Map<String, Object>> convert(Statement statement) throws SQLException, ResultParseException {
        ResultSet rs = statement.getResultSet();
        if (rs != null) {
            ResultContext resultContext = resultStream.getExecutionResults();
            List<ColumnInfo> columnInfos = resultContext.getBaseColumnInfos();
            Map<ColumnInfo, String> columnAliases = resultContext.getColumnAliases();
            List<Map<String, Object>> list = new LinkedList<>();
            while (rs.next()) {
                Map<String, Object> map = new LinkedHashMap<>();
                for (ColumnInfo columnInfo : columnInfos) {
                    String columnName = columnAliases.get(columnInfo);
                    if (columnName == null)
                        columnName = columnInfo.getColumnName();
                    Object value = columnInfo.getTypeHandler().getValue(rs, resultContext.getColumnIndex(columnInfo));
                    map.put(columnName, value);
                }
                list.add(map);
            }
            return list;
        }
        return null;
    }
}
