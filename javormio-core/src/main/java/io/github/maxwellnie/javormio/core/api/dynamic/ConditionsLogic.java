package io.github.maxwellnie.javormio.core.api.dynamic;

import io.github.maxwellnie.javormio.common.java.reflect.method.Chainable;

/**
 * Joining conditions to the where clause
 *
 * @author Maxwell Nie
 */
public class ConditionsLogic<E, P, W extends Where<E, P>> implements Chainable<P> {
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

    @Override
    public P end() {
        return where.end();
    }
}
