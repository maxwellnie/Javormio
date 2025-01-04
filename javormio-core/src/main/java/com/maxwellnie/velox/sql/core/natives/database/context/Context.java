package com.maxwellnie.velox.sql.core.natives.database.context;

import com.maxwellnie.velox.sql.core.annotation.dao.SQL;
import com.maxwellnie.velox.sql.core.annotation.dao.SQLMethod;
import com.maxwellnie.velox.sql.core.annotation.proxy.MethodHandlerProxy;
import com.maxwellnie.velox.sql.core.cache.Cache;
import com.maxwellnie.velox.sql.core.config.Configuration;
import com.maxwellnie.velox.sql.core.natives.database.mapping.returntype.DefaultReturnReturnTypeParser;
import com.maxwellnie.velox.sql.core.natives.database.mapping.returntype.ReturnTypeParser;
import com.maxwellnie.velox.sql.core.natives.database.table.TableInfo;
import com.maxwellnie.velox.sql.core.natives.database.table.TableInfoManager;
import com.maxwellnie.velox.sql.core.natives.database.transaction.TransactionFactory;
import com.maxwellnie.velox.sql.core.natives.exception.ClassTypeException;
import com.maxwellnie.velox.sql.core.natives.exception.ContextInitException;
import com.maxwellnie.velox.sql.core.natives.exception.DaoRegisterException;
import com.maxwellnie.velox.sql.core.natives.exception.RegisterMethodException;
import com.maxwellnie.velox.sql.core.natives.task.TaskQueue;
import com.maxwellnie.velox.sql.core.proxy.dao.DaoImplFactory;
import com.maxwellnie.velox.sql.core.proxy.dao.executor.MethodExecutor;
import com.maxwellnie.velox.sql.core.proxy.dao.executor.MethodExecutorCycle;
import com.maxwellnie.velox.sql.core.proxy.dao.executor.aspect.*;
import com.maxwellnie.velox.sql.core.proxy.factory.MethodExecutorProxyObjectFactory;
import com.maxwellnie.velox.sql.core.proxy.parser.MethodExecutorProxyInfoParser;
import com.maxwellnie.velox.sql.core.utils.framework.ContextUtils;
import com.maxwellnie.velox.sql.core.utils.framework.MethodExecutorUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.*;

/**
 * velox-sql上下文
 *
 * @author Maxwell Nie
 */
public final class Context {
    private static final Logger logger = LoggerFactory.getLogger(Context.class);
    /**
     * 基础配置
     */
    private final Configuration configuration;
    /**
     * 开放接口工厂管理器
     */
    private final DaoImplFactoryManager daoImplManager;
    /**
     * 开放接口
     *
     * @see DaoImplFactory
     */
    private final Class<?> daoImplClazz;
    /**
     * 事务隔离级别
     *
     * @see java.sql.Connection#setTransactionIsolation(int)
     */
    private final int level;
    /**
     * 方法映射管理器
     *
     * @see MethodMappedManager
     */
    private final Map<Class<?>, MethodMappedManager> classMethodMappedMap = new LinkedHashMap<>();
    /**
     * 方法处理器代理解析器（扩展）
     */
    private final MethodExecutorProxyInfoParser methodExecutorProxyInfoParser = new MethodExecutorProxyInfoParser();
    /**
     * 方法处理器代理对象工厂（扩展）
     */
    private final MethodExecutorProxyObjectFactory methodExecutorProxyObjectFactory = new MethodExecutorProxyObjectFactory();
    /**
     * 事务工厂
     *
     * @see TransactionFactory#produce(boolean, int)
     */
    private TransactionFactory transactionFactory;
    /**
     * 缓存类
     */
    private Class<? extends Cache> cacheClass;
    private TaskQueue taskQueue;
    /**
     * 表信息工具
     *
     * @see TableInfoManager
     * @see TableInfo
     */
    private TableInfoManager tableInfoManager = new TableInfoManager() {
    };
    /**
     * 类型解析器
     *
     * @see ReturnTypeParser
     */
    private ReturnTypeParser returnTypeParser = new DefaultReturnReturnTypeParser();
    /**
     * 方法处理器
     *
     * @see MethodHandler
     */
    private Set<MethodHandler> methodHandlers = Collections.synchronizedSet(new TreeSet<>());

