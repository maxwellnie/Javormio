package io.github.maxwellnie.javormio.core.translation.method.impl;

import io.github.maxwellnie.javormio.core.Context;
import io.github.maxwellnie.javormio.core.execution.executor.parameter.ExecutableSql;
import io.github.maxwellnie.javormio.core.translation.method.BaseSqlMethod;
import io.github.maxwellnie.javormio.core.translation.table.TableInfo;
import io.github.maxwellnie.javormio.core.translation.table.column.ColumnInfo;
import io.github.maxwellnie.javormio.core.translation.table.column.ColumnType;
import io.github.maxwellnie.javormio.core.translation.table.primary.AutoKeyGenerator;
import io.github.maxwellnie.javormio.core.translation.table.primary.PrimaryInfo;

import java.util.Properties;

/**
 * @author Maxwell Nie
 */
public class InsertBatch<T> extends BaseSqlMethod<T> {
    public InsertBatch(Class<T> entityClass) {
        super(entityClass);
    }

    @Override
    public Object invoke(int methodFeatureCode, String namespace, Context context, Object[] args) throws Throwable {
        ExecutableSql executableSql = new ExecutableSql();
        return null;
    }
}
