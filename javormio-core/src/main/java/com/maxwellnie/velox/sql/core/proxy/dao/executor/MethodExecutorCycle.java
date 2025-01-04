package com.maxwellnie.velox.sql.core.proxy.dao.executor;

import com.maxwellnie.velox.sql.core.annotation.proxy.MethodHandlerProxy;
import com.maxwellnie.velox.sql.core.annotation.proxy.MethodInterceptor;
import com.maxwellnie.velox.sql.core.cache.Cache;
import com.maxwellnie.velox.sql.core.cache.key.CacheKey;
import com.maxwellnie.velox.sql.core.distributed.TransactionTask;
import com.maxwellnie.velox.sql.core.meta.MetaData;
import com.maxwellnie.velox.sql.core.natives.exception.ExecutorException;
import com.maxwellnie.velox.sql.core.natives.database.mapping.returntype.ReturnTypeMapping;
import com.maxwellnie.velox.sql.core.natives.database.session.Session;
import com.maxwellnie.velox.sql.core.natives.database.sql.SqlType;
import com.maxwellnie.velox.sql.core.natives.database.sql.row.RowSql;
import com.maxwellnie.velox.sql.core.natives.database.statement.StatementWrapper;
import com.maxwellnie.velox.sql.core.natives.database.table.TableInfo;
import com.maxwellnie.velox.sql.core.natives.task.TaskQueue;
import com.maxwellnie.velox.sql.core.natives.type.Empty;
import com.maxwellnie.velox.sql.core.proxy.dao.executor.result.SqlResult;
import com.maxwellnie.velox.sql.core.proxy.dao.executor.aspect.EnhancedMethodHandler;
import com.maxwellnie.velox.sql.core.proxy.dao.executor.aspect.MethodHandler;
import com.maxwellnie.velox.sql.core.proxy.dao.executor.aspect.SimpleInvocation;
import com.maxwellnie.velox.sql.core.utils.framework.MetaWrapperUtils;
import com.maxwellnie.velox.sql.core.utils.base.SystemClock;
import com.maxwellnie.velox.sql.core.utils.base.StringUtils;
import com.maxwellnie.velox.sql.core.utils.jdbc.CurrentThreadUtils;
import org.slf4j.Logger;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Maxwell Nie
 */
@MethodHandlerProxy
public class MethodExecutorCycle extends EnhancedMethodHandler {
    public MethodExecutorCycle() {
        super(MethodHandler.SPRING_SUPPORT_INDEX - 1L, TargetMethodSignature.ANY);
    }

