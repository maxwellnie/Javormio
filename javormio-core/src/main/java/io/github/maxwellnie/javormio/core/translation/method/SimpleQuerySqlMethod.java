package io.github.maxwellnie.javormio.core.translation.method;

import io.github.maxwellnie.javormio.core.Context;
import io.github.maxwellnie.javormio.core.execution.ExecutableSql;
import io.github.maxwellnie.javormio.core.execution.ExecutorContext;
import io.github.maxwellnie.javormio.core.execution.QuerySqlExecutor;
import io.github.maxwellnie.javormio.core.execution.SqlExecutor;
import io.github.maxwellnie.javormio.core.translation.SqlParameter;
import io.github.maxwellnie.javormio.core.translation.SqlType;

import java.util.List;

/**
 * 查询方法
 *
 * @author Maxwell Nie
 */
public class SimpleQuerySqlMethod extends BaseSqlMethod {
    /**
     * 执行查询方法，参数列表为[String.class, List.class]
     *
     * @param methodFeatureCode 方法特征码
     * @param context           上下文
     * @param args              参数
     * @return 结果
     * @throws Throwable
     */
    @SuppressWarnings("unchecked")
    @Override
    public Object invoke(int methodFeatureCode, Context context, Object[] args) throws Throwable {
        String sql = (String) args[0];
        List<Object> params = (List<Object>) args[1];
        //1. 获取执行器
        SqlExecutor sqlExecutor = context.getSqlExecutor(QuerySqlExecutor.class);
        //2. 构建可执行sql
        ExecutableSql executableSql = new ExecutableSql();
        //2.1 设置sql
        executableSql.setSqlList(new String[]{sql});
        //2.2 设置参数
        SqlParameter[] sqlParameters = new SqlParameter[params.size()];
        for (int i = 0; i < params.size(); i++) {
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
}
