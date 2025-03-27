package io.github.maxwellnie.javormio.framework.extension.transaction.conroller;

import java.sql.SQLException;

/**
 * 事务控制器
 *
 * @author Maxwell Nie
 */
public interface TransactionController {
    /**
     * 提交事务
     *
     * @throws SQLException
     */
    void commit() throws SQLException;

    /**
     * 回滚事务
     *
     * @throws SQLException
     */
    void rollback() throws SQLException;

    /**
     * 关闭事务
     *
     * @param waive
     * @throws SQLException
     */
    void close(boolean waive) throws SQLException;

    /**
     * 事务是否关闭
     *
     * @return
     * @throws SQLException
     */
    boolean isClosed() throws SQLException;
}
