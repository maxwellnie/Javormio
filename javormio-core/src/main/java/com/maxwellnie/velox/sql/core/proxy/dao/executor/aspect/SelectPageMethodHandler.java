package com.maxwellnie.velox.sql.core.proxy.dao.executor.aspect;

import com.maxwellnie.velox.sql.core.annotation.proxy.MethodHandlerProxy;
import com.maxwellnie.velox.sql.core.annotation.proxy.MethodInterceptor;
import com.maxwellnie.velox.sql.core.cache.key.CacheKey;
import com.maxwellnie.velox.sql.core.config.simple.SingletonConfiguration;
import com.maxwellnie.velox.sql.core.meta.MetaData;
import com.maxwellnie.velox.sql.core.natives.dao.Page;
import com.maxwellnie.velox.sql.core.natives.dao.SqlDecorator;
import com.maxwellnie.velox.sql.core.natives.dao.VPage;
import com.maxwellnie.velox.sql.core.natives.database.sql.row.QueryRowSql;
import com.maxwellnie.velox.sql.core.natives.exception.ClassTypeException;
import com.maxwellnie.velox.sql.core.natives.exception.ExecutorException;
import com.maxwellnie.velox.sql.core.natives.database.mapping.returntype.ReturnTypeMapping;
import com.maxwellnie.velox.sql.core.natives.database.session.Session;
import com.maxwellnie.velox.sql.core.natives.database.sql.SqlPool;
import com.maxwellnie.velox.sql.core.natives.database.sql.SqlType;
import com.maxwellnie.velox.sql.core.natives.database.sql.row.QueryRowSqlFactory;
import com.maxwellnie.velox.sql.core.natives.database.sql.row.RowSql;
import com.maxwellnie.velox.sql.core.natives.database.sql.row.RowSqlFactory;
import com.maxwellnie.velox.sql.core.natives.database.sql.runner.SqlExecutor;
import com.maxwellnie.velox.sql.core.natives.database.statement.StatementWrapper;
import com.maxwellnie.velox.sql.core.natives.database.table.TableInfo;
import com.maxwellnie.velox.sql.core.proxy.dao.executor.result.SqlResult;
import com.maxwellnie.velox.sql.core.utils.log.LogUtils;

import java.lang.reflect.InvocationTargetException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

/**
 * @author Maxwell Nie
 */
@MethodHandlerProxy
public class SelectPageMethodHandler extends EnhancedMethodHandler {
    public SelectPageMethodHandler() {
        super(2001, new TargetMethodSignature("selectPage", new Class[]{Page.class, SqlDecorator.class}));
    }

    @MethodInterceptor
    public MetaData prepared(SimpleInvocation simpleInvocation, TableInfo tableInfo, Object[] args) throws ExecutorException {
        MetaData metaData = MetaData.ofEmpty();
        metaData.addProperty("tableInfo", tableInfo);
        metaData.addProperty("sqlType", SqlType.QUERY);
        metaData.addProperty("page", args[0]);
        metaData.addProperty("sqlDecorator", args[1]);
        return metaData;
    }

    @MethodInterceptor
    public void check(SimpleInvocation simpleInvocation, TableInfo tableInfo, Session session, Object[] args) throws ExecutorException, InvocationTargetException, IllegalAccessException {
        simpleInvocation.proceed(tableInfo, session, args);
        if (args.length != 2) {
            throw new ExecutorException("args length must be contains Page and SqlDecorator parameter");
        }
    }

    @MethodInterceptor
    public RowSql buildRowSql(SimpleInvocation simpleInvocation, MetaData metaData) throws ExecutorException {
        SqlDecorator<?> sqlDecorator = metaData.getProperty("sqlDecorator");
        if (sqlDecorator != null) {
            sqlDecorator.setLimitFragment(null);
        }
        RowSqlFactory rowSqlFactory = new QueryRowSqlFactory();
        return rowSqlFactory.getRowSql(metaData);
    }

