package io.github.maxwellnie.javormio.core.execution;

import java.io.Closeable;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author Maxwell Nie
 */
public class StatementWrapper implements AutoCloseable {
    protected PreparedStatement statement;
    protected Object result;
    protected boolean autoClosed;

    public StatementWrapper(PreparedStatement statement, Object result, boolean autoClosed) {
        this.statement = statement;
        this.result = result;
        this.autoClosed = autoClosed;
    }

    public PreparedStatement getStatement() {
        return statement;
    }

    public Object getResult() {
        return result;
    }

    public boolean isAutoClosed() {
        return autoClosed;
    }

    @Override
    public void close() throws SQLException {
        if (autoClosed) {
            statement.close();
        }
    }

    public void setResult(Object result) {
        this.result = result;
    }
}
