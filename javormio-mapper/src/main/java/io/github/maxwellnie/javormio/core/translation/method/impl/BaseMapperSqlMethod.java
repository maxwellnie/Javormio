package io.github.maxwellnie.javormio.core.translation.method.impl;

import io.github.maxwellnie.javormio.common.java.api.JavormioException;
import io.github.maxwellnie.javormio.core.Context;
import io.github.maxwellnie.javormio.core.execution.executor.SqlExecutor;
import io.github.maxwellnie.javormio.core.execution.executor.parameter.ExecutableSql;
import io.github.maxwellnie.javormio.core.execution.executor.parameter.ExecutorParameters;
import io.github.maxwellnie.javormio.core.translation.method.SqlMethod;
import io.github.maxwellnie.javormio.common.java.sql.SqlFragment;

/**
 * @author Maxwell Nie
 */
public abstract class BaseMapperSqlMethod<T, R> implements SqlMethod<R> {
    protected final MapperContext<T, R> mapperContext;

    public BaseMapperSqlMethod(MapperContext<T, R> mapperContext) {
        this.mapperContext = mapperContext;
    }

    @Override
    public R invoke(Object[] args) throws JavormioException {
        SqlExecutor sqlExecutor = getSqlExecutor();
        ExecutorParameters<?, R> executorParameters = getExecutorParameters(args);
        return doInvoke(sqlExecutor, executorParameters);
    }
    protected abstract R doInvoke(SqlExecutor sqlExecutor, ExecutorParameters<?, R> executorParameters);
    protected SqlExecutor getSqlExecutor(){
        Context context = mapperContext.getContext();
        return context.getSqlExecutor(mapperContext.methodFeature);
    }
    protected abstract ExecutorParameters<?, R>  getExecutorParameters(Object[] args);
    protected abstract ExecutableSql getExecutableSql(Object[] args);
    protected abstract SqlFragment getSqlFragment(Object[] args);
}
