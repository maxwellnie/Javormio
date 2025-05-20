package io.github.maxwellnie.javormio.core.api.dynamic;

import io.github.maxwellnie.javormio.common.java.reflect.method.Action;
import io.github.maxwellnie.javormio.core.translation.table.TableInfo;

/**
 * @author Maxwell Nie
 */
public class DynamicSql<E> {
    TableInfo tableInfo;
    Where<E, DynamicSql<E>> where;
    E entity;
    Class<E> entityClass;
    SqlFragment whereFragment;
    final Action<DynamicSql<E>, SqlFragment, DynamicSql<E>> callback = (dynamicSql, sqlFragment) -> {
        dynamicSql.whereFragment = sqlFragment;
        return dynamicSql;
    };

    public DynamicSql(Class<E> entityClass, TableInfo tableInfo) {
        this.entityClass = entityClass;
        this.tableInfo = tableInfo;
    }

    @SuppressWarnings("unchecked")
    public DynamicSql(E entity, TableInfo tableInfo) {
        this.entity = entity;
        if (entity != null)
            this.entityClass = (Class<E>) entity.getClass();
        this.tableInfo = tableInfo;
    }

    public LambdaWhere<E, DynamicSql<E>> lambdaWhere() {
        if (where != null && where instanceof LambdaWhere)
            return (LambdaWhere<E, DynamicSql<E>>) where;
        LambdaWhere<E, DynamicSql<E>> lambdaWhere = new LambdaWhere<>(this, callback, tableInfo);
        where = lambdaWhere;
        return lambdaWhere;
    }

    public Where<E, DynamicSql<E>> where() {
        if (where != null)
            return where;
        Where<E, DynamicSql<E>> where = new Where<>(this, callback, tableInfo);
        this.where = where;
        return where;
    }

    @SuppressWarnings("unchecked")
    public <C> DynamicSql<C> reset(Class<C> clazz, TableInfo tableInfo) {
        this.whereFragment = null;
        this.where = null;
        this.entity = null;
        this.tableInfo = null;
        DynamicSql<C> dynamicSql = (DynamicSql<C>) this;
        dynamicSql.tableInfo = tableInfo;
        dynamicSql.entityClass = clazz;
        return dynamicSql;
    }

    @SuppressWarnings("unchecked")
    public <C> DynamicSql<C> reset(C o, Class<C> clazz, TableInfo tableInfo) {
        this.whereFragment = null;
        this.where = null;
        this.entity = null;
        this.tableInfo = null;
        DynamicSql<C> dynamicSql = (DynamicSql<C>) this;
        dynamicSql.tableInfo = tableInfo;
        dynamicSql.entityClass = clazz;
        dynamicSql.entity = o;
        return dynamicSql;
    }
}
