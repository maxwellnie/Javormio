package com.maxwellnie.velox.sql.core.proxy.dao.executor.aspect;

import com.maxwellnie.velox.sql.core.annotation.proxy.MethodHandlerProxy;
import com.maxwellnie.velox.sql.core.annotation.proxy.MethodInterceptor;
import com.maxwellnie.velox.sql.core.meta.MetaData;
import com.maxwellnie.velox.sql.core.natives.database.sql.row.RowSql;
import com.maxwellnie.velox.sql.core.natives.exception.ExecutorException;
import com.maxwellnie.velox.sql.core.natives.exception.ProxyExtendsException;

import java.lang.reflect.InvocationTargetException;

/**
 * @author Maxwell Nie
 */
@MethodHandlerProxy
public abstract class SqlInterceptor extends EnhancedMethodHandler {
    public SqlInterceptor(long index, TargetMethodSignature signature) {
        super(index, signature);
    }
    /**
     * 构建sql
     *
     * @param metaData
     * @return sql
     * @throws ExecutorException
     */
    @MethodInterceptor
    public RowSql buildRowSql(SimpleInvocation simpleInvocation, MetaData metaData){
        RowSql rowSql = beforeBuildRowSql(metaData);
        if (rowSql != null)
            return rowSql;
        try {
            rowSql = (RowSql) simpleInvocation.proceed();
            rowSql = afterBuildRowSql(metaData, rowSql);
            return rowSql;
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new ProxyExtendsException(e);
        }
    }
    public abstract RowSql beforeBuildRowSql(MetaData metaData);
    public abstract RowSql afterBuildRowSql(MetaData metaData, RowSql rowSql);
}
