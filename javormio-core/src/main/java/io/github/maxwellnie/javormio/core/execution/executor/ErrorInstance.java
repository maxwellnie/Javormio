package io.github.maxwellnie.javormio.core.execution.executor;

import io.github.maxwellnie.javormio.common.java.sql.SqlParameter;

import java.sql.SQLException;
import java.util.List;

/**
 * @author Maxwell Nie
 */
public class ErrorInstance {
    private static final String LINE_SEPARATOR = System.lineSeparator();
    private static final ThreadLocal<ErrorInstance> errorInstance = new ThreadLocal<>();
    public static ErrorInstance instance() {
        ErrorInstance instance = errorInstance.get();
        if (instance == null) {
            instance = new ErrorInstance();
            errorInstance.set(instance);
        }else
            instance.reset();
        return instance;
    }
    private String sql;
    private List<SqlParameter[]> parametersList;
    private String sqlErrorMsg;
    private Throwable cause;

    public void setSql(String sql) {
        this.sql = sql;
    }

    public void setParametersList(List<SqlParameter[]> parametersList) {
        this.parametersList = parametersList;
    }

    public void setSqlErrorMsg(String sqlErrorMsg) {
        this.sqlErrorMsg = sqlErrorMsg;
    }

    public void setCause(Throwable cause) {
        this.cause = cause;
    }
    public void setSQLError(SQLException sqlError){
        this.sqlErrorMsg = sqlError.getMessage();
        this.cause = sqlError;
    }

    public void reset() {
        sql = null;
        parametersList = null;
        sqlErrorMsg = null;
        cause = null;
    }
    @Override
    public String toString() {
        StringBuilder msg = new StringBuilder(LINE_SEPARATOR);
        if (sql != null)msg.append("**** oops! Your SQL has one even many errors!");
        else msg.append("**** oops! SqlExecutor occurs error in executing!");
        if (sqlErrorMsg != null){
            msg.append(LINE_SEPARATOR)
                    .append("ERROR MESSAGE: ")
                    .append(sqlErrorMsg);
        }
        if (sql != null){
            msg.append(LINE_SEPARATOR)
                    .append("SQL: ").append(sql);
            if (parametersList != null){
                msg.append(LINE_SEPARATOR)
                        .append("SQL PARAMETERS: ");
                for (SqlParameter[] currentParams : parametersList){
                    msg.append("{");
                    for (SqlParameter currentParam : currentParams){
                        msg.append(currentParam.getValue()).append(",");
                    }
                    msg.append("} ");
                }
            }
        }
        if (cause != null) {
            msg.append(LINE_SEPARATOR);
            msg.append("Cause: ");
            msg.append(cause);
        }
        return msg.toString();
    }
}
