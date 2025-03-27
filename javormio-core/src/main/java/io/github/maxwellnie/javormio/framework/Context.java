package io.github.maxwellnie.javormio.framework;

import io.github.maxwellnie.javormio.framework.common.cache.Cache;
import io.github.maxwellnie.javormio.framework.core.db.jdbc.connection.ConnectionResource;
import io.github.maxwellnie.javormio.framework.core.db.jdbc.datasource.DynamicMultipleDataSource;
import io.github.maxwellnie.javormio.framework.core.executor.result.ResultSetConvertor;
import io.github.maxwellnie.javormio.framework.core.executor.SqlExecutor;
import io.github.maxwellnie.javormio.framework.core.translate.method.DaoMethodFeature;
import io.github.maxwellnie.javormio.framework.core.translate.method.SqlMethod;
import io.github.maxwellnie.javormio.framework.core.translate.table.TableInfo;
import io.github.maxwellnie.javormio.framework.common.java.type.NullTypeHandler;
import io.github.maxwellnie.javormio.framework.common.java.type.TypeHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * 上下文
 * @author Maxwell Nie
 */
public class Context {
    final Map<Class<?>, TypeHandler<?>> typeHandlerMap = new HashMap<>();
    public Cache<?,?> getCache(Object key) {
        return null;
    }
    public SqlMethod getSqlMethod(Class<?> daoImplClazz, String methodName, Class<?>[] parameterTypes) {
        return null;
    }
    public SqlExecutor getSqlExecutor(Class<?> implClazz) {
        return null;
    }
    public SqlExecutor getSqlExecutor(String className) {
        return null;
    }
    public DynamicMultipleDataSource getDynamicMultipleDataSource() {
        return null;
    }
    public ConnectionResource getConnectionResource() {
        return null;
    }
    public TypeHandler<?> getTypeHandler(Class<?> clazz) {
        return null;
    }
    public TypeHandler<?> getTypeHandler(Object obj) {
        if (obj == null)
            return NullTypeHandler.INSTANCE;
        return typeHandlerMap.get(obj.getClass());
    }
    public DaoMethodFeature getDaoMethodFeature(Class<?> daoImplClazz, String methodName, Class<?>[] parameterTypes) {
        return null;
    }
    public TableInfo getTableInfo(Class<?> clazz) {
        return null;
    }
    public DaoMethodFeature getDaoMethodFeature(int methodFeatureCode) {
        return null;
    }

    public ResultSetConvertor getResultSetConvertor() {
        return null;
    }
}
