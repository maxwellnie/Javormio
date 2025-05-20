package io.github.maxwellnie.javormio.core.execution.executor;

import io.github.maxwellnie.javormio.core.execution.executor.parameter.ExecutorParameters;

import java.sql.Statement;

/**
 * @author Maxwell Nie
 */
public interface SqlExecutor {
    /**
     * 执行查询
     *
     * @param executorParameters 执行参数
     * @param <E>                返回类型
     * @return 返回查询结果
     */
    <T extends Statement, E> E query(ExecutorParameters<T, E> executorParameters);
    /**
     * 执行更新
     *
     * @param executorParameters 执行参数
     * @return 返回受影响的行数
     */
    <T extends Statement> int update(ExecutorParameters<T, ?> executorParameters);
    /**
     * 执行批量更新
     *
     * @param executorParameters 执行参数
     * @return 返回受影响的行数
     */
    <T extends Statement> int[] batchUpdate(ExecutorParameters<T, ?> executorParameters);
}
