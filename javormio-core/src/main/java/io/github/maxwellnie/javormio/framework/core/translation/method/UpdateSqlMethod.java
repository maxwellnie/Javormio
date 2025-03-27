package io.github.maxwellnie.javormio.framework.core.translation.method;

import io.github.maxwellnie.javormio.framework.Context;

/**
 * @author Maxwell Nie
 */
public class UpdateSqlMethod extends BaseSqlMethod {
    public UpdateSqlMethod(Context context) {
        super(context);
    }

    @Override
    public Object invoke(int methodFeatureCode, Object[] args) {
        return null;
        // SELECT * FROM tb_user WHERE id = ?
        //String sql, Object[] params
    }
}
