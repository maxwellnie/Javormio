package io.github.maxwellnie.javormio.core.database.sql.executor;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * SQL执行器
 * @author Maxwell Nie
 */
public interface SqlExecutor {
    /**
     * 执行SQL
     *
     * @param executorContext 执行上下文
     * @return 执行结果
     * @throws SQLException
     */
    Object run(ExecutorContext executorContext) throws SQLException;
}
