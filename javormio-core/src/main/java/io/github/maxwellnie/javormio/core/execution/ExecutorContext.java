package io.github.maxwellnie.javormio.core.execution;

import io.github.maxwellnie.javormio.common.java.jdbc.connection.ConnectionResource;
import io.github.maxwellnie.javormio.common.java.proxy.invocation.MethodInvoker;
import io.github.maxwellnie.javormio.core.execution.result.ResultSetConvertor;
import io.github.maxwellnie.javormio.core.translation.method.DaoMethodFeature;

import java.util.Map;

/**
 * @author Maxwell Nie
 */
public class ExecutorContext<E> {
    /**
     * 连接资源
     */
    ConnectionResource connectionResource;
    /**
     * 执行的sql
     */
    ExecutableSql executableSql;
    /**
     * 方法特征
     */
    DaoMethodFeature<E> daoMethodFeature;
    /**
     * 结果集转换器
     */
    ResultSetConvertor resultSetConvertor;
    /**
     * 实例方法调用者
     */
    MethodInvoker<E, E> instanceMethodInvoker;
    /**
     * 其他参数
     */
    Map<String, Object> properties;

    public ExecutorContext(ConnectionResource connectionResource, ExecutableSql executableSql, DaoMethodFeature<E> daoMethodFeature, ResultSetConvertor resultSetConvertor, MethodInvoker<E, E> instanceMethodInvoker, Map<String, Object> properties) {
        this.connectionResource = connectionResource;
        this.executableSql = executableSql;
        this.daoMethodFeature = daoMethodFeature;
        this.resultSetConvertor = resultSetConvertor;
        this.instanceMethodInvoker = instanceMethodInvoker;
        this.properties = properties;
    }

    public ConnectionResource getConnectionResource() {
        return connectionResource;
    }

    public void setConnectionResource(ConnectionResource connectionResource) {
        this.connectionResource = connectionResource;
    }

    public ExecutableSql getExecutableSql() {
        return executableSql;
    }

    public void setExecutableSql(ExecutableSql executableSql) {
        this.executableSql = executableSql;
    }

    public DaoMethodFeature<E> getDaoMethodFeature() {
        return daoMethodFeature;
    }

    public void setDaoMethodFeature(DaoMethodFeature<E> daoMethodFeature) {
        this.daoMethodFeature = daoMethodFeature;
    }

    public ResultSetConvertor getResultSetConvertor() {
        return resultSetConvertor;
    }

    public MethodInvoker<E, E> getInstanceMethodInvoker() {
        return instanceMethodInvoker;
    }

    public void setResultSetConvertor(ResultSetConvertor resultSetConvertor) {
        this.resultSetConvertor = resultSetConvertor;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }
}
