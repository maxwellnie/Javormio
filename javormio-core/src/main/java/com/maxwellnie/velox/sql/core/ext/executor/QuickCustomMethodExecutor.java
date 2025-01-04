package com.maxwellnie.velox.sql.core.ext.executor;

import com.maxwellnie.velox.sql.core.cache.key.CacheKey;
import com.maxwellnie.velox.sql.core.meta.MetaData;
import com.maxwellnie.velox.sql.core.natives.exception.ExecutorException;
import com.maxwellnie.velox.sql.core.natives.database.mapping.returntype.ReturnTypeMapping;
import com.maxwellnie.velox.sql.core.natives.database.session.Session;
import com.maxwellnie.velox.sql.core.natives.database.sql.row.RowSql;
import com.maxwellnie.velox.sql.core.natives.database.table.TableInfo;
import com.maxwellnie.velox.sql.core.proxy.dao.executor.MethodExecutor;
import com.maxwellnie.velox.sql.core.proxy.dao.executor.impl.BaseMethodExecutor;
import com.maxwellnie.velox.sql.core.proxy.dao.executor.result.SqlResult;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author Maxwell Nie
 */
public class QuickCustomMethodExecutor extends BaseMethodExecutor {
    private MethodExecutor targetMethodExecutor;
    private String sql;
    private String name;
    private Class<?>[] paramsType;
    private Consumer<Object[]> check;
    private Function<MetaData, RowSql> buildRowSql;

    public QuickCustomMethodExecutor(String name, Class<?>[] paramsType, MethodExecutor targetMethodExecutor, String sql) {
        super(LoggerFactory.getLogger(QuickCustomMethodExecutor.class.getName() + " => [" + name + "]"));
        this.targetMethodExecutor = targetMethodExecutor;
        this.sql = sql;
        this.name = name;
        this.paramsType = paramsType;
    }

    public QuickCustomMethodExecutor(String name) {
        super(LoggerFactory.getLogger(QuickCustomMethodExecutor.class.getName() + " => [" + name + "]"));
        this.name = name;
    }

    @Override
    public void check(TableInfo tableInfo, Session session, Object[] args) throws ExecutorException {
        super.check(tableInfo, session, args);
        if (args == null) {
            if (paramsType != null && paramsType.length != 0)
                throw new ExecutorException("The number of parameters does not match the number of parameters required by the method");
            else
                return;
        }
        if (args.length != paramsType.length)
            throw new ExecutorException("The number of parameters does not match the number of parameters required by the method");
        if (check != null)
            check.accept(args);
    }

    @Override
    public MetaData prepared(TableInfo tableInfo, Object[] args) throws ExecutorException {
        MetaData metaData = MetaData.ofEmpty();
        metaData.addProperty("tableInfo", tableInfo);
        metaData.addProperty("args", args);
        return metaData;
    }

    @Override
    public RowSql buildRowSql(MetaData metaData) throws ExecutorException {
        if (buildRowSql != null) {
            metaData.addProperty("sqlTemplate", sql);
            return buildRowSql.apply(metaData);
        }
        return targetMethodExecutor.buildRowSql(metaData);
    }

    @Override
    public SqlResult handleRunnerResult(Object result, TableInfo tableInfo, CacheKey cacheKey, ReturnTypeMapping returnTypeMapping) throws ExecutorException {
        return targetMethodExecutor.handleRunnerResult(result, tableInfo, cacheKey, returnTypeMapping);
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public String getName() {
        return name;
    }

    public Class<?>[] getParamsType() {
        return paramsType;
    }

    public void setParamsType(Class<?>[] paramsType) {
        this.paramsType = paramsType;
    }

    public Consumer<Object[]> getCheck() {
        return check;
    }

    public void setCheck(Consumer<Object[]> check) {
        this.check = check;
    }

    public Function<MetaData, RowSql> getBuildRowSql() {
        return buildRowSql;
    }

    public void setBuildRowSql(Function<MetaData, RowSql> buildRowSql) {
        this.buildRowSql = buildRowSql;
    }

    public void setTargetMethodExecutor(MethodExecutor targetMethodExecutor) {
        this.targetMethodExecutor = targetMethodExecutor;
    }
}
