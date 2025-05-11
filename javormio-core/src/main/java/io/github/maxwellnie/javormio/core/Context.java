package io.github.maxwellnie.javormio.core;

import io.github.maxwellnie.javormio.common.annotation.document.ExtensionPoint;
import io.github.maxwellnie.javormio.common.cache.Cache;
import io.github.maxwellnie.javormio.common.java.jdbc.connection.ConnectionResource;
import io.github.maxwellnie.javormio.common.java.jdbc.datasource.DynamicMultipleDataSource;
import io.github.maxwellnie.javormio.common.java.reflect.method.SerializableFunction;
import io.github.maxwellnie.javormio.common.java.type.NullTypeHandler;
import io.github.maxwellnie.javormio.common.java.type.TypeHandler;
import io.github.maxwellnie.javormio.core.api.dynamic.DynamicSql;
import io.github.maxwellnie.javormio.core.execution.SqlExecutor;
import io.github.maxwellnie.javormio.core.execution.result.ResultSetConvertor;
import io.github.maxwellnie.javormio.core.translation.method.DaoMethodFeature;
import io.github.maxwellnie.javormio.core.translation.method.SqlMethod;
import io.github.maxwellnie.javormio.core.translation.table.TableInfo;

import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 上下文
 *
 * @author Maxwell Nie
 */
@ExtensionPoint
public class Context {
    final Map<Class<?>, TypeHandler<?>> typeHandlerMap = new HashMap<>();
    final ThreadLocal<Map<Object, Object>> reusableObjectsPool = new ThreadLocal<>();
    final Map<SerializableFunction<?, ?>, String> METHOD_NAME_CACHE = new ConcurrentHashMap<>();

    public Cache<?, ?> getCache(Object key) {
        return null;
    }

    public SqlMethod getSqlMethod(Class<?> daoImplClazz, String methodName, Class<?>[] parameterTypes) {
        return null;
    }

    public SqlExecutor getSqlExecutor(Class<?> implClazz) {
        return null;
    }
    public SqlExecutor getSqlExecutor(int type) {
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
}
