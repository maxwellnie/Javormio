package com.maxwellnie.velox.sql.core.natives.database.sql.row;

import com.maxwellnie.velox.sql.core.natives.dao.SqlDecorator;
import com.maxwellnie.velox.sql.core.natives.database.sql.SqlType;
import com.maxwellnie.velox.sql.core.natives.type.convertor.TypeConvertor;
import com.sun.istack.internal.NotNull;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Maxwell Nie
 */
public class RowSql {
    private String nativeSql;
    private List<List<Object>> paramsList = Collections.synchronizedList(new LinkedList<>());
    private List<TypeConvertor<?>> typeConvertors = Collections.synchronizedList(new LinkedList<>());
    private SqlType sqlType;
    private SqlDecorator<?> sqlDecorator;
    public RowSql() {
    }

    public RowSql(String nativeSql) {
        this.nativeSql = nativeSql;
    }

    public String getNativeSql() {
        return nativeSql;
    }

    public void setNativeSql(String nativeSql) {
        this.nativeSql = nativeSql;
    }

    public List<List<Object>> getParamsList() {
        return paramsList;
    }

    public void setParamsList(List<List<Object>> paramsList) {
        this.paramsList = paramsList;
    }

    public List<TypeConvertor<?>> getTypeConvertors() {
        return typeConvertors;
    }

    public void setTypeConvertors(List<TypeConvertor<?>> typeConvertors) {
        this.typeConvertors = typeConvertors;
    }

    public SqlType getSqlType() {
        return sqlType;
    }

    public void setSqlType(SqlType sqlType) {
        this.sqlType = sqlType;
    }

    public SqlDecorator<?> getSqlDecorator() {
        return sqlDecorator;
    }

    public void setSqlDecorator(SqlDecorator<?> sqlDecorator) {
        this.sqlDecorator = sqlDecorator;
    }

    @Override
    public String toString() {
        return nativeSql;
    }
}
