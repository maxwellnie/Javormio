package io.github.maxwellnie.javormio.framework.core.translation.method;

import io.github.maxwellnie.javormio.framework.Context;
import io.github.maxwellnie.javormio.framework.core.execution.result.ConvertException;
import io.github.maxwellnie.javormio.framework.core.execution.ExecutableSql;
import io.github.maxwellnie.javormio.framework.core.translation.SqlParameter;
import io.github.maxwellnie.javormio.framework.core.translation.SqlType;
import io.github.maxwellnie.javormio.framework.core.execution.ExecutorContext;
import io.github.maxwellnie.javormio.framework.core.execution.QuerySqlExecutor;
import io.github.maxwellnie.javormio.framework.core.execution.SqlExecutor;

import java.sql.SQLException;
import java.util.List;

/**
 * @author Maxwell Nie
 */
public class QuerySqlMethod extends BaseSqlMethod {
    public QuerySqlMethod(Context context) {
        super(context);
    }
    protected Object query(int methodFeatureCode, String sql, List<Object> params) throws SQLException, ConvertException {
        //1. 获取执行器
        SqlExecutor sqlExecutor = context.getSqlExecutor(QuerySqlExecutor.class);
        //2. 构建可执行sql
        ExecutableSql executableSql = new ExecutableSql();
        //2.1 设置sql
        executableSql.setSqlList(new String[]{sql});
        //2.2 设置参数
        SqlParameter[] sqlParameters = new SqlParameter[params.size()];
        for (int i = 0; i < params.size(); i++){
            sqlParameters[i] = new SqlParameter(params.get(i), context.getTypeHandler(params.get(i)));
        }
        executableSql.getParametersList().add(sqlParameters);
        //2.3 设置sql类型
        executableSql.setType(SqlType.SELECT);
        //3. 构建执行上下文
        ExecutorContext executorContext = new ExecutorContext(
                context.getConnectionResource(),
                executableSql,
                //获取类型映射
                context.getDaoMethodFeature(methodFeatureCode),
                context.getResultSetConvertor(),
                null
        );
        //4. 执行
        return sqlExecutor.run(executorContext);
    }
    @Override
    public Object invoke(int methodFeatureCode, Object[] args) throws Throwable{
        return query(methodFeatureCode, (String) args[0], (List<Object>) args[1]);
    }
}
