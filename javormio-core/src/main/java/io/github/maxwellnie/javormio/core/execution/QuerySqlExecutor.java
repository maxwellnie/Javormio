package io.github.maxwellnie.javormio.core.execution;

import io.github.maxwellnie.javormio.common.java.api.ObjectMap;
import io.github.maxwellnie.javormio.common.java.jdbc.connection.ConnectionResource;
import io.github.maxwellnie.javormio.common.java.jdbc.connection.JConnectionResource;
import io.github.maxwellnie.javormio.core.execution.result.ConvertException;
import io.github.maxwellnie.javormio.core.execution.result.ResultSetConvertor;
import io.github.maxwellnie.javormio.core.translation.SqlParameter;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * 查询语句执行器（单行）
 *
 * @author Maxwell Nie
 */
public class QuerySqlExecutor extends BaseSqlExecutor {
    @Override
    public <T> StatementWrapper run(ExecutorContext<T> executorContext) throws ConvertException {
        ConnectionResource connectionResource = executorContext.getConnectionResource();
        ExecutableSql executableSql = executorContext.getExecutableSql();
        //获取类型映射
        ObjectMap<ResultSet, T> typeMapping = executorContext.getDaoMethodFeature().getTypeMapping();
        //获取结果集转换器
        ResultSetConvertor resultSetConvertor = executorContext.getResultSetConvertor();
        //获取连接对象
        Connection connection = connectionResource
                .getConnection();
        //打开报表
        try (StatementWrapper statementWrapper = new StatementWrapper(connection.prepareStatement(executableSql.getSqlList()[0]), null, executorContext.isAutoConvert())) {
            //获取参数列表
            List<SqlParameter[]> sqlParametersList = executableSql.getParametersList();
            //当前行SQL的参数数组
            SqlParameter[] sqlParameters = null;
            //判断参数列表是否为空，非空则设置当前行SQL参数数组
            if (!sqlParametersList.isEmpty())
                sqlParameters = sqlParametersList.get(0);
            //判断参数数组是否为空，如果不空则设置参数到报表，例如:SELECT * FROM tb_user WHERE id = ?; preparedStatement.setInt(1, 1);
            if (sqlParameters != null) {
                int index = 1;
                //遍历参数
                for (SqlParameter sqlParameter : sqlParameters) {
                    //获取参数的类型处理器，使用类型处理器设置报表的参数
                    sqlParameter.getTypeHandler()
                            .setValue(statementWrapper.statement, index++, sqlParameter.getValue());
                }
            }
            if (executorContext.isAutoConvert())
                statementWrapper.setResult(resultSetConvertor.convert(statementWrapper.statement.executeQuery(), typeMapping, executorContext.getInstanceMethodInvoker()));
            else
                statementWrapper.setResult(statementWrapper.statement.executeQuery());
            return statementWrapper;
        }catch (SQLException e){
            throw new ConvertException(e);
        }
    }
}
