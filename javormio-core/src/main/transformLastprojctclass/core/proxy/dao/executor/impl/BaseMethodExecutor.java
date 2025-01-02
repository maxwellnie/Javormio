package com.maxwellnie.velox.sql.core.proxy.dao.executor.impl;

import com.maxwellnie.velox.sql.core.cache.Cache;
import com.maxwellnie.velox.sql.core.cache.transactional.CacheTransactional;
import com.maxwellnie.velox.sql.core.distributed.TransactionTask;
import com.maxwellnie.velox.sql.core.natives.exception.ClassTypeException;
import com.maxwellnie.velox.sql.core.natives.exception.ExecutorException;
import com.maxwellnie.velox.sql.core.natives.exception.MethodNotSupportException;
import com.maxwellnie.velox.sql.core.natives.database.context.Context;
import com.maxwellnie.velox.sql.core.natives.database.mapping.returntype.ReturnTypeMapping;
import com.maxwellnie.velox.sql.core.natives.database.session.Session;
import com.maxwellnie.velox.sql.core.natives.database.sql.row.RowSql;
import com.maxwellnie.velox.sql.core.natives.database.sql.runner.SqlExecutor;
import com.maxwellnie.velox.sql.core.natives.database.statement.StatementWrapper;
import com.maxwellnie.velox.sql.core.natives.database.table.TableInfo;
import com.maxwellnie.velox.sql.core.natives.database.transaction.impl.jdbc.Connections;
import com.maxwellnie.velox.sql.core.natives.type.Empty;
import com.maxwellnie.velox.sql.core.proxy.dao.executor.MethodExecutor;
import com.maxwellnie.velox.sql.core.proxy.dao.executor.result.SqlResult;
import com.maxwellnie.velox.sql.core.utils.log.LogUtils;
import org.slf4j.Logger;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @author Maxwell Nie
 */
public abstract class BaseMethodExecutor implements MethodExecutor {
    private final Logger logger;
    protected Context.MethodMappedManager methodMappedManager;

    public BaseMethodExecutor(Logger logger) {
        this.logger = logger;
    }

    @Override
    public Object execute(TableInfo tableInfo, Session session, Cache<Object, Object> cache, String daoImplHashCode, ReturnTypeMapping returnTypeMapping, Object[] args) throws ExecutorException {
        throw new MethodNotSupportException("You must use MethodExecutorCycle.");
    }

    @Override
    public void check(TableInfo tableInfo, Session session, Object[] args) throws ExecutorException {
        if (session == null) {
            throw new ExecutorException("Session is null!");
        } else if (tableInfo == null) {
            throw new ExecutorException("TableInfo is null!");
        } else if (session.isClosed()) {
            throw new ExecutorException("Session is closed!");
        } else {
            if (session.getTransaction() == null) {
                throw new ExecutorException("Session is not have Transaction!");
            }
        }
    }

    @Override
    public StatementWrapper openStatement(RowSql rowSql, Session session, TableInfo tableInfo, Object[] args) throws ExecutorException {
        try {
            Connections.DataSourceAndConnection dataSourceAndConnection = session.getTransaction().getDataSourceAndConnection();
            TransactionTask transactionTask = session.getTransaction().getTransactionTask();
            PreparedStatement preparedStatement = dataSourceAndConnection.getConnection().prepareStatement(rowSql.getNativeSql());
            StatementWrapper statementWrapper = new StatementWrapper(preparedStatement);
            statementWrapper.addProperty("dataSourceAndConnection", dataSourceAndConnection);
            statementWrapper.addProperty("transactionTask", transactionTask);
            return statementWrapper;
        } catch (SQLException e) {
            throw LogUtils.convertToAdaptLoggerException(e, rowSql.getNativeSql(), rowSql.getParamsList());
        }
    }

    @Override
    public Object runSql(StatementWrapper statementWrapper, RowSql rowSql) throws ExecutorException {
        SqlExecutor<?> sqlExecutor = SqlExecutor.get(rowSql.getSqlType());
        try {
            return sqlExecutor.run(rowSql, statementWrapper);
        } catch (SQLException | ClassTypeException e) {
            throw LogUtils.convertToAdaptLoggerException(e, rowSql.getNativeSql(), rowSql.getParamsList());
        }
    }

    @Override
    public void flushCache(SqlResult sqlResult, Cache cache, CacheTransactional dirtyManager, boolean isTransactional) throws ExecutorException {
        if (sqlResult != null && sqlResult.getCacheKey() != null && cache != null) {
            if (sqlResult.getFlag().equals(SqlResult.FlushFlag.FLUSH)) {
                logger.debug("Cache flushed.");
                doFlushCache(sqlResult, cache, dirtyManager, isTransactional);
            } else {
                logger.debug("Cache cleared.");
                doClearCache(cache, dirtyManager, isTransactional);
            }
        }
    }

    /**
     * 更新缓存。
     *
     * @param sqlResult
     * @param cache
     * @param dirtyManager
     */
    protected void doFlushCache(SqlResult sqlResult, Cache cache, CacheTransactional dirtyManager, boolean isTransactional) {
        if (dirtyManager != null && isTransactional) {
            Object value = sqlResult.getResult();
            if (value == null)
                value = Empty.EMPTY;
            dirtyManager.get(cache).put(sqlResult.getCacheKey(), value);
        } else {
            cache.put(sqlResult.getCacheKey(), sqlResult.getResult());
        }
    }

    /**
     * 清理缓存。
     *
     * @param cache
     * @param dirtyManager
     */
    protected void doClearCache(Cache<?, ?> cache, CacheTransactional dirtyManager, boolean isTransactional) {
        if (dirtyManager != null && isTransactional) {
            dirtyManager.clear();
        }
        cache.clear();
    }

    @Override
    public void closeStatement(StatementWrapper statementWrapper) throws ExecutorException {
        try {
            statementWrapper.get().close();
            logger.debug("Closed statement.");
        } catch (SQLException e) {
            throw new ExecutorException(e);
        }
    }

    public Logger getLogger() {
        return logger;
    }

    @Override
    public void setMethodMappedManager(Context.MethodMappedManager methodMappedManager) {
        this.methodMappedManager = methodMappedManager;
    }
}
