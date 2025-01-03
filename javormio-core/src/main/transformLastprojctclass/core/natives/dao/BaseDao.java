package com.maxwellnie.velox.sql.core.natives.dao;

import com.maxwellnie.velox.sql.core.annotation.dao.BasicDaoDeclared;
import com.maxwellnie.velox.sql.core.annotation.dao.SQLMethod;
import com.maxwellnie.velox.sql.core.proxy.dao.executor.impl.DeleteMethodExecutor;
import com.maxwellnie.velox.sql.core.proxy.dao.executor.impl.InsertMethodExecutor;
import com.maxwellnie.velox.sql.core.proxy.dao.executor.impl.QueryMethodExecutor;
import com.maxwellnie.velox.sql.core.proxy.dao.executor.impl.UpdateMethodExecutor;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * @author Maxwell Nie
 */
@BasicDaoDeclared
public interface BaseDao<T> {
    @SQLMethod(InsertMethodExecutor.class)
    int insert(T t);

    @SQLMethod(DeleteMethodExecutor.class)
    int delete(SqlDecorator<T> sqlDecorator);

    @SQLMethod(UpdateMethodExecutor.class)
    int update(T t, SqlDecorator<T> sqlDecorator);

    @SQLMethod(InsertMethodExecutor.class)
    int[] batchInsert(Collection<T> t);

    @SQLMethod(DeleteMethodExecutor.class)
    int[] batchDelete(Serializable[] ids);

    @SQLMethod(QueryMethodExecutor.class)
    List<T> select(SqlDecorator<T> sqlDecorator);

    default T selectOne(SqlDecorator<T> sqlDecorator) {
        List<T> list = select(sqlDecorator);
        return Optional.ofNullable(list).map(ts -> ts.size() > 0 ? ts.get(0) : null).orElse(null);
    }

    default List<T> selectAll() {
        return select(null);
    }

    @SQLMethod(QueryMethodExecutor.class)
    long count(SqlDecorator<T> sqlDecorator);

    @SQLMethod(QueryMethodExecutor.class)
    Page<T> selectPage(Page<T> page, SqlDecorator<T> sqlDecorator);
}
