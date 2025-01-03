package io.github.maxwellnie.javormio.core.database.jdbc.connection.factory;

import io.github.maxwellnie.javormio.core.database.jdbc.connection.ConnectionResource;
import io.github.maxwellnie.javormio.core.java.api.Factory;
import io.github.maxwellnie.javormio.core.java.api.Resource;

import java.sql.SQLException;

/**
 * 数据库连接工厂
 *
 * @author Maxwell Nie
 */
public interface ConnectionFactory extends AutoCloseable, Resource, Factory<ConnectionResource> {
    /**
     * 设置是否自动提交
     *
     * @param autoCommit 是否自动提交
     */
    void setAutoCommit(boolean autoCommit);

    /**
     * 是否关闭
     *
     * @return boolean
     */
    boolean isClosed();

    /**
     * 获取数据库连接资源
     *
     * @return ConnectionResource
     */
    ConnectionResource getConnection() throws SQLException;

    /**
     * 是否自动提交
     *
     * @return boolean
     */
    boolean autoCommit();

    /**
     * 关闭
     */
    void close();
}