    @MethodInterceptor
    public StatementWrapper openStatement(SimpleInvocation simpleInvocation, RowSql rowSql, Session session, TableInfo tableInfo, Object[] args) throws ExecutorException, InvocationTargetException, IllegalAccessException {
        long count = 0;
        try {
            count = count(rowSql, tableInfo, session);
        } catch (SQLException | ClassTypeException e) {
            throw new ExecutorException(e);
        }
        Page<?> page = (Page<?>) args[0];
        long currentRequestCount = 0;
        long updatedCurrent = 0;
        long offset = 10;
        if (page != null && page.getCurrent() > 0) {
            currentRequestCount = page.getCurrent() * page.getOffset();
            updatedCurrent = page.getCurrent();
            if (currentRequestCount > count) {
                updatedCurrent = count / page.getOffset() - 1;
            }
        }
        QueryRowSql queryRowSql = (QueryRowSql) rowSql;
        queryRowSql.setPaging(true);
        queryRowSql.setStart(currentRequestCount);
        queryRowSql.setOffset(offset);
        SingletonConfiguration.getInstance().getDialect().afterBuild(queryRowSql);
        StatementWrapper statementWrapper = (StatementWrapper) simpleInvocation.proceed(rowSql, session, tableInfo, args);
        statementWrapper.addProperty("total", count);
        statementWrapper.addProperty("current", updatedCurrent);
        statementWrapper.addProperty("offset", offset);
        return statementWrapper;
    }

    public long count(RowSql rowSql, TableInfo tableInfo, Session session) throws SQLException, ClassTypeException {
        String sql = rowSql.getNativeSql();
        int fromIndex = sql.indexOf("FROM");
        sql = sql.substring(fromIndex);
        String count = "COUNT(*)";
        if (tableInfo.hasPk()) {
            count = "COUNT(" + tableInfo.getTableName() + "." + tableInfo.getPkColumn().getColumnName() + ")";
        }
        sql = "SELECT" + SqlPool.SPACE + count + SqlPool.SPACE + sql;
        RowSql countRowSql = new RowSql();
        countRowSql.setNativeSql(sql);
        StatementWrapper statementWrapper = new StatementWrapper(session.getTransaction().getDataSourceAndConnection().getConnection().prepareStatement(countRowSql.getNativeSql()));
        PreparedStatement ps = statementWrapper.getPrepareStatement();
        try (ResultSet resultSet = ps.executeQuery()) {
            if (resultSet.next())
                return resultSet.getLong(1);
            else
                return 0;
        } finally {
            ps.close();
        }

    }

    @MethodInterceptor
    public Object runSql(SimpleInvocation simpleInvocation, StatementWrapper statementWrapper, RowSql rowSql) throws ExecutorException {
        SqlExecutor<?> sqlExecutor = SqlExecutor.get(rowSql.getSqlType());
        try {
            statementWrapper.addProperty("result", sqlExecutor.run(rowSql, statementWrapper));
            return statementWrapper;
        } catch (SQLException | ClassTypeException e) {
            throw LogUtils.convertToAdaptLoggerException(e, rowSql.getNativeSql(), rowSql.getParamsList());
        }
    }

    @MethodInterceptor
    public SqlResult handleRunnerResult(SimpleInvocation simpleInvocation, Object result, TableInfo tableInfo, CacheKey cacheKey, ReturnTypeMapping returnTypeMapping) throws ExecutorException {
        Object entityObjects = Collections.EMPTY_LIST;
        StatementWrapper statementWrapper = (StatementWrapper) result;
        if (result != null) {
            try {
                entityObjects = tableInfo.getResultSetParser().parseResultSet((ResultSet) statementWrapper.getProperty("result"), returnTypeMapping);
            } catch (SQLException | InvocationTargetException | NoSuchMethodException | InstantiationException |
                     IllegalAccessException e) {
                throw new ExecutorException(e);
            }
        }
        entityObjects = new VPage((List) entityObjects, (long) statementWrapper.getProperty("total"), (long) statementWrapper.getProperty("current"), (long) statementWrapper.getProperty("offset"));
        return new SqlResult(SqlResult.FlushFlag.FLUSH, entityObjects, cacheKey);
    }
}
