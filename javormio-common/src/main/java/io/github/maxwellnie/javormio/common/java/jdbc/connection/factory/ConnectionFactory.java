package io.github.maxwellnie.javormio.common.java.jdbc.connection.factory;

import io.github.maxwellnie.javormio.common.java.api.Factory;
import io.github.maxwellnie.javormio.common.java.api.Resource;
import io.github.maxwellnie.javormio.common.java.jdbc.connection.JConnectionResource;

import java.sql.SQLException;

/**
 * 数据库连接工厂
 *
 * @author Maxwell Nie
 */
public interface ConnectionFactory extends AutoCloseable, Resource, Factory<JConnectionResource> {
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
     * @return JConnectionResource
     */
    JConnectionResource getConnection() throws SQLException;

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
