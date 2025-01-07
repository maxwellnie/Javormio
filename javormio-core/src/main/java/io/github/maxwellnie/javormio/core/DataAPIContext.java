package io.github.maxwellnie.javormio.core;

import io.github.maxwellnie.javormio.core.database.jdbc.connection.ConnectionResource;
import io.github.maxwellnie.javormio.core.database.jdbc.datasource.DynamicMultipleDataSource;
import io.github.maxwellnie.javormio.core.database.sql.executor.SqlExecutor;
import io.github.maxwellnie.javormio.core.database.sql.method.SqlMethod;
import io.github.maxwellnie.javormio.core.java.type.TypeHandler;

/**
 * @author Maxwell Nie
 */
public class DataAPIContext {
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
}