    public Context(TransactionFactory transactionFactory, Configuration configuration, TableInfoManager tableInfoManager) {
        this.configuration = configuration;
        this.level = configuration.getLevel();
        if (transactionFactory == null)
            throw new ContextInitException("TransactionFactory must be not null");
        else
            this.transactionFactory = transactionFactory;
        if (tableInfoManager != null)
            this.tableInfoManager = tableInfoManager;
        if (configuration.getDaoImplClass() == null)
            throw new ContextInitException("DaoImplClazz must be not null.");
        else {
            try {
                this.daoImplClazz = configuration.getDaoImplClass();
                ContextUtils.registerDaoImpl(daoImplClazz, new Object[]{configuration, registerMappedManager(daoImplClazz)});
                this.daoImplManager = new DaoImplFactoryManager();
            } catch (ClassTypeException | RegisterMethodException e) {
                throw new ContextInitException("Register dao class[" + configuration.getDaoImplClass() + "] failed", e.getCause());
            }
        }
        if (configuration.getDialect() == null) {
            throw new ContextInitException("Dialect must be not null.");
        } else
            this.methodHandlers.add(configuration.getDialect());
        if (configuration.isCache()) {
            if (configuration.getCacheClass() == null)
                throw new ContextInitException("Cache supporter must be not null.");
            try {
                cacheClass = configuration.getCacheClass();
            } catch (ClassCastException e) {
                throw new ContextInitException("Not found cache class " + configuration.getCacheClass() + ".", e.getCause());
            }
        }
        if (configuration.getIsTaskQueue()) {
            if (configuration.getTaskQueue() == null)
                throw new ContextInitException("Task queue supporter must be not null.");
            try {
                taskQueue = configuration.getTaskQueue();
            } catch (Exception e) {
                throw new ContextInitException("Not found task queue " + configuration.getTaskQueue() + ".", e.getCause());
            }
        }
    }

    public Context(TransactionFactory transactionFactory, Configuration configuration) {
        this(transactionFactory, configuration, null);
    }

    public MethodMappedManager registerMappedManager(Class<?> clazz) {
        return classMethodMappedMap.computeIfAbsent(clazz, k -> new MethodMappedManager(clazz));
    }

    public TransactionFactory getTransactionFactory() {
        return transactionFactory;
    }

    public void setTransactionFactory(TransactionFactory transactionFactory) {
        this.transactionFactory = transactionFactory;
    }

    public synchronized void setTypeParser(ReturnTypeParser returnTypeParser) {
        this.returnTypeParser = returnTypeParser;
    }

    public DaoImplFactoryManager getDaoImplManager() {
        return daoImplManager;
    }


    public Class<?> getDaoImplClazz() {
        return daoImplClazz;
    }

    public int getLevel() {
        return level;
    }

    public TaskQueue getTaskQueue() {
        return taskQueue;
    }

    /**
     * 注册clazz对应的开放接口实例工厂
     *
     * @param clazz
     */
    public void addDaoImplByEntityClass(Class<?> clazz) {
        lazyProxy(daoImplClazz);
        daoImplManager.registerDaoImplFactory(clazz);
        TableInfo tableInfo = TableInfoManager.getTableInfo(clazz);
        parseMethodsReturnType(daoImplClazz, tableInfo);
    }

    public void addDaoImplByBaseDao(Class<?> clazz, Class<?> entityClass) {
        daoImplManager.registerDaoImplFactory(clazz, entityClass);
        try {
            ContextUtils.registerDaoImpl(daoImplClazz, clazz, new Object[]{entityClass, registerMappedManager(clazz)});
        } catch (ClassTypeException e) {
            throw new DaoRegisterException(e);
        }
        lazyProxy(clazz);
        TableInfo tableInfo = TableInfoManager.getTableInfo(entityClass);
        parseMethodsReturnType(clazz, tableInfo);
    }

    private void parseMethodsReturnType(Class<?> daoImplClazz, TableInfo tableInfo) {
        for (Method method : daoImplClazz.getMethods()) {
            if (method.isAnnotationPresent(SQLMethod.class)) {
                SQLMethod SQLMethod = method.getDeclaredAnnotation(SQLMethod.class);
                if (SQLMethod.value() != null) {
                    tableInfo.registerReturnTypeMapping(daoImplClazz, MethodExecutorUtils.getMethodDeclaredName(method), returnTypeParser.parse(method.getReturnType(), tableInfo));
                }
            } else if (method.isAnnotationPresent(SQL.class)) {
                SQL sql = method.getDeclaredAnnotation(SQL.class);
                tableInfo.registerReturnTypeMapping(daoImplClazz, MethodExecutorUtils.getMethodDeclaredName(method), returnTypeParser.parse(method.getReturnType(), tableInfo));
            }
        }
    }

