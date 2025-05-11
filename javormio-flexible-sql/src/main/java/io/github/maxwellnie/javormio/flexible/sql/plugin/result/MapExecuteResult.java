package io.github.maxwellnie.javormio.flexible.sql.plugin.result;

import io.github.maxwellnie.javormio.common.java.api.ObjectMap;

import java.util.function.Supplier;

/**
 * @author Maxwell Nie
 */
public class MapExecuteResult<E, N> extends ExecuteResult<E, ExecuteResult<N, ?>> {
    protected ObjectMap<N, E> objectMap;
    protected ExecuteResult<N, ?> executeResult;
    protected Supplier<E> instanceFactory;

    public MapExecuteResult(ObjectMap<N, E> objectMap, ExecuteResult<N, ?> executeResult){
        super(executeResult, executeResult.resultSet, executeResult.tool, executeResult.columnInfos);
        this.objectMap = objectMap;
        this.executeResult = executeResult;
    }

    @Override
    public E parse() throws ResultParseException{
        try {
            N n = executeResult.parse();
            E e = instanceFactory.get();
            objectMap.map(n, e);
            return e;
        }catch (Throwable e){
            throw new ResultParseException(e);
        }
    }
}
