package io.github.maxwellnie.javormio.core.database.transaction.conroller;

import java.sql.SQLException;

/**
 * @author Maxwell Nie
 */
public interface TransactionController {
    void commit() throws SQLException;

    void rollback() throws SQLException;

    void close(boolean waive) throws SQLException;

    boolean isClosed() throws SQLException;
}
