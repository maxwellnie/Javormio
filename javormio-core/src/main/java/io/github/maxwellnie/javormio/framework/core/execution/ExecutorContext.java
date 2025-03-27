package io.github.maxwellnie.javormio.framework.core.execution;

import io.github.maxwellnie.javormio.framework.extension.jdbc.connection.ConnectionResource;
import io.github.maxwellnie.javormio.framework.core.execution.result.ResultSetConvertor;
import io.github.maxwellnie.javormio.framework.core.translation.method.DaoMethodFeature;

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
     * 方法特征
     */
    DaoMethodFeature daoMethodFeature;
    /**
     * 结果集转换器
     */
    ResultSetConvertor resultSetConvertor;
    /**
     * 其他参数
     */
    Map<String, Object> properties;

    public ExecutorContext(ConnectionResource connectionResource, ExecutableSql executableSql, DaoMethodFeature daoMethodFeature, ResultSetConvertor resultSetConvertor, Map<String, Object> properties) {
        this.connectionResource = connectionResource;
        this.executableSql = executableSql;
        this.daoMethodFeature = daoMethodFeature;
        this.resultSetConvertor = resultSetConvertor;
        this.properties = properties;
    }

    public ConnectionResource getConnectionResource() {
        return connectionResource;
    }

    public ExecutableSql getExecutableSql() {
        return executableSql;
    }

    public DaoMethodFeature getDaoMethodFeature() {
        return daoMethodFeature;
    }

    public ResultSetConvertor getResultSetConvertor() {
        return resultSetConvertor;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public void setConnectionResource(ConnectionResource connectionResource) {
        this.connectionResource = connectionResource;
    }

    public void setExecutableSql(ExecutableSql executableSql) {
        this.executableSql = executableSql;
    }

    public void setDaoMethodFeature(DaoMethodFeature daoMethodFeature) {
        this.daoMethodFeature = daoMethodFeature;
    }

    public void setResultSetConvertor(ResultSetConvertor resultSetConvertor) {
        this.resultSetConvertor = resultSetConvertor;
    }


    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }
}
