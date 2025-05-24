package io.github.maxwellnie.javormio.core.translation.method.impl;

import io.github.maxwellnie.javormio.common.java.reflect.MethodFeature;
import io.github.maxwellnie.javormio.core.Context;
import io.github.maxwellnie.javormio.core.execution.result.ResultSetConvertor;
import io.github.maxwellnie.javormio.core.execution.statement.StatementHelper;
import io.github.maxwellnie.javormio.core.translation.table.TableInfo;

/**
 * @author Maxwell Nie
 */
public class MapperContext<T, R> {
    protected Context context;
    protected String namespace;
    protected MethodFeature methodFeature;
    protected ResultSetConvertor<R> convertor;
    protected TableInfo<T> tableInfo;
    protected StatementHelper statementHelper;

    public Context getContext() {
        return context;
    }

    public String getNamespace() {
        return namespace;
    }

    public MethodFeature getMethodFeature() {
        return methodFeature;
    }

    public ResultSetConvertor<R> getConvertor() {
        return convertor;
    }

    public TableInfo<T> getTableInfo() {
        return tableInfo;
    }

    public StatementHelper getStatementHelper() {
        return statementHelper;
    }
}
