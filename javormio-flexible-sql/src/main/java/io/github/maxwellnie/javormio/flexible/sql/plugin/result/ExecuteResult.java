package io.github.maxwellnie.javormio.flexible.sql.plugin.result;

import io.github.maxwellnie.javormio.common.java.api.ObjectMap;
import io.github.maxwellnie.javormio.core.execution.ExecutorContext;
import io.github.maxwellnie.javormio.core.translation.table.column.ColumnInfo;

import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.StreamSupport;

/**
 * @author Maxwell Nie
 */
public abstract class ExecuteResult<T, E>{
    protected E object;
    protected Tool tool;
    protected ResultSet resultSet;
    protected List<ColumnInfo> columnInfos;
    protected ExecutorContext executorContext;

    public ExecuteResult(E object, ResultSet resultSet, List<ColumnInfo> columnInfos, Map<ColumnInfo, Integer> columnIndexes, Map<ColumnInfo, String> columnAliases, ExecutorContext executorContext) throws ResultParseException{
        this.object = object;
        this.resultSet = resultSet;
        this.tool = new Tool(resultSet, columnIndexes, columnAliases);
        this.columnInfos = columnInfos;
        this.executorContext = executorContext;
    }

    public ExecuteResult(E object, ResultSet resultSet, Tool tool, List<ColumnInfo> columnInfos, ExecutorContext executorContext) {
        this.object = object;
        this.tool = tool;
        this.resultSet = resultSet;
        this.columnInfos = columnInfos;
        this.executorContext = executorContext;
    }
    public <R> MapExecuteResult<R, T> mapTo(Supplier<R> supplier, ObjectMap<T, R> objectMap){
        return new MapExecuteResult<>(objectMap, this, supplier);
    }
    public abstract T parse();
    @SuppressWarnings("unchecked")
    public <R, A> R collect(Collector<T, A, R> collector){
        try{
            A container = collector.supplier().get();
            while (!resultSet.isClosed() && resultSet.next()){
                T t = parse();
                collector.accumulator().accept(container, t);
            }
            return collector.characteristics().contains(Collector.Characteristics.IDENTITY_FINISH)
                    ? (R) container
                    : collector.finisher().apply(container);
        }catch (Throwable e){
            throw new ResultParseException(e);
        }finally {
            finish();
        }
    }
    public void finish() throws ResultParseException{

    }
}
