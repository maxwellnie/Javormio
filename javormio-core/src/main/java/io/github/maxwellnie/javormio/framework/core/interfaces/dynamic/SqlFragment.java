package io.github.maxwellnie.javormio.framework.core.interfaces.dynamic;

import io.github.maxwellnie.javormio.framework.common.java.type.TypeHandler;

import java.util.LinkedList;
import java.util.List;

/**
 * The sql fragment.
 *
 * @author Maxwell Nie
 */
public class SqlFragment {
    String sql;
    List<TypeHandler<?>> typeHandlers;
    List<Object> params;

    public SqlFragment() {
        typeHandlers = new LinkedList<>();
        params = new LinkedList<>();
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public List<TypeHandler<?>> getTypeHandlers() {
        return typeHandlers;
    }

    public void setTypeHandlers(List<TypeHandler<?>> typeHandlers) {
        this.typeHandlers = typeHandlers;
    }

    public List<Object> getParams() {
        return params;
    }

    public void setParams(List<Object> params) {
        this.params = params;
    }

    public void addParam(Object param, TypeHandler<?> typeHandler) {
        params.add(param);
        typeHandlers.add(typeHandler);
    }
}

