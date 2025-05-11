package io.github.maxwellnie.javormio.core.execution.result;

import io.github.maxwellnie.javormio.common.java.api.ObjectMap;
import io.github.maxwellnie.javormio.common.java.proxy.invocation.MethodInvoker;

import java.sql.ResultSet;
import java.util.List;

/**
 * @author Maxwell Nie
 */
public class DefaultResultSetConvertor implements ResultSetConvertor{

    @Override
    public <E> E convert(ResultSet resultSet, ObjectMap<ResultSet, E> typeMapping, MethodInvoker<E, E> instanceInvoker) throws ConvertException {
        try{
            if (instanceInvoker!=null){
                E e = instanceInvoker.invoke();
                typeMapping.map(resultSet, e);
                return e;
            }else {
                return typeMapping.map(resultSet, null);
            }
        }catch (Throwable e){
            throw new ConvertException(e);
        }
    }

    @Override
    public <E> E convert(List<ResultSet> resultSets, ObjectMap<ResultSet, E> typeMapping, MethodInvoker<E, E> instanceInvoker) throws ConvertException {
        return null;
    }
}
