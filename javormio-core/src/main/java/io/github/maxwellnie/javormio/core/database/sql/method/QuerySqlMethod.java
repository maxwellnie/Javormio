package io.github.maxwellnie.javormio.core.database.sql.method;

import io.github.maxwellnie.javormio.core.DataAPIContext;
import io.github.maxwellnie.javormio.core.database.sql.ExecutableSql;
import io.github.maxwellnie.javormio.core.database.sql.executor.ExecutorContext;
import io.github.maxwellnie.javormio.core.database.sql.executor.QuerySqlExecutor;
import io.github.maxwellnie.javormio.core.database.sql.executor.SqlExecutor;

import java.util.List;

/**
 * @author Maxwell Nie
 */
public class QuerySqlMethod extends BaseSqlMethod {
    public QuerySqlMethod(DataAPIContext dataAPIContext) {
        super(dataAPIContext);
    }

    @Override
    public Object invokeExactly(Object... args) {
        String sql = (String) args[0];
        List<Object> params = (List<Object>) args[1];
        return query(sql, params);
    }
    protected Object query(String sql, List<Object> params){
        SqlExecutor sqlExecutor = dataAPIContext.getSqlExecutor(QuerySqlExecutor.class);
        ExecutableSql executableSql = new ExecutableSql();
        executableSql.setSqlList(new String[]{sql});
        executableSql.getParametersList().add(params.);
        ExecutorContext executorContext = new ExecutorContext();
        return sqlExecutor.run();
    }
    @Override
    public Object invoke(Object[] args) {
        return null;
    }
}
