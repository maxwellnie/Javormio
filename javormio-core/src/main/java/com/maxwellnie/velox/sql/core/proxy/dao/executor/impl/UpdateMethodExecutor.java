package com.maxwellnie.velox.sql.core.proxy.dao.executor.impl;

import com.maxwellnie.velox.sql.core.cache.key.CacheKey;
import com.maxwellnie.velox.sql.core.meta.MetaData;
import com.maxwellnie.velox.sql.core.natives.exception.ExecutorException;
import com.maxwellnie.velox.sql.core.natives.database.mapping.returntype.ReturnTypeMapping;
import com.maxwellnie.velox.sql.core.natives.database.session.Session;
import com.maxwellnie.velox.sql.core.natives.database.sql.SqlType;
import com.maxwellnie.velox.sql.core.natives.database.sql.row.RowSql;
import com.maxwellnie.velox.sql.core.natives.database.sql.row.RowSqlFactory;
import com.maxwellnie.velox.sql.core.natives.database.sql.row.UpdateRowSqlFactory;
import com.maxwellnie.velox.sql.core.natives.database.table.TableInfo;
import com.maxwellnie.velox.sql.core.proxy.dao.executor.result.SqlResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;

/**
 * @author Maxwell Nie
 */
public class UpdateMethodExecutor extends BaseMethodExecutor {
    public UpdateMethodExecutor() {
        super(LoggerFactory.getLogger(UpdateMethodExecutor.class));
    }

    public UpdateMethodExecutor(Logger logger) {
        super(logger);
    }

    @Override
    public MetaData prepared(TableInfo tableInfo, Object[] args) throws ExecutorException {
        MetaData metaData = MetaData.ofEmpty();
        metaData.addProperty("tableInfo", tableInfo);
        metaData.addProperty("entityObjects", Collections.singleton(args[0]));
        metaData.addProperty("sqlType", SqlType.UPDATE);
        metaData.addProperty("sqlDecorator", args[1]);
        return metaData;
    }

    @Override
    public void check(TableInfo tableInfo, Session session, Object[] args) throws ExecutorException {
        super.check(tableInfo, session, args);
        if (args[0] == null)
            throw new ExecutorException("update entity can not be null");
    }

    @Override
    public RowSql buildRowSql(MetaData metaData) throws ExecutorException {
        RowSqlFactory rowSqlFactory = new UpdateRowSqlFactory();
        return rowSqlFactory.getRowSql(metaData);
    }

    @Override
    public SqlResult handleRunnerResult(Object result, TableInfo tableInfo, CacheKey cacheKey, ReturnTypeMapping returnTypeMapping) throws ExecutorException {
        return new SqlResult(SqlResult.FlushFlag.CLEAR, result, cacheKey);
    }
}
