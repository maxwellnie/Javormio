package io.github.maxwellnie.javormio.core.java.plugin;

import io.github.maxwellnie.javormio.core.annotation.proxy.Interceptor;
import io.github.maxwellnie.javormio.core.annotation.proxy.ProxyDefine;
import io.github.maxwellnie.javormio.core.cache.Cache;
import io.github.maxwellnie.javormio.core.cache.SqlHashCacheKey;
import io.github.maxwellnie.javormio.core.database.executable.SqlExecutor;
import io.github.maxwellnie.javormio.core.database.jdbc.connection.ConnectionResource;
import io.github.maxwellnie.javormio.core.database.result.TypeMapping;
import io.github.maxwellnie.javormio.core.database.sql.ExecutableSql;
import io.github.maxwellnie.javormio.core.database.table.TableInfo;
import io.github.maxwellnie.javormio.core.java.proxy.MethodInvocationException;
import io.github.maxwellnie.javormio.core.java.proxy.invocation.InvocationLine;
import io.github.maxwellnie.javormio.core.java.proxy.invocation.InvokerContext;

/**
 * @author Maxwell Nie
 */
@ProxyDefine(interfaces = {SqlExecutor.class}, index = 20)
public class CachePlugin {
    private final Cache<Object, Object> cache;

    public CachePlugin(Cache<Object, Object> cache) {
        this.cache = cache;
    }

    @Interceptor
    public Object execute(InvocationLine invocationLine, InvokerContext invokerContext,
                          ExecutableSql executableSql, TableInfo tableInfo, ConnectionResource connectionResource, TypeMapping typeMapping)
            throws MethodInvocationException {
        Object result = cache.get(new SqlHashCacheKey(executableSql.getNamespace(),
                connectionResource.getDataSourceName(), executableSql.getSqlList(), executableSql.getParametersList()));
        if (result != null) {
            return result;
        } else {
            return invocationLine.proceed(invokerContext);
        }
    }

    @Interceptor
    public Object execute(InvocationLine invocationLine, InvokerContext invokerContext)
            throws MethodInvocationException {
        ExecutableSql executableSql = (ExecutableSql) invokerContext.getArgs()[0];
        ConnectionResource connectionResource = (ConnectionResource) invokerContext.getArgs()[1];
        Object result = cache.get(new SqlHashCacheKey(executableSql.getNamespace(),
                connectionResource.getDataSourceName(), executableSql.getSqlList(), executableSql.getParametersList()));
        if (result != null) {
            return result;
        } else {
            return invocationLine.proceed(invokerContext);
        }
    }
}
