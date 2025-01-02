package com.maxwellnie.velox.sql.core.proxy.dao.executor.aspect;

import com.maxwellnie.velox.sql.core.annotation.proxy.MethodHandlerProxy;
import com.maxwellnie.velox.sql.core.annotation.proxy.MethodInterceptor;
import com.maxwellnie.velox.sql.core.natives.dao.BaseSql;
import com.maxwellnie.velox.sql.core.natives.exception.ExecutorException;
import com.maxwellnie.velox.sql.core.natives.database.session.Session;
import com.maxwellnie.velox.sql.core.natives.database.sql.SqlPool;
import com.maxwellnie.velox.sql.core.natives.database.sql.row.RowSql;
import com.maxwellnie.velox.sql.core.natives.database.statement.StatementWrapper;
import com.maxwellnie.velox.sql.core.natives.database.table.TableInfo;
import com.maxwellnie.velox.sql.core.natives.type.convertor.TypeConvertorManager;

import java.lang.reflect.InvocationTargetException;
import java.util.stream.Collectors;

/**
 * @author Maxwell Nie
 */
@MethodHandlerProxy
public class LastSqlMethodHandler extends EnhancedMethodHandler {
    public LastSqlMethodHandler() {
        super(1, TargetMethodSignature.ANY);
    }
    @MethodInterceptor
    public StatementWrapper openStatement(SimpleInvocation simpleInvocation, RowSql rowSql, Session session, TableInfo tableInfo, Object[] args) throws ExecutorException {
        try {
            if (rowSql.getSqlDecorator() != null && rowSql.getSqlDecorator().getLastSql() != null) {
                BaseSql sql = rowSql.getSqlDecorator().getLastSql();
                rowSql.setNativeSql(rowSql.getNativeSql() + SqlPool.SPACE + sql.getSql());
                rowSql.getTypeConvertors().addAll(rowSql.getParamsList().stream().map((o) -> TypeConvertorManager.getConvertor(o.getClass())).collect(Collectors.toList()));
                rowSql.getParamsList().stream().peek((params) -> params.addAll(sql.getParams()));
            }
            return (StatementWrapper) simpleInvocation.proceed();
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new ExecutorException(e);
        }
    }
}
