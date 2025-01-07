package io.github.maxwellnie.javormio.core.database.sql.method;

import io.github.maxwellnie.javormio.core.DataAPIContext;

/**
 * @author Maxwell Nie
 */
public class UpdateSqlMethod extends BaseSqlMethod {
    public UpdateSqlMethod(DataAPIContext dataAPIContext) {
        super(dataAPIContext);
    }

    @Override
    public Object invokeExactly(Object... args) {
        return null;
    }

    @Override
    public Object invoke(Object[] args) {
        return null;
        // SELECT * FROM tb_user WHERE id = ?
        //String sql, Object[] params
    }
}
