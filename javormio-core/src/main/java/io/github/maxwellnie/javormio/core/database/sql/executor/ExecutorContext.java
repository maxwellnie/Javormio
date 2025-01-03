package io.github.maxwellnie.javormio.core.database.sql.executor;

import io.github.maxwellnie.javormio.core.database.jdbc.connection.ConnectionResource;
import io.github.maxwellnie.javormio.core.database.result.TypeMapping;
import io.github.maxwellnie.javormio.core.database.sql.ExecutableSql;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Maxwell Nie
 */
public class ExecutorContext {
    /**
     * 连接资源
     */
    ConnectionResource connectionResource;
    /**
     * 执行的sql
     */
    ExecutableSql executableSql;
    /**
     * 类型映射
     */
    TypeMapping typeMapping;
    /**
     * 其他参数
     */
    Map<String, Object> properties = new LinkedHashMap<>();

    public ExecutorContext(ConnectionResource connectionResource, ExecutableSql executableSql, TypeMapping typeMapping) {
        this.connectionResource = connectionResource;
        this.executableSql = executableSql;
        this.typeMapping = typeMapping;
    }

    public ConnectionResource getConnectionResource() {
        return connectionResource;
    }

    public ExecutableSql getExecutableSql() {
        return executableSql;
    }

    public TypeMapping getTypeMapping() {
        return typeMapping;
    }

    public Map<String, Object> putProperties(String key, Object value) {
        properties.put(key, value);
        return properties;
    }

    public Map<String, Object> removeProperties(String key) {
        properties.remove(key);
        return properties;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }
}
