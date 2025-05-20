package io.github.maxwellnie.javormio.core;

import io.github.maxwellnie.javormio.common.annotation.document.ExtensionPoint;
import io.github.maxwellnie.javormio.common.java.jdbc.datasource.DynamicDataSource;
import io.github.maxwellnie.javormio.common.java.jdbc.transaction.TransactionObject;
import io.github.maxwellnie.javormio.common.java.reflect.method.SerializableFunction;
import io.github.maxwellnie.javormio.common.java.type.*;
import io.github.maxwellnie.javormio.core.api.SqlOperation;
import io.github.maxwellnie.javormio.core.api.dynamic.DynamicSql;
import io.github.maxwellnie.javormio.core.execution.executor.SingleSqlExecutor;
import io.github.maxwellnie.javormio.core.execution.executor.SqlExecutor;
import io.github.maxwellnie.javormio.core.execution.statement.StatementHelper;
import io.github.maxwellnie.javormio.core.translation.TableParser;
import io.github.maxwellnie.javormio.core.translation.table.TableInfo;

import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 上下文
 *
 * @author Maxwell Nie
 */
@ExtensionPoint
public class Context {
    protected final ThreadLocal<Map<Object, Object>> reusableObjectsPool = new ThreadLocal<>();
    protected final ThreadLocal<TransactionObject> transactionObject = new ThreadLocal<>();
    protected final Map<SerializableFunction<?, ?>, String> METHOD_NAME_CACHE = new ConcurrentHashMap<>();
    protected final Map<Object, StatementHelper<?>> statementHelperMap = new LinkedHashMap<>();
    protected final Map<Object, TypeHandler<?>> typeHandlerPool = new LinkedHashMap<>();
    {
        typeHandlerPool.put(String.class, new StringTypeHandler());
        typeHandlerPool.put(Integer.class, new IntegerTypeHandler());
        typeHandlerPool.put(Long.class, new LongTypeHandler());
        typeHandlerPool.put(Byte.class, new ByteTypeHandler());
        typeHandlerPool.put(Short.class, new ShortTypeHandler());
        typeHandlerPool.put(Float.class, new FloatTypeHandler());
        typeHandlerPool.put(Double.class, new DoubleTypeHandler());
    }
    protected TableParser tableParser;
    protected final Map<Object, SqlExecutor> sqlExecutorPool = new LinkedHashMap<>();
    {
        sqlExecutorPool.put(SingleSqlExecutor.class, new SingleSqlExecutor());
    }
    protected DynamicDataSource dynamicDataSource;
    @SuppressWarnings("unchecked")
    public<T extends SqlExecutor> T getSqlExecutor(Class<T> implClazz) {
        return (T) sqlExecutorPool.get(implClazz);
    }

    public DynamicDataSource getDynamicMultipleDataSource() {
        return dynamicDataSource;
    }
    public Connection getConnection() {
        return getDynamicMultipleDataSource().getConnection();
    }
    @SuppressWarnings("unchecked")
    public<T> TypeHandler<T> getTypeHandler(Class<T> clazz) {
        return (TypeHandler<T>) typeHandlerPool.get(clazz);
    }

    public TypeHandler<?> getTypeHandler(Object obj) {
        if (obj == null)
            return NullTypeHandler.INSTANCE;
        return typeHandlerPool.get(obj.getClass());
    }
    @SuppressWarnings("unchecked")
    public<T> TableInfo<T> getTableInfo(Class<T> clazz) {
        return tableParser.get(clazz);
    }

    @SuppressWarnings("unchecked")
    public <E> DynamicSql<E> newDynamicSql(E entityObj) {
        DynamicSql<?> dynamicSql = (DynamicSql<?>) getReusableObject(DynamicSql.class);
        Class<E> entityClass = (Class<E>) entityObj.getClass();
        if (dynamicSql == null) {
            DynamicSql<E> newDynamicSql = new DynamicSql<>(entityClass, getTableInfo(entityClass));
            setReusableObject(DynamicSql.class, newDynamicSql);
            return newDynamicSql;
        }
        return dynamicSql.reset(entityObj, entityClass, getTableInfo(entityClass));
    }
    public StatementHelper<?> getStatementHelper(Object key) {
        return statementHelperMap.get(key);
    }
    public <E> DynamicSql<E> newDynamicSql(Class<E> entityClass) {
        DynamicSql<?> dynamicSql = (DynamicSql<?>) getReusableObject(DynamicSql.class);
        if (dynamicSql == null) {
            DynamicSql<E> newDynamicSql = new DynamicSql<>(entityClass, getTableInfo(entityClass));
            setReusableObject(DynamicSql.class, newDynamicSql);
            return newDynamicSql;
        }
        return dynamicSql.reset(entityClass, getTableInfo(entityClass));
    }

    public Object getReusableObject(Object key) {
        Map<Object, Object> map = reusableObjectsPool.get();
        if (map == null) {
            return null;
        }
        return map.get(key);
    }
    public TransactionObject getTransaction() {
        return transactionObject.get();
    }
    public SqlOperation getOperation() {
        return null;
    }
    public void setReusableObject(Object key, Object value) {
        Map<Object, Object> map = reusableObjectsPool.get();
        if (map == null) {
            map = new HashMap<>();
            reusableObjectsPool.set(map);
        }
        map.put(key, value);
    }

    /**
     * 获取Getter方法名
     *
     * @param getter Getter方法
     * @param <T>    实体类对象
     * @param <R>    Getter方法返回值类型
     * @return String
     * @
     */
    public <T, R> String getMethodName(SerializableFunction<T, R> getter) throws ReflectiveOperationException {
        if (METHOD_NAME_CACHE.containsKey(getter))
            return METHOD_NAME_CACHE.get(getter);
        else {
            if (METHOD_NAME_CACHE.containsKey(getter))
                return METHOD_NAME_CACHE.get(getter);
            Method writeReplace = getter.getClass().getDeclaredMethod("writeReplace");
            writeReplace.setAccessible(true);
            SerializedLambda serializedLambda = (SerializedLambda) writeReplace.invoke(getter);
            String name = serializedLambda.getImplMethodName();
            METHOD_NAME_CACHE.put(getter, name);
            return name;
        }
    }
    public <T> void addTypeHandler(Class<T> clazz, TypeHandler<T> typeHandler) {
        typeHandlerPool.put(clazz, typeHandler);
    }

}
