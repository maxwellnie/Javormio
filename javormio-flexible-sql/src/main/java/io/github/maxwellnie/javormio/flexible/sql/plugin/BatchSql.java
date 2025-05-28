package io.github.maxwellnie.javormio.flexible.sql.plugin;

import io.github.maxwellnie.javormio.common.java.sql.SqlParameter;

/**
 * 批量SQL
 *
 * @author Maxwell Nie
 */
public class BatchSql extends Sql {
    /**
     * 每组参数的数量
     */
    protected int groupParameterCount;

    public BatchSql(String sql, SqlParameter[] parameters, int groupParameterCount) {
        super(sql, parameters);
        this.groupParameterCount = groupParameterCount;
    }

    public BatchSql(String sql, SqlParameter[] parameters) {
        super(sql, parameters);
    }

    public int getGroupParameterCount() {
        return groupParameterCount;
    }

    public void setGroupParameterCount(int groupParameterCount) {
        this.groupParameterCount = groupParameterCount;
    }
}
