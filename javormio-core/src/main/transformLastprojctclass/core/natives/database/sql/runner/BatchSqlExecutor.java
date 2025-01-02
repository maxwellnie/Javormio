package com.maxwellnie.velox.sql.core.natives.database.sql.runner;

import com.maxwellnie.velox.sql.core.natives.exception.ClassTypeException;
import com.maxwellnie.velox.sql.core.natives.database.sql.row.RowSql;
import com.maxwellnie.velox.sql.core.natives.database.statement.StatementWrapper;
import com.maxwellnie.velox.sql.core.utils.jdbc.StatementUtils;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @author Maxwell Nie
 */
public class BatchSqlExecutor implements SqlExecutor<int[]> {
    @Override
    public int[] run(RowSql rowSql, StatementWrapper statementWrapper) throws SQLException, ClassTypeException {
        String[] allSql = rowSql.getNativeSql().split(";");
        assert allSql.length == rowSql.getParamsList().size() : "sql and params size not match";
        PreparedStatement preparedStatement = null;
        preparedStatement = statementWrapper.getPrepareStatement();
        for (int i = 0; i < rowSql.getParamsList().size(); i++) {
            preparedStatement.addBatch(allSql[i]);
            StatementUtils.setParam(rowSql.getParamsList().get(i), preparedStatement, rowSql.getTypeConvertors());
        }
        return preparedStatement.executeBatch();
    }
}
