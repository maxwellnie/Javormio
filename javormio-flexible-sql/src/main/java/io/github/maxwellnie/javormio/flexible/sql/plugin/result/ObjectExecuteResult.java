package io.github.maxwellnie.javormio.flexible.sql.plugin.result;

import io.github.maxwellnie.javormio.core.execution.ExecutorContext;
import io.github.maxwellnie.javormio.core.execution.StatementWrapper;
import io.github.maxwellnie.javormio.core.translation.table.BaseMetaTableInfo;
import io.github.maxwellnie.javormio.core.translation.table.column.ColumnInfo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * @author Maxwell Nie
 */
public class ObjectExecuteResult<T> extends ExecuteResult<T, BaseMetaTableInfo<T>> {
    protected StatementWrapper statementWrapper;

    public ObjectExecuteResult(BaseMetaTableInfo<T> object, ResultSet resultSet, List<ColumnInfo> columnInfos, Map<ColumnInfo, Integer> columnIndexes, Map<ColumnInfo, String> columnAliases, StatementWrapper statementWrapper, ExecutorContext executorContext) throws ResultParseException {
        super(object, resultSet, columnInfos, columnIndexes, columnAliases, executorContext);
        this.statementWrapper = statementWrapper;
    }

    public ObjectExecuteResult(BaseMetaTableInfo<T> object, ResultSet resultSet, Tool tool, List<ColumnInfo> columnInfos, StatementWrapper statementWrapper, ExecutorContext executorContext) {
        super(object, resultSet, tool, columnInfos, executorContext);
        this.statementWrapper = statementWrapper;
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
