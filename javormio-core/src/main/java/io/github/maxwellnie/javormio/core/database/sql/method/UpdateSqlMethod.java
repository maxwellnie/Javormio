package io.github.maxwellnie.javormio.core.database.sql.method;

import io.github.maxwellnie.javormio.core.OperationContext;

/**
 * @author Maxwell Nie
 */
public class UpdateSqlMethod extends BaseSqlMethod {
    public UpdateSqlMethod(OperationContext operationContext) {
        super(operationContext);
    }

    @Override
    public Object invokeExactly(int methodFeatureCode, Object... args) {
        return null;
    }

    @Override
    public Object invoke(int methodFeatureCode, Object[] args) {
        return null;
        // SELECT * FROM tb_user WHERE id = ?
        //String sql, Object[] params
    }
}
