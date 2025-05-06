package io.github.maxwellnie.javormio.core.execution;

import io.github.maxwellnie.javormio.common.annotation.document.ExtensionPoint;
import io.github.maxwellnie.javormio.core.execution.result.ConvertException;

import java.sql.SQLException;

/**
 * SQL执行器
 *
 * @author Maxwell Nie
 */
@ExtensionPoint
public interface SqlExecutor {
    /**
     * 执行SQL
     *
     * @param executorContext 执行上下文
     * @return 执行结果
     * @throws SQLException
     */
    Object run(ExecutorContext executorContext) throws SQLException, ConvertException;
}
