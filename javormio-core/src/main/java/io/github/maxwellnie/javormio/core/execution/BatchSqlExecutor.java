package io.github.maxwellnie.javormio.core.execution;

import io.github.maxwellnie.javormio.common.java.api.Constants;
import io.github.maxwellnie.javormio.core.execution.result.ConvertException;
import io.github.maxwellnie.javormio.core.execution.result.ResultSetConvertor;
import io.github.maxwellnie.javormio.core.execution.result.TypeMapping;
import io.github.maxwellnie.javormio.core.translation.SqlParameter;
import io.github.maxwellnie.javormio.core.translation.SqlType;
import io.github.maxwellnie.javormio.common.java.jdbc.connection.ConnectionResource;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * 批处理语句执行器
 *
 * @author yurongqi
 */
public class BatchSqlExecutor extends BaseSqlExecutor {
    @Override
    public Object run(ExecutorContext executorContext) throws SQLException, ConvertException {
        //获取连接资源、可执行sql、属性
        ConnectionResource connectionResource = executorContext.getConnectionResource();
        ExecutableSql executableSql = executorContext.getExecutableSql();
        Map<String, Object> properties = executorContext.getProperties();
        //获取类型映射
        TypeMapping typeMapping = executorContext.getDaoMethodFeature().getTypeMapping();
        //获取结果集转换器----批量查询参数
        ResultSetConvertor resultSetConvertor = executorContext.getResultSetConvertor();
        //判断SQL类型是否是INSERT或UPDATE或DELETE
        boolean isDataModifyingSqlType = SqlType.isDataModifyingSqlType(executableSql.getType());
        //消费者----批量更新参数
        Consumer<ResultSet> consumer = (Consumer<ResultSet>) properties.get(Constants.SELECT_GENERATED_KEY);
        //是否需要查询生成的主键----批量添加参数
        boolean selectGeneratedKeys = isDataModifyingSqlType && SqlType.isInsert(executableSql.getType()) && consumer != null;
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
                            sqlParameter.getTypeHandler()
                                    .setValue(preparedStatement, index++, sqlParameter.getValue());
                        }
                    }
                    //添加批处理
                    preparedStatement.addBatch();
                }
            }
            //提交到数据库
            int[] updateCounts = preparedStatement.executeBatch();
            //若是INSERT或UPDATE或DELETE，则返回更新的影响行数，否则返回查询结果集
            if (isDataModifyingSqlType) {
                //判断是否需要获取生成的主键值，如果需要则交给消费者处理
                if (selectGeneratedKeys)
                    handleGeneratedKeysOfInsert(updateCounts, consumer, preparedStatement);
                return updateCounts;
            } else {
                boolean multipleTable = executorContext.getDaoMethodFeature().isMultipleTable();
                return handleResultSetOfQuery(updateCounts, preparedStatement, resultSetConvertor, typeMapping, multipleTable);
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
        for (int ignored : updateCounts)
            consumer.accept(preparedStatement.getGeneratedKeys());
    }

    /**
     * 处理批量查询的结果集
     *
     * @param updateCounts       批量更新的影响行数
     * @param preparedStatement  预处理报表
     * @param resultSetConvertor 结果集转换器
     * @param typeMapping        类型映射
     * @param multipleTable      是否是多表查询
     * @return Object
     * @throws SQLException
     */
    private Object handleResultSetOfQuery(int[] updateCounts, PreparedStatement preparedStatement, ResultSetConvertor resultSetConvertor, TypeMapping typeMapping, boolean multipleTable) throws SQLException, ConvertException {
        //收集产生的结果集
        LinkedList<ResultSet> list = new LinkedList<>();
        for (int ignored : updateCounts) {
            list.add(preparedStatement.getResultSet());
        }
        //转换结果集到Java对象
        return resultSetConvertor.convert(list, typeMapping, multipleTable);
    }

}


