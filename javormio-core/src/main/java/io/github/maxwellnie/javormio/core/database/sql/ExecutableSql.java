package io.github.maxwellnie.javormio.core.database.sql;

import java.util.List;

/**
 * 可直接打开报表提交到数据库的SQL对象
 *
 * @author Maxwell Nie
 */
public class ExecutableSql {
    /**
     * 命名空间
     */
    private String namespace;
    /**
     * SQL列表
     */
    private String[] sqlList;
    /**
     * 参数列表
     */
    private List<SqlParameter[]> parametersList;
    /**
     * 类型
     */
    private SqlType type;

    public ExecutableSql(String namespace, String[] sqlList, List<SqlParameter[]> parametersList, SqlType type) {
        this.namespace = namespace;
        this.sqlList = sqlList;
        this.parametersList = parametersList;
        this.type = type;
    }

    public ExecutableSql() {
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String[] getSqlList() {
        return sqlList;
    }

    public void setSqlList(String[] sqlList) {
        this.sqlList = sqlList;
    }

    public List<SqlParameter[]> getParametersList() {
        return parametersList;
    }

    public void setParametersList(List<SqlParameter[]> parametersList) {
        this.parametersList = parametersList;
    }

    public SqlType getType() {
        return type;
    }

    public void setType(SqlType type) {
        this.type = type;
    }
}
