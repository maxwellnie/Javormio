package com.maxwellnie.velox.sql.core.proxy.dao.executor.aspect;

import com.maxwellnie.velox.sql.core.annotation.proxy.MethodHandlerProxy;
import com.maxwellnie.velox.sql.core.annotation.proxy.MethodInterceptor;
import com.maxwellnie.velox.sql.core.meta.MetaData;
import com.maxwellnie.velox.sql.core.natives.dao.SqlDecorator;
import com.maxwellnie.velox.sql.core.natives.exception.ExecutorException;
import com.maxwellnie.velox.sql.core.natives.database.session.Session;
import com.maxwellnie.velox.sql.core.natives.database.sql.SqlPool;
import com.maxwellnie.velox.sql.core.natives.database.sql.row.RowSql;
import com.maxwellnie.velox.sql.core.natives.database.statement.StatementWrapper;
import com.maxwellnie.velox.sql.core.natives.database.table.TableInfo;

import java.lang.reflect.InvocationTargetException;

/**
 * @author Maxwell Nie
 */
@MethodHandlerProxy
public class CountMethodHandler extends EnhancedMethodHandler {
    public CountMethodHandler() {
        super(999, new TargetMethodSignature("count", new Class[]{SqlDecorator.class}));
    }

    @MethodInterceptor
    public RowSql buildRowSql(SimpleInvocation simpleInvocation, MetaData metaData) throws ExecutorException {
        SqlDecorator<?> sqlDecorator = metaData.getProperty("sqlDecorator");
        if (sqlDecorator != null) {
            sqlDecorator.setLimitFragment(null);
        }
        try {
            return (RowSql) simpleInvocation.targetMethod.invoke(simpleInvocation.getTarget(), metaData);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new ExecutorException(e);
        }
    }

    @MethodInterceptor
    public StatementWrapper openStatement(SimpleInvocation simpleInvocation, RowSql rowSql, Session session, TableInfo tableInfo, Object[] args) throws ExecutorException {
        String sql = rowSql.getNativeSql();
        int fromIndex = sql.indexOf("FROM");
        sql = sql.substring(fromIndex);
        String count = "COUNT(*)";
        if (tableInfo.hasPk()) {
            count = "COUNT(" + tableInfo.getTableName() + "." + tableInfo.getPkColumn().getColumnName() + ")";
        }
        sql = "SELECT" + SqlPool.SPACE + count + SqlPool.SPACE + sql;
        rowSql.setNativeSql(sql);
        try {
            return (StatementWrapper) simpleInvocation.targetMethod.invoke(simpleInvocation.getTarget(), simpleInvocation.getArgs());
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new ExecutorException(e);
        }
    }
}
