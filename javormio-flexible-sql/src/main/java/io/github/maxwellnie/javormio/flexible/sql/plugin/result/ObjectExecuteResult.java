package io.github.maxwellnie.javormio.flexible.sql.plugin.result;

import io.github.maxwellnie.javormio.core.translation.table.BaseMetaTableInfo;
import io.github.maxwellnie.javormio.core.translation.table.column.ColumnInfo;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

/**
 * @author Maxwell Nie
 */
public class ObjectExecuteResult<T> extends ExecuteResult<T, BaseMetaTableInfo<T>> {

    public ObjectExecuteResult(BaseMetaTableInfo<T> object, ResultSet resultSet, List<ColumnInfo> columnInfos, Map<ColumnInfo, Integer> columnIndexes, Map<ColumnInfo, String> columnAliases) throws ResultParseException {
        super(object, resultSet, columnInfos, columnIndexes, columnAliases);
    }

    public ObjectExecuteResult(BaseMetaTableInfo<T> object, ResultSet resultSet, Tool tool, List<ColumnInfo> columnInfos) {
        super(object, resultSet, tool, columnInfos);
    }

    @Override
    @SuppressWarnings("unchecked")
    public T parse() throws ResultParseException{
        try{
            T t = object.instanceInvoker.invoke();
            for (ColumnInfo columnInfo : columnInfos) {
                int index = tool.getColumnIndex(columnInfo);
                columnInfo.getMetaField().set(t, columnInfo.getTypeHandler().getValue(resultSet, index));
            }
            return t;
        }catch (Throwable e){
            throw new ResultParseException(e);
        }
    }

}
