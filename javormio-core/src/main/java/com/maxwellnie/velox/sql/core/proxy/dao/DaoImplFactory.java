package com.maxwellnie.velox.sql.core.proxy.dao;

import com.maxwellnie.velox.sql.core.cache.Cache;
import com.maxwellnie.velox.sql.core.natives.exception.DaoImplClassException;
import com.maxwellnie.velox.sql.core.natives.database.context.Context;
import com.maxwellnie.velox.sql.core.natives.database.session.Session;
import com.maxwellnie.velox.sql.core.natives.database.table.TableInfo;

import java.lang.reflect.Proxy;

/**
 * @author Maxwell Nie
 */
public class DaoImplFactory<T> {
    private final Class<T> daoInterfaceClass;
    private final TableInfo tableInfo;
    private final Cache cache;

    public DaoImplFactory(Class<T> daoInterfaceClass, TableInfo tableInfo, Cache cache) {
        this.daoInterfaceClass = daoInterfaceClass;
        this.tableInfo = tableInfo;
        this.cache = cache;
    }

    public Class<T> getDaoInterfaceClass() {
        return daoInterfaceClass;
    }

    public <B extends T> T produce(Session session, Context.MethodMappedManager methodMappedManager) {
        if (daoInterfaceClass.isInterface()) {
            return (B) Proxy.newProxyInstance(daoInterfaceClass.getClassLoader(), new Class[]{daoInterfaceClass},
                    new DaoImplInvokeHandler(tableInfo, session, cache, methodMappedManager));
        } else
            throw new DaoImplClassException();
    }
}
