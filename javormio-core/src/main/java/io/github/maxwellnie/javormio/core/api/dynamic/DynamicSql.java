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
    //模拟大量请求创建DynamicSql对象

//    public static void main(String[] args) throws InterruptedException {
//        DynamicSql<LambdaWhere.User> dynamicSql = new DynamicSql<>(LambdaWhere.User.class, null).lambdaWhere().end();
//        DynamicSql<LambdaWhere.User> dynamicSql1 = new DynamicSql<>(LambdaWhere.User.class, null).lambdaWhere().end();
//        DynamicSql<String> dynamicSql2 = new DynamicSql<>("", null).lambdaWhere().end();
//        System.out.println(dynamicSql.end == dynamicSql1.end);
//        System.out.println(dynamicSql1.end.equals(dynamicSql2.end));
//        System.out.println(dynamicSql.end);
//        System.out.println(dynamicSql1.end);
//        LinkedList<DynamicSql<String>> list = new LinkedList<>();
//        for (int i = 0; i < 100000000; i++){
//            DynamicSql<?> dynamicSql3 = i == 0? new DynamicSql<>("", null).lambdaWhere().end() : list.getLast();
//            list.add(dynamicSql3.reset(String.class, null).lambdaWhere().end());
//        }
//        AtomicBoolean flag = new AtomicBoolean(true);
//        list.forEach(dynamicSql3 -> {if (!dynamicSql3.end.equals(dynamicSql2.end))
//        flag.set(false);});
//        System.out.println(flag.get());
//        list = null;
//        Thread.sleep(100000);
//    }
}
