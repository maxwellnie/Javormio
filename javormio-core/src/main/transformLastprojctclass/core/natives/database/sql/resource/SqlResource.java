package com.maxwellnie.velox.sql.core.natives.database.sql.resource;

import com.maxwellnie.velox.sql.core.natives.database.sql.SqlType;
import com.maxwellnie.velox.sql.core.natives.resource.Resource;

import java.util.Objects;

/**
 * sql模板
 * @author Maxwell Nie
 */
public class SqlResource implements Resource<String> {
    private String sqlTemplate;
    private String sqlKey;
    private SqlType sqlType;

    public SqlResource(String sqlTemplate, String sqlKey, SqlType sqlType) {
        this.sqlTemplate = sqlTemplate;
        this.sqlKey = sqlKey;
        this.sqlType = sqlType;
    }

    @Override
    public String getResourceId() {
        return this.sqlTemplate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SqlResource that = (SqlResource) o;
        return Objects.equals(sqlTemplate, that.sqlTemplate) && Objects.equals(sqlKey, that.sqlKey) && sqlType == that.sqlType;
    }

    public String getSqlTemplate() {
        return sqlTemplate;
    }

    public String getSqlKey() {
        return sqlKey;
    }

    public SqlType getSqlType() {
        return sqlType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(sqlTemplate, sqlKey, sqlType);
    }

    @Override
    public String toString() {
        return "SqlResource{" +
                "sqlTemplate='" + sqlTemplate + '\'' +
                ", sqlKey='" + sqlKey + '\'' +
                ", sqlType=" + sqlType +
                '}';
    }
}
