package io.github.maxwellnie.javormio.core.api.dynamic;

import io.github.maxwellnie.javormio.common.java.type.TypeHandler;

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
        lazyParams();
        this.params = params;
    }

    public void addParam(Object param, TypeHandler<?> typeHandler) {
        lazyTypeHandlers();
        lazyParams();
        params.add(param);
        typeHandlers.add(typeHandler);
    }

    void lazyTypeHandlers() {
        if (typeHandlers == null)
            typeHandlers = new LinkedList<>();
    }

    void lazyParams() {
        if (params == null)
            params = new LinkedList<>();
    }
}

