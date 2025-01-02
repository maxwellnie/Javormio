package com.maxwellnie.velox.sql.core.proxy.dao;

import com.maxwellnie.velox.sql.core.cache.Cache;
import com.maxwellnie.velox.sql.core.natives.exception.NotMappedMethodException;
import com.maxwellnie.velox.sql.core.natives.database.context.Context;
import com.maxwellnie.velox.sql.core.natives.database.mapping.returntype.ReturnTypeMapping;
import com.maxwellnie.velox.sql.core.natives.database.session.Session;
import com.maxwellnie.velox.sql.core.natives.database.table.TableInfo;
import com.maxwellnie.velox.sql.core.proxy.dao.executor.MethodExecutor;
import com.maxwellnie.velox.sql.core.utils.framework.MethodExecutorUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;

import static com.maxwellnie.velox.sql.core.config.Configuration.JavaByteCodeConfiguration.*;

/**
 * 这是一个代理类，会处理被代理接口的方法，即使他不是我们的初始DaoImpl，我们将整个框架的很多结构都修改为可自定义的模式。<br/>
 * 你可以根据需求重新设置DaoImpl的功能。
 *
 * @author Maxwell Nie
 */
public class DaoImplInvokeHandler implements InvocationHandler {
    private static final Logger logger = LoggerFactory.getLogger(DaoImplInvokeHandler.class);

    /**
     * @since 1.0
     */
    private TableInfo tableInfo;
    /**
     * @since 1.0
     */
    private Session session;
    private Cache<Object, Object> cache;
    private Context.MethodMappedManager methodMappedManager;

    public DaoImplInvokeHandler(TableInfo tableInfo, Session session, Cache<Object, Object> cache) {
        this.tableInfo = tableInfo;
        this.session = session;
        this.cache = cache;
        logger.debug("table info : " + tableInfo);
        logger.debug("enable cache : " + (cache != null));
    }

    public DaoImplInvokeHandler(TableInfo tableInfo, Session session, Cache<Object, Object> cache, Context.MethodMappedManager methodMappedManager) {
        this.tableInfo = tableInfo;
        this.session = session;
        this.cache = cache;
        this.methodMappedManager = methodMappedManager;
        logger.debug("table info : " + tableInfo);
        logger.debug("enable cache : " + (cache != null));
    }



    public Cache<Object, Object> getCache() {
        return cache;
    }

    public void setCache(Cache<Object, Object> cache) {
        this.cache = cache;
    }

    public TableInfo getTableInfo() {
        return tableInfo;
    }

    public void setTableInfo(TableInfo tableInfo) {
        this.tableInfo = tableInfo;
    }

    public Session getDaoImplSession() {
        return session;
    }

    public void setDaoImplSession(Session session) {
        this.session = session;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        logger.debug("Method - " + method.getName() + " invoke ");
        String methodMappedKey = MethodExecutorUtils.getMethodDeclaredName(method);
        MethodExecutor methodExecutor = methodMappedManager.getRegisteredMapped(methodMappedKey);
        ReturnTypeMapping returnTypeMapping = tableInfo.getReturnTypeMapping(methodMappedManager.getMappedDaoClass(), methodMappedKey);
        /**
         * 判断处理器是否被获取到，被获取到就开始执行，反之就判断是否Object的方法，是则执行代理类的对应方法，如果都不是则抛出异常
         * 如果设置了自动提交，那么每次执行完Executor都会更新缓存。
         */
        if (methodExecutor != null) {
            return methodExecutor.execute(tableInfo, session, cache, toString(), returnTypeMapping, args);
        } else if (Object.class.equals(method.getDeclaringClass())) {
            return method.invoke(this, args);
        } else if (method.isDefault()) {
            try {
                return handleDefaultMethod(method, proxy, args);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                throw e;
            }
        } else
            throw new NotMappedMethodException("The method \"" + method.getReturnType() + " " + method.getName() + Arrays.toString(method.getParameterTypes()).replace("[", "(").replace("]", ")") + "\" did not find a MethodExecutor.");
    }



    @Override
    public int hashCode() {
        return Objects.hash(tableInfo, session, cache);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DaoImplInvokeHandler that = (DaoImplInvokeHandler) o;
        return Objects.equals(tableInfo, that.tableInfo) && Objects.equals(session, that.session) && Objects.equals(cache, that.cache);
    }

    @Override
    public String toString() {
        return super.toString() + "&" + tableInfo.getMappedClazz().getName();
    }
}
