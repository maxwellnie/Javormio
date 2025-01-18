package io.github.maxwellnie.javormio.core.database.sql.method;

import io.github.maxwellnie.javormio.core.OperationContext;

/**
 * @author Maxwell Nie
 */
public abstract class BaseSqlMethod implements SqlMethod{
    protected OperationContext operationContext;

    public BaseSqlMethod(OperationContext operationContext) {
        this.operationContext = operationContext;
    }

}
