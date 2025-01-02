package com.maxwellnie.velox.sql.core.config;

import com.maxwellnie.velox.sql.core.cache.Cache;
import com.maxwellnie.velox.sql.core.cache.impl.LRUCache;
import com.maxwellnie.velox.sql.core.distributed.NoTransactionTask;
import com.maxwellnie.velox.sql.core.distributed.TransactionTask;
import com.maxwellnie.velox.sql.core.ext.template.engine.TemplateEngine;
import com.maxwellnie.velox.sql.core.natives.database.dialect.Dialect;
import com.maxwellnie.velox.sql.core.natives.task.DefaultTaskQueue;
import com.maxwellnie.velox.sql.core.natives.task.TaskQueue;
import com.maxwellnie.velox.sql.core.natives.type.convertor.impl.json.JsonSupporter;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;

/**
 * Configuration
 *
 * @author Maxwell Nie
 */
public abstract class Configuration {
    /**
     * 有关适配Java字节码的配置
     * @since 1.2.2
     */
    public static class JavaByteCodeConfiguration {
        /**
         * JDK9+ introduces a novel method named "privateLookupIn" to handle PRIVATE and PROTECTED methods.
         *
         * @since 1.0
         */
        public static final Method highJavaVersionLookUpMethod;
        /**
         * JDK8 it is necessary to use an invisible constructor to instantiate the LookUp class to handle PRIVATE and PROTECTED methods.
         *
         * @since 1.0
         */
        public static final Constructor<MethodHandles.Lookup> java8LookupConstructor;

        //since 1.0
        static {
            //java9+
            Method privateLookupIn;
            try {
                privateLookupIn = MethodHandles.class.getMethod("privateLookupIn", Class.class, MethodHandles.Lookup.class);
            } catch (NoSuchMethodException e) {
                privateLookupIn = null;
            }
            highJavaVersionLookUpMethod = privateLookupIn;
            //java8
            Constructor<MethodHandles.Lookup> lookup = null;
            if (highJavaVersionLookUpMethod == null) {
                try {
                    lookup = MethodHandles.Lookup.class.getDeclaredConstructor(Class.class, int.class);
                    lookup.setAccessible(true);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    lookup = null;
                }
            }
            java8LookupConstructor = lookup;
        }
        /**
         * @param method
         * @return
         * @throws NoSuchMethodException
         * @throws IllegalAccessException
         * @throws InvocationTargetException
         * @since 1.0
         */
        private static MethodHandle getHighJavaVersionMethodHandle(Method method)
                throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
            final Class<?> declaringClass = method.getDeclaringClass();
            return ((MethodHandles.Lookup) highJavaVersionLookUpMethod.invoke(null, declaringClass, MethodHandles.lookup())).findSpecial(
                    declaringClass, method.getName(), MethodType.methodType(method.getReturnType(), method.getParameterTypes()),
                    declaringClass);
        }

        /**
         * @param method
         * @return
         * @throws IllegalAccessException
         * @throws InstantiationException
         * @throws InvocationTargetException
         * @since 1.0
         */
        private static MethodHandle getJava8MethodHandle(Method method)
                throws IllegalAccessException, InstantiationException, InvocationTargetException {
            final Class<?> declaringClass = method.getDeclaringClass();
            return java8LookupConstructor.newInstance(declaringClass, MethodHandles.Lookup.PRIVATE | MethodHandles.Lookup.PROTECTED).unreflectSpecial(method, declaringClass);
        }
        public static Object handleDefaultMethod(Method method, Object proxy, Object[] args) throws Throwable {
            MethodHandle methodHandle = getJava8MethodHandle(method);
            if (highJavaVersionLookUpMethod == null)
                return methodHandle.bindTo(proxy).invokeWithArguments(args);
            else
                return getHighJavaVersionMethodHandle(method).bindTo(proxy).invokeWithArguments(args);
        }
    }
    /**
     * daoImplClassName
     */
    private Class<?> daoImplClass;
    /**
     * tablePrefix
     */
    private String tablePrefix = "";
    /**
     * standColumn
     */
    private boolean standColumn = false;
    private boolean standTable = false;
    private boolean isCache;
    private TemplateEngine templateEngine;
    /**
     * level
     */
    private int level = Connection.TRANSACTION_REPEATABLE_READ;
    private Dialect dialect = null;
    private Class<? extends TransactionTask> transactionTaskClass = NoTransactionTask.class;
    private boolean isTaskQueue;
    private TaskQueue taskQueue = new DefaultTaskQueue();
    private JsonSupporter jsonSupporter;
    /**
     * cacheClassName
     */
    private Class<? extends Cache> cacheClass = LRUCache.class;

    public boolean getIsTaskQueue() {
        return isTaskQueue;
    }

    public void setIsTaskQueue(boolean taskQueue) {
        isTaskQueue = taskQueue;
    }


    public Class<?> getDaoImplClass() {
        return daoImplClass;
    }

    public void setDaoImplClass(Class<?> daoImplClass) {
        this.daoImplClass = daoImplClass;
    }

    public boolean isStandTable() {
        return standTable;
    }

    public void setStandTable(boolean standTable) {
        this.standTable = standTable;
    }

    public String getTablePrefix() {
        return tablePrefix;
    }

    public void setTablePrefix(String tablePrefix) {
        this.tablePrefix = tablePrefix;
    }

    public boolean isStandColumn() {
        return standColumn;
    }

    public void setStandColumn(boolean standColumn) {
        this.standColumn = standColumn;
    }

    public boolean isCache() {
        return isCache;
    }

    public void setCache(boolean cache) {
        isCache = cache;
    }
    public void setCache(boolean cache, Class<? extends Cache> cacheClass) {
        isCache = cache;
        this.cacheClass = cacheClass;
    }
    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public Class<? extends Cache> getCacheClass() {
        return cacheClass;
    }

    public void setCacheClass(Class<? extends Cache> cacheClass) {
        this.isCache = true;
        this.cacheClass = cacheClass;
    }

    public Dialect getDialect() {
        return dialect;
    }

    public JsonSupporter getJsonSupporter() {
        return jsonSupporter;
    }

    public Class<? extends TransactionTask> getTransactionTaskClass() {
        return transactionTaskClass;
    }

    public void setTransactionTaskClass(Class<? extends TransactionTask> transactionTaskClass) {
        this.transactionTaskClass = transactionTaskClass;
    }

    public TaskQueue getTaskQueue() {
        return taskQueue;
    }

    public void setTaskQueue(TaskQueue taskQueue) {
        this.isTaskQueue = true;
        this.taskQueue = taskQueue;
    }
    public void setJsonSupporter(JsonSupporter jsonSupporter) {
        this.jsonSupporter = jsonSupporter;
    }

    public TemplateEngine getTemplateEngine() {
        return templateEngine;
    }

    public void setTemplateEngine(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    public void setDialect(Dialect dialect) {
        this.dialect = dialect;
    }
}
