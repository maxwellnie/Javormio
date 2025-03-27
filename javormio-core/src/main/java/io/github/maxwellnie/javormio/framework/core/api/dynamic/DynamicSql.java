package io.github.maxwellnie.javormio.framework.core.api.dynamic;

/**
 * @author Maxwell Nie
 */
public class DynamicSql<E> {
    Class<?> entityClass;
    Where<E> where;
    String[] columns;
    Object entity;

}