    @MethodInterceptor
    public Object execute(SimpleInvocation simpleInvocation, TableInfo tableInfo, Session session, Cache cache, String daoImplHashCode, ReturnTypeMapping returnTypeMapping, Object[] args) throws ExecutorException {
        MethodExecutor executor = (MethodExecutor) simpleInvocation.getTarget();
        Logger logger = executor.getLogger();
        executor.check(tableInfo, session, args);
        setDataSource(tableInfo);
        // 预处理
        MetaData metaData = executor.prepared(tableInfo, args);
        // 构建SQL
        RowSql rowSql = executor.buildRowSql(metaData);
        if (rowSql != null) {
            long startTime = SystemClock.now();
            // 打开Statement
            StatementWrapper statementWrapper = executor.openStatement(rowSql, session, tableInfo, args);
            // 绑定元数据
            statementWrapper.getMetaData().addFromMetaData(metaData);
            logger.debug("SQL ### : " + rowSql.getNativeSql());
            if (rowSql.getParamsList().size() > 10)
                logger.debug("PARAM # : [" + rowSql.getParamsList().size() + "] row params");
            else
                logger.debug("PARAM # : " + rowSql.getParamsList());
            // 缓存Key
            CacheKey cacheKey = new CacheKey(tableInfo.getMappedClazz(), rowSql.getNativeSql(), session.getTransaction().getHolderObject(), daoImplHashCode);
            // 缓存Key添加参数
            for (List<Object> params : rowSql.getParamsList())
                cacheKey.addParams(params);
            // 缓存Key添加Statement
            statementWrapper.addProperty("cacheKey", cacheKey);
            AtomicReference<Object> result = new AtomicReference<>();
            TaskQueue taskQueue = session.getTaskQueue();
            AtomicReference<Throwable> error = new AtomicReference<>();
            if (rowSql.getSqlType().equals(SqlType.QUERY) && cache != null) {
                result.set(cache.get(cacheKey));
                if (result.get() != null) {
                    logger.debug("Cache hit.");
                    Object value = result.get();
                    if (value == Empty.EMPTY)
                        return null;
                    return value;
                } else if (taskQueue != null) {
                    taskQueue.require(daoImplHashCode, cacheKey, () -> {
                        if (rowSql.getSqlType().equals(SqlType.QUERY)) {
                            result.set(cache.get(cacheKey));
                            if (result.get() == null) {
                                try {
                                    result.set(executor.runSql(statementWrapper, rowSql));
                                    result.set(handleResult(executor, logger, tableInfo, session, cache, returnTypeMapping, startTime, statementWrapper, result));
                                } catch (Throwable e) {
                                    error.set(e);
                                }
                            } else
                                logger.debug("Cache hit.");
                        } else {
                            try {
                                result.set(executor.runSql(statementWrapper, rowSql));
                                result.set(handleResult(executor, logger, tableInfo, session, cache, returnTypeMapping, startTime, statementWrapper, result));
                            } catch (Throwable e) {
                                error.set(e);
                            }
                        }
                    });
                    if (error.get() != null)
                        throw new ExecutorException(error.get());
                    Object value = result.get();
                    if (value == Empty.EMPTY)
                        return null;
                    return value;
                } else
                    result.set(executor.runSql(statementWrapper, rowSql));
            } else
                result.set(executor.runSql(statementWrapper, rowSql));
            Object resultObject = handleResult(executor, logger, tableInfo, session, cache, returnTypeMapping, startTime, statementWrapper, result);
            SqlType sqlType = MetaWrapperUtils.of(statementWrapper, "sqlType");
            if (!sqlType.equals(SqlType.QUERY)) {
                TransactionTask transactionTask = MetaWrapperUtils.of(statementWrapper, "transactionTask");
                MetaData transactionMetaData = MetaData.ofEmpty();
                transactionMetaData.addProperty("dataSourceAndConnection", statementWrapper.getProperty("dataSourceAndConnection"));
                transactionMetaData.addProperty("rowSql", rowSql);
                transactionTask.add(transactionMetaData);
            }
            return resultObject;
        }
        throw new ExecutorException("rowSql build failed,rowSql is null.");
    }

    private void setDataSource(TableInfo tableInfo) {
        String id = CurrentThreadUtils.getDataSourceName();
        if (StringUtils.isNullOrEmpty(id) && StringUtils.isNullOrEmpty(tableInfo.getDataSourceName())) {
            CurrentThreadUtils.setDataSourceName(tableInfo.getDataSourceName());
        }
    }

    private Object handleResult(MethodExecutor executor, Logger logger, TableInfo tableInfo, Session context, Cache cache, ReturnTypeMapping returnTypeMapping, long startTime, StatementWrapper statementWrapper, AtomicReference<Object> result) throws ExecutorException {
        if (result.get() != null) {
            SqlResult sqlResult = executor.handleRunnerResult(result.get(), tableInfo, MetaWrapperUtils.of(statementWrapper, "cacheKey"), returnTypeMapping);
            logger.debug("SQL EXECUTED | TIME: " + (SystemClock.now() - startTime) + "ms");
            executor.flushCache(sqlResult, cache, context.getDirtyManager(), !context.getAutoCommit());
            executor.closeStatement(statementWrapper);
            return sqlResult.getResult();
        }
        executor.closeStatement(statementWrapper);
        throw new ExecutorException("The result of sql running is null.That's wrong result.");
    }
}
