package io.github.maxwellnie.javormio.framework.core.api.dynamic;

/**
 * Joining conditions to the where clause
 *
 * @author Maxwell Nie
 */
public class ConditionsLogic<E, W extends Where<E>> {
    W where;

    public ConditionsLogic(W where) {
        this.where = where;
    }

    public W and() {
        where.sqlBuilder.append(" AND");
        return where;
    }

    public W or() {
        where.sqlBuilder.append(" OR");
        return where;
    }

    public SqlFragment ok() {
        return where.ok();
    }
}
