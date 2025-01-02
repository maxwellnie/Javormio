package com.maxwellnie.velox.sql.core.proxy.dao.executor.impl;

import com.maxwellnie.velox.sql.core.cache.key.CacheKey;
import com.maxwellnie.velox.sql.core.meta.MetaData;
import com.maxwellnie.velox.sql.core.natives.exception.ExecutorException;
import com.maxwellnie.velox.sql.core.natives.database.mapping.returntype.ReturnTypeMapping;
import com.maxwellnie.velox.sql.core.natives.database.session.Session;
import com.maxwellnie.velox.sql.core.natives.database.sql.SqlType;
import com.maxwellnie.velox.sql.core.natives.database.sql.row.DeleteRowSqlFactory;
import com.maxwellnie.velox.sql.core.natives.database.sql.row.RowSql;
import com.maxwellnie.velox.sql.core.natives.database.sql.row.RowSqlFactory;
import com.maxwellnie.velox.sql.core.natives.database.table.TableInfo;
import com.maxwellnie.velox.sql.core.proxy.dao.executor.result.SqlResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

/**
 * @author Maxwell Nie
 */
public class DeleteMethodExecutor extends BaseMethodExecutor {
    public DeleteMethodExecutor() {
        super(LoggerFactory.getLogger(DeleteMethodExecutor.class));
    }

    public DeleteMethodExecutor(Logger logger) {
        super(logger);
    }

    @Override
    public MetaData prepared(TableInfo tableInfo, Object[] args) throws ExecutorException {
        MetaData metaData = MetaData.ofEmpty();
        metaData.addProperty("tableInfo", tableInfo);
        if (args[0] instanceof Serializable[]) {
            metaData.addProperty("sqlType", SqlType.BATCH_UPDATE);
            metaData.addProperty("ids", args[0]);
        } else {
            metaData.addProperty("sqlType", SqlType.UPDATE);
            metaData.addProperty("sqlDecorator", args[0]);
        }
        return metaData;
    }

    @Override
    public void check(TableInfo tableInfo, Session session, Object[] args) throws ExecutorException {
        super.check(tableInfo, session, args);
        if (args[0] instanceof Serializable[]) {
            Serializable[] ids = (Serializable[]) args[0];
            if (ids.length == 0) {
                throw new ExecutorException("ids is empty");
            }
            if (!tableInfo.hasPk()) {
                throw new ExecutorException("tableInfo has no pk");
            }
        }
    }

    @Override
    public RowSql buildRowSql(MetaData metaData) throws ExecutorException {
        RowSqlFactory rowSqlFactory = new DeleteRowSqlFactory();
        return rowSqlFactory.getRowSql(metaData);
    }

    @Override
    public SqlResult handleRunnerResult(Object result, TableInfo tableInfo, CacheKey cacheKey, ReturnTypeMapping returnTypeMapping) throws ExecutorException {
        return new SqlResult(SqlResult.FlushFlag.CLEAR, result, cacheKey);
    }
}
