package io.github.maxwellnie.javormio.flexible.sql.plugin;

import io.github.maxwellnie.javormio.core.translation.SqlParameter;

/**
 * @author Maxwell Nie
 */
public class MultipleRowSql extends Sql {
    protected MultipleRowSql prev;
    protected MultipleRowSql next;

    public MultipleRowSql(String sql, SqlParameter[] parameters, MultipleRowSql prev, MultipleRowSql next) {
        super(sql, parameters);
        this.prev = prev;
        this.next = next;
    }

    public MultipleRowSql(String sql, SqlParameter[] parameters) {
        super(sql, parameters);
    }

    public MultipleRowSql getPrev() {
        return prev;
    }

    public void setPrev(MultipleRowSql prev) {
        this.prev = prev;
    }

    public MultipleRowSql getNext() {
        return next;
    }

    public void setNext(MultipleRowSql next) {
        this.next = next;
    }
}
