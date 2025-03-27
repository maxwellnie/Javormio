package io.github.maxwellnie.javormio.framework.core.executor;

import io.github.maxwellnie.javormio.framework.core.db.jdbc.connection.ConnectionResource;
import io.github.maxwellnie.javormio.framework.core.executor.result.ConvertException;
import io.github.maxwellnie.javormio.framework.core.executor.result.ResultSetConvertor;
import io.github.maxwellnie.javormio.framework.core.executor.result.TypeMapping;
import io.github.maxwellnie.javormio.framework.core.translate.SqlParameter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * 查询语句执行器（单行）
 *
 * @author Maxwell Nie
 */
public class QuerySqlExecutor extends BaseSqlExecutor {
    @Override
    public Object run(ExecutorContext executorContext) throws SQLException, ConvertException {
        ConnectionResource connectionResource = executorContext.getConnectionResource();
        ExecutableSql executableSql = executorContext.getExecutableSql();
        //获取类型映射
        TypeMapping typeMapping = executorContext.getDaoMethodFeature().getTypeMapping();
        //获取结果集转换器
        ResultSetConvertor resultSetConvertor = executorContext.getResultSetConvertor();
        //获取连接对象
        Connection connection = connectionResource
                .getConnection();
        boolean multipleTable = executorContext.getDaoMethodFeature().isMultipleTable();
        //打开报表
        try (PreparedStatement preparedStatement = connection.prepareStatement(executableSql.getSqlList()[0])) {
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
                            .setValue(preparedStatement, index++, sqlParameter.getValue());
                }
            }
            return resultSetConvertor.convert(preparedStatement.executeQuery(), typeMapping, multipleTable);
        }
    }
}
