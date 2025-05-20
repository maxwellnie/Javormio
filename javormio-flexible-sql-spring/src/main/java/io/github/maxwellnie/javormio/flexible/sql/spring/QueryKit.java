package io.github.maxwellnie.javormio.flexible.sql.spring;

import io.github.maxwellnie.javormio.core.translation.table.BaseMetaTableInfo;
import io.github.maxwellnie.javormio.flexible.sql.plugin.AliasTable;
import io.github.maxwellnie.javormio.flexible.sql.plugin.FlexibleSqlContext;
import io.github.maxwellnie.javormio.flexible.sql.plugin.execution.query.QueryBuilder;
import io.github.maxwellnie.javormio.flexible.sql.plugin.execution.query.SubqueryTable;

import javax.annotation.Resource;

/**
 * @author Maxwell Nie
 */
public class QueryKit {
    @Resource
    protected FlexibleSqlContext flexibleSqlContext;

    public QueryKit() {
    }

    public QueryKit(FlexibleSqlContext flexibleSqlContext) {
        this.flexibleSqlContext = flexibleSqlContext;
    }

    public<T> QueryBuilder<T> from(BaseMetaTableInfo<T> table){
        return QueryBuilder.from(table, flexibleSqlContext);
    }
    public<T> QueryBuilder<T> from(BaseMetaTableInfo<T> table, AliasTable aliasTable){
        return QueryBuilder.from(table, flexibleSqlContext, aliasTable);
    }
    public<T> QueryBuilder<T> from(BaseMetaTableInfo<T> table, String alias){
        return QueryBuilder.from(table, alias, flexibleSqlContext);
    }
    public<T, R> QueryBuilder<R> fromSubquery(SubqueryTable<R, T> table, String alias, FlexibleSqlContext context){
        return QueryBuilder.fromSubquery(table, alias, context);
    }
    public<T, R> QueryBuilder<R> fromSubquery(SubqueryTable<R, T> table, String alias, FlexibleSqlContext context, AliasTable aliasTable){
        return QueryBuilder.fromSubquery(table, context, aliasTable);
    }

    public FlexibleSqlContext getFlexibleSqlContext() {
        return flexibleSqlContext;
    }

    public void setFlexibleSqlContext(FlexibleSqlContext flexibleSqlContext) {
        this.flexibleSqlContext = flexibleSqlContext;
    }
}
