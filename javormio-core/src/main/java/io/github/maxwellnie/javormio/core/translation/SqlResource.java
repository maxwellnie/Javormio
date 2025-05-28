package io.github.maxwellnie.javormio.core.translation;


import io.github.maxwellnie.javormio.common.java.api.Resource;
import io.github.maxwellnie.javormio.common.java.sql.SqlType;

/**
 * sql资源
 *
 * @author Maxwell Nie
 */
public class SqlResource implements Resource {
    /**
     * sql模板或完整的sql语句
     */
    private final String sql;
    /**
     * sql类型
     */
    private final SqlType type;
    /**
     * sql资源缓存键
     */
    private final SqlKey key;
    /**
     * sql行数
     */
    private final int line;

    public SqlResource(String sql, SqlType type, SqlKey key, int line) {
        this.sql = sql;
        this.type = type;
        this.key = key;
        this.line = line;
    }

    public String getSql() {
        return sql;
    }

    public SqlType getType() {
        return type;
    }

    public SqlKey getKey() {
        return key;
    }

    public int getLine() {
        return line;
    }
}
