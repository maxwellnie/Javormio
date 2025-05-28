package io.github.maxwellnie.javormio.core.translation.method.impl;

import io.github.maxwellnie.javormio.common.java.api.JavormioException;
import io.github.maxwellnie.javormio.common.java.reflect.MethodFeature;
import io.github.maxwellnie.javormio.common.java.type.NullTypeHandler;
import io.github.maxwellnie.javormio.common.java.type.TypeHandler;
import io.github.maxwellnie.javormio.core.Context;
import io.github.maxwellnie.javormio.core.api.SqlOperation;
import io.github.maxwellnie.javormio.core.execution.executor.SingleSqlExecutor;
import io.github.maxwellnie.javormio.core.execution.executor.parameter.ExecutableSql;
import io.github.maxwellnie.javormio.core.execution.executor.parameter.ExecutorParameters;
import io.github.maxwellnie.javormio.core.execution.result.ResultParseException;
import io.github.maxwellnie.javormio.core.execution.result.ResultSetConvertor;
import io.github.maxwellnie.javormio.core.execution.statement.StatementHelper;
import io.github.maxwellnie.javormio.common.java.sql.SqlParameter;
import io.github.maxwellnie.javormio.common.java.sql.SqlType;
import io.github.maxwellnie.javormio.core.translation.method.SqlMethod;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

/**
 * @author Maxwell Nie
 */
public class NativeSelect implements SqlMethod<List<Map<String, Object>>>{
    protected final MethodFeature daoMethodFeature;
    {
        daoMethodFeature  = new MethodFeature(null, Objects.hash(SqlOperation.class, "Select", String.class, Object[].class, Map.class));
    }
    protected final ResultSetConvertor<List<Map<String, Object>>> resultSetConvertor = new NResultSetConvertor();
    protected final Context context;

    public NativeSelect(Context context) {
        this.context = context;
    }

    @Override
    public List<Map<String, Object>> invoke(Object[] args) throws JavormioException {
        SingleSqlExecutor singleSqlExecutor = context.getSqlExecutor(SingleSqlExecutor.class);
        StatementHelper statementHelper = context.getStatementHelper(daoMethodFeature);
        ExecutorParameters<?, List<Map<String, Object>>> executorParameters = new ExecutorParameters<>();
        executorParameters.setStatementHelper(statementHelper);
        executorParameters.setConnection(context.getConnection());
        ExecutableSql executableSql = new ExecutableSql();
        executableSql.setSql(args[0].toString());
        SqlParameter[] parameters = new SqlParameter[((Object[]) args[1]).length];
        int i =  0;
        for (Object arg : (Object[]) args[1]){
            TypeHandler typeHandler = null;
            if (arg == null){
                typeHandler = NullTypeHandler.INSTANCE;
            }else
                typeHandler = context.getTypeHandler(arg);
            SqlParameter parameter = new SqlParameter(arg, typeHandler);
            parameters[i++] = parameter;
        }
        executableSql.setParametersList(Collections.singletonList(parameters));
        executableSql.setType(SqlType.SELECT);
        executorParameters.setExecutableSql(executableSql);
        executorParameters.setNamespace(SqlOperation.class.getName());
        executorParameters.setResultSetConvertor(resultSetConvertor);
        return singleSqlExecutor.query(executorParameters);
    }
    static class NResultSetConvertor implements ResultSetConvertor<List<Map<String, Object>>> {
        @Override
        public List<Map<String, Object>> convert(Statement statement) throws SQLException, ResultParseException {
            ResultSet resultSet = statement.getResultSet();
            int columnCount = resultSet.getMetaData().getColumnCount();
            String[] columnNames = new String[columnCount];
            for (int i = 1; i <= columnCount; i++){
                columnNames[i - 1] = resultSet.getMetaData().getColumnName(i);
            }
            List<Map<String, Object>> result = new LinkedList<>();
            while (resultSet.next()) {
                Map<String, Object> map = new LinkedHashMap<>();
                for (int i = 1; i <= columnCount; i++){
                    map.put(columnNames[i - 1], resultSet.getObject(i));
                }
                result.add(map);
            }
            resultSet.close();
            return result;
        }
    }
}
