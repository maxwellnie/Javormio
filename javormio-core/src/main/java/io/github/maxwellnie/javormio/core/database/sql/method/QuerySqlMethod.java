package io.github.maxwellnie.javormio.core.database.sql.method;

import io.github.maxwellnie.javormio.core.OperationContext;
import io.github.maxwellnie.javormio.core.database.sql.ExecutableSql;
import io.github.maxwellnie.javormio.core.database.sql.SqlParameter;
import io.github.maxwellnie.javormio.core.database.sql.SqlType;
import io.github.maxwellnie.javormio.core.database.sql.executor.ExecutorContext;
import io.github.maxwellnie.javormio.core.database.sql.executor.QuerySqlExecutor;
import io.github.maxwellnie.javormio.core.database.sql.executor.SqlExecutor;

import java.sql.SQLException;
import java.util.List;

/**
 * @author Maxwell Nie
 */
public class QuerySqlMethod extends BaseSqlMethod {
    public QuerySqlMethod(OperationContext operationContext) {
        super(operationContext);
    }

    @Override
    public Object invokeExactly(int methodFeatureCode, Object... args) throws Throwable{
        return query(methodFeatureCode, (String) args[0], (List<Object>) args[1]);
    }
    protected Object query(int methodFeatureCode, String sql, List<Object> params) throws SQLException {
        //1. 获取执行器
        SqlExecutor sqlExecutor = operationContext.getSqlExecutor(QuerySqlExecutor.class);
        //2. 构建可执行sql
        ExecutableSql executableSql = new ExecutableSql();
        //2.1 设置sql
        executableSql.setSqlList(new String[]{sql});
        //2.2 设置参数
        SqlParameter[] sqlParameters = new SqlParameter[params.size()];
        for (int i = 0; i < params.size(); i++){
            sqlParameters[i] = new SqlParameter(params.get(i), operationContext.getTypeHandler(params.get(i)));
        }
        executableSql.getParametersList().add(sqlParameters);
        //2.3 设置sql类型
        executableSql.setType(SqlType.SELECT);
        //3. 构建执行上下文
        ExecutorContext executorContext = new ExecutorContext(
                operationContext.getConnectionResource(), executableSql,
                //获取类型映射
                operationContext
                        .getDaoMethodFeature(methodFeatureCode)
                        .typeMapping);
        //4. 执行
        return sqlExecutor.run(executorContext);
    }
    @Override
    public Object invoke(int methodFeatureCode, Object[] args) throws Throwable{
        return query(methodFeatureCode, (String) args[0], (List<Object>) args[1]);
    }
}
