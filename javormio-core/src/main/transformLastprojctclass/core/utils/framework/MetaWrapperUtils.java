package com.maxwellnie.velox.sql.core.utils.framework;

import com.maxwellnie.velox.sql.core.natives.exception.ExecutorException;
import com.maxwellnie.velox.sql.core.natives.database.statement.StatementWrapper;

/**
 * @author Maxwell Nie
 */
public class MetaWrapperUtils {
    public static <T> T of(StatementWrapper statementWrapper, String key) throws ExecutorException {
        try {
            return (T) statementWrapper.getProperty(key);
        } catch (Throwable throwable) {
            throw new ExecutorException(key + " not found.", throwable);
        }
    }
}
