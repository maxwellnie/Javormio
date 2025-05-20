package io.github.maxwellnie.javormio.flexible.sql.plugin.execution.result.stream;

import io.github.maxwellnie.javormio.core.execution.result.ResultParseException;
import io.github.maxwellnie.javormio.flexible.sql.plugin.execution.result.convertor.ExecutionResults;

/**
 * @author Maxwell Nie
 */
public class ExecutionResultStream extends BaseResultStream<ExecutionResults> {
    public ExecutionResultStream(ExecutionResults executionResults) {
        super(executionResults);
    }

    @Override
    public ExecutionResults receive() throws ResultParseException {
        return getExecutionResults();
    }
}
