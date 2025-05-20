package io.github.maxwellnie.javormio.core.translation.method;

/**
 * @author Maxwell Nie
 */
public abstract class BaseSqlMethod<T> implements SqlMethod {
    protected Class<T> entityClass;

    public BaseSqlMethod(Class<T> entityClass) {
        this.entityClass = entityClass;
    }
}
