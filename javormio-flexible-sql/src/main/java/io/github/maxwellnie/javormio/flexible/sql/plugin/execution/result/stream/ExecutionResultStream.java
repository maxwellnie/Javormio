package io.github.maxwellnie.javormio.flexible.sql.plugin.execution.result.stream;

import io.github.maxwellnie.javormio.core.execution.result.ResultParseException;
import io.github.maxwellnie.javormio.flexible.sql.plugin.execution.result.convertor.ResultContext;

/**
 * @author Maxwell Nie
 */
public class ExecutionResultStream extends BaseResultStream<ResultContext> {
    public ExecutionResultStream(ResultContext resultContext) {
        super(resultContext);
    }

    @Override
    public ResultContext receive() throws ResultParseException {
        return getExecutionResults();
    }
}
