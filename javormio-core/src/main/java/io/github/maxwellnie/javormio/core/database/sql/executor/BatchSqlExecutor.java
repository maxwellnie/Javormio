package io.github.maxwellnie.javormio.core.database.sql.executor;

import io.github.maxwellnie.javormio.core.database.jdbc.connection.ConnectionResource;
import io.github.maxwellnie.javormio.core.database.result.ResultSetConvertor;
import io.github.maxwellnie.javormio.core.database.result.TypeMapping;
import io.github.maxwellnie.javormio.core.database.sql.ExecutableSql;
import io.github.maxwellnie.javormio.core.database.sql.SqlParameter;
import io.github.maxwellnie.javormio.core.java.api.Constants;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import static io.github.maxwellnie.javormio.core.database.sql.SqlType.isInsert;
import static io.github.maxwellnie.javormio.core.database.sql.SqlType.isMayChange;

/**
 * @author yurongqi
 */
public class BatchSqlExecutor extends BaseSqlExecutor {


    @Override
    public Object run(ExecutorContext executorContext) throws SQLException {
        //获取连接资源、可执行sql、属性
        ConnectionResource connectionResource = executorContext.getConnectionResource();
        ExecutableSql executableSql = executorContext.getExecutableSql();
        Map<String, Object> properties = executorContext.getProperties();
        //获取类型映射
        TypeMapping typeMapping = executorContext.getTypeMapping();
        //获取结果集转换器----批量查询参数
        ResultSetConvertor resultSetConvertor = (ResultSetConvertor) properties.get(Constants.RESULT_SET_CONVERTOR);
        //判断SQL类型是否是INSERT或UPDATE或DELETE
        boolean mayChange = isMayChange(executableSql.getType());
        //消费者----批量更新参数
        Consumer<ResultSet> consumer = (Consumer<ResultSet>) properties.get(Constants.SELECT_GENERATED_KEY);
        //是否需要查询生成的主键----批量更新参数
        boolean selectGeneratedKeys = mayChange && isInsert(executableSql.getType()) && consumer != null;
        //获取连接对象
        Connection connection = connectionResource.getConnection();
        try (PreparedStatement preparedStatement = selectGeneratedKeys ? connection.prepareStatement(executableSql.getSqlList()[0], Statement.RETURN_GENERATED_KEYS)
                : connection.prepareStatement(executableSql.getSqlList()[0])) {
            //获取参数列表
            List<SqlParameter[]> sqlParametersList = executableSql.getParametersList();
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
            //提交到数据库
            int[] updateCounts = preparedStatement.executeBatch();
            //若是INSERT或UPDATE或DELETE，则返回更新的影响行数，否则返回查询结果集
            if (mayChange) {
                //判断是否需要获取生成的主键值，如果需要则交给消费者处理
                if (selectGeneratedKeys)
                    handleGeneratedKeysOfInsert(updateCounts, consumer, preparedStatement);
                return updateCounts;
            } else {
                return handleResultSetOfQuery(updateCounts, preparedStatement, resultSetConvertor, typeMapping);
            }
        }
    }

    /**
     * 处理批量插入生成的主键
     *
     * @param updateCounts      批量更新的影响行数
     * @param consumer          消费者
     * @param preparedStatement 预处理报表
     * @throws SQLException
     */
    private void handleGeneratedKeysOfInsert(int[] updateCounts, Consumer<ResultSet> consumer, PreparedStatement preparedStatement) throws SQLException {

        for (int flag : updateCounts) {
            if (flag != Statement.EXECUTE_FAILED) {
                consumer.accept(preparedStatement.getGeneratedKeys());
            }
        }
    }

    /**
     * 处理批量查询的结果集
     *
     * @param updateCounts       批量更新的影响行数
     * @param preparedStatement  预处理报表
     * @param resultSetConvertor 结果集转换器
     * @param typeMapping        类型映射
     * @return Object
     * @throws SQLException
     */
    private Object handleResultSetOfQuery(int[] updateCounts, PreparedStatement preparedStatement, ResultSetConvertor resultSetConvertor, TypeMapping typeMapping) throws SQLException {
        //收集产生的结果集
        LinkedList<ResultSet> list = new LinkedList<>();
        for (int flag : updateCounts) {
            if (flag == -2) {
                list.add(preparedStatement.getResultSet());
            }
        }
        //转换结果集到Java对象
        return resultSetConvertor.convert(list, typeMapping);
    }

}


