package io.github.maxwellnie.javormio.flexible.sql.plugin.result.stream;

import io.github.maxwellnie.javormio.flexible.sql.plugin.result.ExecutionResults;
import io.github.maxwellnie.javormio.flexible.sql.plugin.result.ResultParseException;

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
