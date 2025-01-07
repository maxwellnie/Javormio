package io.github.maxwellnie.javormio.core.database.sql.method;

import io.github.maxwellnie.javormio.core.DataAPIContext;

/**
 * @author Maxwell Nie
 */
public abstract class BaseSqlMethod implements SqlMethod{
    protected DataAPIContext dataAPIContext;

    public BaseSqlMethod(DataAPIContext dataAPIContext) {
        this.dataAPIContext = dataAPIContext;
    }

}
