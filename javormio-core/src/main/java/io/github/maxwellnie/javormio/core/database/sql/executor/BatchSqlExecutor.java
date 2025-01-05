package io.github.maxwellnie.javormio.core.database.sql.executor;

import io.github.maxwellnie.javormio.core.database.jdbc.connection.ConnectionResource;
import io.github.maxwellnie.javormio.core.database.result.ResultSetConvertor;
import io.github.maxwellnie.javormio.core.database.result.TypeMapping;
import io.github.maxwellnie.javormio.core.database.sql.ExecutableSql;
import io.github.maxwellnie.javormio.core.database.sql.SqlParameter;
import io.github.maxwellnie.javormio.core.database.sql.SqlType;
import io.github.maxwellnie.javormio.core.java.api.Constants;

import java.sql.*;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * @author yurongqi
 */
public class BatchSqlExecutor extends BaseSqlExecutor {

/*
* mmp抽象不出来
* */
    @Override
    public PreparedStatement run(ExecutorContext executorContext) throws SQLException {
        //获取连接资源、可执行sql、属性
        ConnectionResource connectionResource = executorContext.getConnectionResource();
        ExecutableSql executableSql = executorContext.getExecutableSql();
        Map<String, Object> properties = executorContext.getProperties();
        //获取类型映射
        TypeMapping typeMapping = executorContext.getTypeMapping();
        //获取结果集转换器----批量查询参数----BatchQuerySqlExecutor
        ResultSetConvertor resultSetConvertor = (ResultSetConvertor) properties.get(Constants.RESULT_SET_CONVERTOR);
        //生产者----批量更新参数----BatchUpdateSqlExecutor
        Consumer<ResultSet> consumer = (Consumer<ResultSet>) properties.get(Constants.SELECT_GENERATED_KEY);
        //是否需要查询生成的主键----批量更新参数----BatchUpdateSqlExecutor
        boolean selectGeneratedKeys = executableSql.getType().equals(SqlType.INSERT) && consumer != null;
        //获取连接对象
        Connection connection = connectionResource.getConnection();


        try (PreparedStatement preparedStatement = selectGeneratedKeys ? connection.prepareStatement(executableSql.getSqlList()[0], Statement.RETURN_GENERATED_KEYS)
                : connection.prepareStatement(executableSql.getSqlList()[0])) {
            //获取参数列表
            List<SqlParameter[]> sqlParametersList = executableSql.getParametersList();
            //当前行SQL的参数数组
//                SqlParameter[] sqlParameters = null;
            //判断参数列表是否为空，非空则设置当前行SQL参数数组
            if (!sqlParametersList.isEmpty()) {
                for (SqlParameter[] sqlParameters : sqlParametersList) {
                    if (sqlParameters != null) {
                        int index = 1;
                        //遍历参数
                        for (SqlParameter sqlParameter : sqlParameters) {
                            //获取参数的类型处理器，使用类型处理器设置报表的参数
                            //判断参数数组是否为空，如果不空则设置参数到报表，例如:SELECT * FROM tb_user WHERE id = ?; preparedStatement.setInt(1, 1);
                            sqlParameter.getTypeHandler().setValue(preparedStatement, index++, sqlParameter.getValue());
                        }
                    }
                    //添加批处理
                    preparedStatement.addBatch();
                }
            }
            return preparedStatement;
        }
    }

}


