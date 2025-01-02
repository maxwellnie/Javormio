package io.github.maxwellnie.javormio.core.database.sql.executor;

import io.github.maxwellnie.javormio.core.database.jdbc.connection.ConnectionResource;
import io.github.maxwellnie.javormio.core.database.result.TypeMapping;
import io.github.maxwellnie.javormio.core.database.sql.ExecutableSql;
import io.github.maxwellnie.javormio.core.database.sql.SqlParameter;
import io.github.maxwellnie.javormio.core.database.sql.SqlType;

import java.sql.*;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 *
 * @author Maxwell Nie
 */
public class UpdateSqlExecutor extends BaseSqlExecutor{
    @Override
    public Object run(ExecutorContext executorContext) throws SQLException {
        ConnectionResource connectionResource = executorContext.getConnectionResource();
        ExecutableSql executableSql = executorContext.getExecutableSql();
        Map<String, Object> properties = executorContext.getProperties();
        Consumer<ResultSet> consumer = (Consumer<ResultSet>) properties.get("SGK");
        boolean selectGeneratedKeys = executableSql.getType().equals(SqlType.INSERT) && consumer!= null;
        Connection connection = connectionResource
                .getConnection();
        try(PreparedStatement preparedStatement = selectGeneratedKeys?connection.prepareStatement(executableSql.getSqlList()[0], Statement.RETURN_GENERATED_KEYS)
                :connection.prepareStatement(executableSql.getSqlList()[0])){
            List<SqlParameter[]> sqlParametersList = executableSql.getParametersList();
            SqlParameter[] sqlParameters = null;
            if (!sqlParametersList.isEmpty())
                sqlParameters = sqlParametersList.get(0);
            if(sqlParameters != null){
                int index = 1;
                for (SqlParameter sqlParameter : sqlParameters){
                    sqlParameter.getTypeHandler()
                            .setValue(preparedStatement, index++, sqlParameter.getValue());
                }
            }
            int updateCount = preparedStatement.executeUpdate();
            if (selectGeneratedKeys)
                consumer.accept(preparedStatement.getGeneratedKeys());
            return updateCount;
        }
    }
}
