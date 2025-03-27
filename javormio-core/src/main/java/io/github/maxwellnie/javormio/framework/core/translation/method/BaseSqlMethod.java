package io.github.maxwellnie.javormio.framework.core.translation.method;

import io.github.maxwellnie.javormio.framework.Context;

/**
 * @author Maxwell Nie
 */
public abstract class BaseSqlMethod implements SqlMethod{
    protected Context context;

    public BaseSqlMethod(Context context) {
        this.context = context;
    }

}
