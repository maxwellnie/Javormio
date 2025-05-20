package io.github.maxwellnie.javormio.flexible.sql.plugin.function;

import io.github.maxwellnie.javormio.common.java.type.TypeHandler;
import io.github.maxwellnie.javormio.core.translation.SqlParameter;
import io.github.maxwellnie.javormio.core.translation.sql.SqlFragment;

/**
 * @author Maxwell Nie
 */
public abstract class SqlFunction<T> {
    protected String name;
    protected SqlParameter[] sqlParameters;
    protected Class<T> returnType;
    protected TypeHandler<T> resultTypeHandler;

    public SqlFunction(String name, SqlParameter[] sqlParameters, Class<T> returnType, TypeHandler<T> resultTypeHandler) {
        this.name = name;
        this.sqlParameters = sqlParameters;
        this.returnType = returnType;
        this.resultTypeHandler = resultTypeHandler;
    }

    public TypeHandler<T> getResultTypeHandler() {
        return resultTypeHandler;
    }

    public String getName() {
        return name;
    }

    public SqlParameter[] getSqlParameters() {
        return sqlParameters;
    }

    public Class<T> getReturnType() {
        return returnType;
    }

    public abstract SqlFragment toSqlFragment();
}
