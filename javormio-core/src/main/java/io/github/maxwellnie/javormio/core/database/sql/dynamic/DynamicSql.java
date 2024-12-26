package io.github.maxwellnie.javormio.core.database.sql.dynamic;

/**
 * @author Maxwell Nie
 */
public class DynamicSql<E> {
    Class<?> entityClass;
    Where<E> where;
    String[] columns;
    Object entity;

}