    /**
     * 懒加载代理
     */
    private void lazyProxy(Class<?> clazz) {
        MethodMappedManager methodMappedManager = classMethodMappedMap.get(clazz);
        if (methodMappedManager != null && !methodMappedManager.isProxy) {
            for (String key : methodMappedManager.methodMappedMap.keySet()) {
                for (MethodHandler methodHandler : methodHandlers) {
                    MethodExecutor methodExecutor = methodMappedManager.methodMappedMap.get(key);
                    if (methodExecutor == null)
                        continue;
                    if (methodHandler.getTargetMethodSignature() == null || methodHandler.getTargetMethodSignature() == MethodHandler.TargetMethodSignature.ANY || methodHandler.getTargetMethodSignature().key().equals(key)) {
                        MethodExecutor proxy;
                        if (methodHandler.getClass().isAnnotationPresent(MethodHandlerProxy.class))
                            proxy = methodExecutorProxyObjectFactory.produce(methodExecutor, methodExecutorProxyInfoParser.parse(methodHandler));
                        else
                            proxy = (MethodExecutor) Proxy.newProxyInstance(
                                    methodExecutor.getClass().getClassLoader(),
                                    new Class[]{MethodExecutor.class},
                                    new MethodsHandler(methodHandler, methodExecutor));
                        methodMappedManager.methodMappedMap.put(key, proxy);
                    }
                }
            }
            methodMappedManager.isProxy = true;
        }
    }

    public void injectBaseEnhance() {
        methodHandlers.add(new MethodExecutorCycle());
        methodHandlers.add(new CountMethodHandler());
        methodHandlers.add(new SelectPageMethodHandler());
        methodHandlers.add(new LastSqlMethodHandler());
    }

    /**
     * 获取clazz对应的开放接口实例工厂
     *
     * @param clazz
     * @param <T>
     * @return 开放接口实例工厂
     */
    public <T> DaoImplFactory<T> getDaoImplFactory(Class<?> clazz) {
        return (DaoImplFactory<T>) daoImplManager.getDaoImplFactory(clazz);
    }

    public MethodMappedManager getMethodMappedManager(Class<?> daoImplClazz) {
        return classMethodMappedMap.get(daoImplClazz);
    }

    public Set<MethodHandler> getMethodHandlers() {
        return methodHandlers;
    }

    public synchronized void setMethodHandlers(TreeSet<MethodHandler> methodHandlers) {
        this.methodHandlers = methodHandlers;
    }

    public synchronized void addMethodHandler(MethodHandler methodHandler) {
        methodHandlers.add(methodHandler);
    }

    /**
     * 方法映射管理器
     */
    public static class MethodMappedManager {
        final Map<String, MethodExecutor> methodMappedMap = new LinkedHashMap<>();
        final Class<?> clazz;
        volatile boolean isProxy = false;

        public MethodMappedManager(Class<?> clazz) {
            this.clazz = clazz;
        }

        /**
         * 获取被映射方法的处理器
         *
         * @param name
         * @return
         */
        public MethodExecutor getRegisteredMapped(String name) {
            return methodMappedMap.get(name);
        }

        /**
         * 注册被映射方法的处理器
         *
         * @param name
         * @param methodExecutor
         */
        public synchronized void registeredMapped(String name, MethodExecutor methodExecutor) {
            methodMappedMap.put(name, methodExecutor);
        }

        public Class<?> getMappedDaoClass() {
            return clazz;
        }
    }

    private class DaoImplFactoryManager {
        private final Map<Class<?>, DaoImplFactory<?>> daoImplMap = Collections.synchronizedMap(new LinkedHashMap<>());

        public DaoImplFactory<?> getDaoImplFactory(Class<?> clazz) {
            return daoImplMap.get(clazz);
        }

        public void registerDaoImplFactory(Class<?> clazz) {
            if ((clazz != null)) {
                Cache cache = null;
                if (configuration.isCache()) {
                    try {
                        cache = cacheClass.getConstructor().newInstance();
                    } catch (InstantiationException | NoSuchMethodException |
                             IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
                this.daoImplMap.put(clazz, new DaoImplFactory<>(daoImplClazz, tableInfoManager.getTableInfo(clazz, configuration), cache));
            } else
                throw new RegisterDaoImplFailedException("The daoImpl mapped class is null");
        }

        public void registerDaoImplFactory(Class<?> clazz, Class<?> entityClass) {
            if ((clazz != null)) {
                Cache cache = null;
                if (configuration.isCache()) {
                    try {
                        cache = cacheClass.getConstructor().newInstance();
                    } catch (InstantiationException | NoSuchMethodException |
                             IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
                this.daoImplMap.put(clazz, new DaoImplFactory<>(clazz, tableInfoManager.getTableInfo(entityClass, configuration), cache));
            } else
                throw new RegisterDaoImplFailedException("The daoImpl mapped class is null");
        }

        private class RegisterDaoImplFailedException extends RuntimeException {
            public RegisterDaoImplFailedException(String message) {
                super(message);
            }
        }
    }
}
